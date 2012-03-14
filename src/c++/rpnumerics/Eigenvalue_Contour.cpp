#include "Eigenvalue_Contour.h"

void Eigenvalue_Contour::fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    flux_object->jet(state_c, c_jet, degree);

    // Fill F
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet(i);

    // Fill J
    if (J != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                J[i * n + j] = c_jet(i, j);
            }
        }
    }

    // Fill H
    if (H != 0) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    H[(i * n + j) * n + k] = c_jet(i, j, k); 
                }
            }
        }
    }

    return;
}

Eigenvalue_Contour::Eigenvalue_Contour(const FluxFunction *f, const AccumulationFunction *a, 
                                       const RealVector &min, const RealVector &max, 
                                       const int *cells, const Boundary *b){

    int dim = 2;

    ff = f; aa = a;
    boundary = b;

    double JF[4];
    double JG[4];

    // Create the grid...
    //
    pmin.resize(dim); pmax.resize(dim);

    number_of_cells = new int[2];
    for (int i = 0; i < 2; i++){
        number_of_cells[i] = cells[i];
        pmin.component(i) = min.component(i);
        pmax.component(i) = max.component(i);
    }

    double delta[2];
    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i)))/(double)(number_of_cells[i]);

    grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    eigenvalues_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    eigenvalues_are_real_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    vertex_in_domain.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

    for (int i = 0; i <= number_of_cells[0]; i++){
        for (int j = 0; j <= number_of_cells[1]; j++){
            grid(i, j).resize(dim);
            grid(i, j).component(0) = pmin.component(0) + (double)i*delta[0];
            grid(i, j).component(1) = pmin.component(1) + (double)j*delta[1];

            if (!boundary->inside(grid(i, j))){
                vertex_in_domain(i, j) = false;
                continue;
            }
            else vertex_in_domain(i, j) = true;

            // Fill the eigenvalues
            eigenvalues_on_grid(i, j).resize(dim);

            fill_with_jet((RpFunction*)ff, dim, grid(i, j).components(), 1, 0, JF, 0);
            fill_with_jet((RpFunction*)aa, dim, grid(i, j).components(), 1, 0, JG, 0);

            std::vector<eigenpair> e;
            Eigen::eig(dim, JF, JG, e);
            for (int k = 0; k < e.size(); k++) eigenvalues_on_grid(i, j).component(k) = e[k].r;

            // Are they real?
            eigenvalues_are_real_on_grid(i, j).resize(dim);
            for (int k = 0; k < e.size(); k++) eigenvalues_are_real_on_grid(i, j)[k] = (fabs(e[k].i) < 1e-10);
        }
    }


    levels.push_back(0.0); // At least this level can be contoured.
    family = 0;            // At least for this family.

    level = 0.0;
    family = 0;
}

Eigenvalue_Contour::~Eigenvalue_Contour(){
    delete [] number_of_cells;
}

// Find the minimum and maximum lambdas, as were computed on the grid.
//
void Eigenvalue_Contour::find_minmax_lambdas(int f, double &min, double &max){
    int r = eigenvalues_on_grid.rows();
    int c = eigenvalues_on_grid.cols();

    min = max = eigenvalues_on_grid(0).component(f);

    for (int i = 1; i < r*c; i++){
        if (eigenvalues_on_grid(i).component(f) > max) max = eigenvalues_on_grid(i).component(f);
        if (eigenvalues_on_grid(i).component(f) < min) min = eigenvalues_on_grid(i).component(f);
    }

    return;
}

// Set the levels as a vector with arbitrary values.
// The levels will be sorted from minimum to maximum.
//
void Eigenvalue_Contour::set_levels(int f, const std::vector<double> &l){
    family = f;

    levels.clear();
    levels.resize(l.size());

    for (int i = 0; i < l.size(); i++) levels[i] = l[i];

    std::sort(levels.begin(), levels.end());

    return;
}

// Set the levels from the minimum towards the maximum, with a
// uniform separation between levels.
//
void Eigenvalue_Contour::set_levels_from_delta(int f, double delta_l){
    family = f;
    levels.clear();

    double min_lambda, max_lambda;
    find_minmax_lambdas(family, min_lambda, max_lambda);
    
    double lambda = min_lambda;
    while (lambda < max_lambda){
        levels.push_back(lambda);
        lambda += delta_l;
    }
    
    return;
}

// Set the number of levels to be uniformly distributed.
//
void Eigenvalue_Contour::set_number_levels(int f, int n){
    family = f;
    levels.clear();

    n = max(n, 2); // At least two levels.

    double min_lambda, max_lambda;
    find_minmax_lambdas(family, min_lambda, max_lambda);

    double delta_l = (max_lambda - min_lambda)/(n - 1);
    levels.resize(n);
    for (int i = 0; i < n; i++) levels[i] = min_lambda + i*delta_l;

    return;
}

// Set the levels radiating from the level at the given point, 
// with the given distance between levels.
//
void Eigenvalue_Contour::set_levels_from_point(int f, const RealVector &p, double delta_l){
    family = f;

    levels.clear();

    double JF[4], JG[4];

    fill_with_jet((RpFunction*)ff, p.size(), ((RealVector)p).components(), 1, 0, JF, 0);
    fill_with_jet((RpFunction*)aa, p.size(), ((RealVector)p).components(), 1, 0, JG, 0);

    std::vector<eigenpair> e;
    Eigen::eig(p.size(), JF, JG, e);
    double level = e[family].r;

    double now_level = level - delta_l;

    double min_lambda, max_lambda;
    find_minmax_lambdas(family, min_lambda, max_lambda);   

    // Downwards...
    while (now_level >= min_lambda){
        levels.push_back(now_level);
        now_level -= delta_l;
    }

    now_level = level;

    // Upwards
    while (now_level <= max_lambda){
        levels.push_back(now_level);
        now_level += delta_l;
    }

    std::sort(levels.begin(), levels.end());

    return;
}

// Set the level for a given family.
//
void Eigenvalue_Contour::set_level(double l, int f){
    level = l;
    family = f;

    return;
}

// Set the level at the given point.
//
void Eigenvalue_Contour::set_level_from_point(int f, const RealVector &p){
//    set_family(f);
    family = f;

//    levels.clear();

    double JF[4], JG[4];

    fill_with_jet((RpFunction*)ff, p.size(), ((RealVector)p).components(), 1, 0, JF, 0);
    fill_with_jet((RpFunction*)aa, p.size(), ((RealVector)p).components(), 1, 0, JG, 0);

    std::vector<eigenpair> e;
    Eigen::eig(p.size(), JF, JG, e);
//    levels.push_back(e[family].r); //printf("Level = %f\n", e[family].r);
    level = e[family].r;

    return;
}

//void Eigenvalue_Contour::set_family(int f){
//    family = f;
//    return;
//}

// Foncub.
//
int Eigenvalue_Contour::function_on_square(double *foncub, int i, int j, int is_square){
    if (!vertex_in_domain(i + 1, j + 0)) return 0;
    if (!eigenvalues_are_real_on_grid(i + 1, j + 0)[family]) return 0;
    foncub[0] = eigenvalues_on_grid(i + 1, j + 0).component(family) - level;

    if (!vertex_in_domain(i + 0, j + 0)) return 0;
    if (!eigenvalues_are_real_on_grid(i + 0, j + 0)[family]) return 0;
    foncub[1] = eigenvalues_on_grid(i + 0, j + 0).component(family) - level;

    if (!vertex_in_domain(i + 1, j + 1)) return 0;
    if (!eigenvalues_are_real_on_grid(i + 1, j + 1)[family]) return 0;
    foncub[2] = eigenvalues_on_grid(i + 1, j + 1).component(family) - level;

    if (!vertex_in_domain(i + 0, j + 1)) return 0;
    if (!eigenvalues_are_real_on_grid(i + 0, j + 1)[family]) return 0;
    foncub[3] = eigenvalues_on_grid(i + 0, j + 1).component(family) - level;

    return 1;
}

int Eigenvalue_Contour::curve(std::vector< std::vector<RealVector> > &eigenvalues_curves,
                              std::vector<double> &out_levels){

    eigenvalues_curves.clear();
    out_levels.clear();

    int info;

    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);

    for (int i = 0; i < levels.size(); i++){
        level = levels[i];
        std::vector<RealVector> curve;
//        info = ContourMethod::contour2d(this, (Boundary *)boundary,rect, number_of_cells, curve);

        if (curve.size() > 0){
            eigenvalues_curves.push_back(curve);
            out_levels.push_back(level);
        }
    }

    return info;
}

int Eigenvalue_Contour::curve(std::vector<RealVector> &curve, double &l){

    curve.clear();

    int info;

    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);

//    info = ContourMethod::contour2d(this, (Boundary *) boundary,rect, number_of_cells, curve);
    l = level;

    return info;
}

