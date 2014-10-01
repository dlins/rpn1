#include "Bifurcation_Curve.h"

void Bifurcation_Curve::create_grid(const RealVector &pmin, const RealVector &pmax, const int *number_of_cells, Matrix<RealVector> &p){
    int dim = pmin.size();

    double delta[pmin.size()];

    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i)))/(double)(number_of_cells[i] - 1);
    

    for (int i = 0; i < number_of_cells[0]; i++){
        for (int j = 0; j < number_of_cells[1]; j++){
            //printf("Here\n");

            p(i, j).resize(dim);

            p(i, j).component(0) = pmin.component(0) + (double)i*delta[0];
            p(i, j).component(1) = pmin.component(1) + (double)j*delta[1];
        }
    }

    printf("Inside create_grid()\n");

    return;
}

// Given the extreme points of a rectangular domain
// and the number of grid points along each dimension,
// compute the vertices of the grid thus defined,
// and in said vertices a series of values.
//
//          pmin, pmax: Extremes of the domain. Ideally, 
//
//                          pmin[i] <= pmax[i] for 0 <= i < dimension of space.
//
//                      In practice, this will be checked out within the body of the function.
//                  ff, aa: Flux and accumulation functions that apply from R^n to R^n.
//     number_of_grid_pnts: The number of cells in each dimension (array defined externally).
//                    grid: The spatial grid created. Space to hold it must be reserved outside the function.
//                          The i-th dimension will have number_of_cells[i] cells. Thus, for 2D, which is the case
//                          that is being implemented, each vertex (i, j) will be of
//                          the form:
//
//                              grid[i*number_of_grid_pnts[1] + j].component(k) = pmin[k] + j*(pmax[k] - pmin[k])/(number_of_grid_pnts[k])
//
//                          where:
//
//                              0 <= i < number_of_grid_pnts[0],
//                              0 <= j < number_of_grid_pnts[1],
//                              0 <= k < 2.
//
//                          Thus, grid needs to be of size 
//
//                              number_of_grid_pnts[0]*...*number_of_grid_pnts[pmax.size() - 1].
//
//                ffv, aav: Arrays of RealVector that will hold the value of the flux and accumulation functions
//                          at each vertex of the grid. These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//                      
//                       e: Array of vectors of eigenpairs that will hold all the eigenpairs at
//                          each vertex of the grid. These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//
//             eig_is_real: Array of vectors of booleans that state if each eigenvalue at a given grid vertex is
//                          real (true) or complex (false). These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//

// TODO: Change indices i, j to k, l. i & j are reserved for grid- or cell-like uses.
void Bifurcation_Curve::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                            const FluxFunction *ff, const AccumulationFunction *aa, 
                                            const int *number_of_grid_pnts,
                                            Matrix<RealVector> &grid,
                                            Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                            Matrix< vector<eigenpair> > &e, Matrix< vector<bool> > &eig_is_real){
    
//    // Dimension of space
//    int dim = pmin.size();

//    // Create the grid proper
//    create_grid(pmin, pmax, number_of_grid_pnts, grid);

//    // Number of elements in the grid.
//    int n = 1;
//    for (int i = 0; i < dim; i++) n *= number_of_grid_pnts[i];

//    // Fill the arrays with the value of the flux and accumulation functions at every point in the grid.
//    // The eigenpairs must also be stored.
//    for (int i = 0; i < n; i++){
//        double point[dim];
//        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);

//        double F[dim], G[dim], JF[dim][dim], JG[dim][dim];
//        fill_with_jet((RpFunction*)ff, dim, point, 1, F, &JF[0][0], 0);
//        fill_with_jet((RpFunction*)aa, dim, point, 1, G, &JG[0][0], 0);

//        // Fill the values of the functions
//        ffv(i).resize(dim);
//        aav(i).resize(dim);
//        for (int j = 0; j < dim; j++){
//            ffv(i).component(j) = F[j];	
//            aav(i).component(j) = G[j];
//        }

//        // Find the eigenpairs
//        vector<eigenpair> etemp;
//        Eigen::eig(dim, &JF[0][0], &JG[0][0], etemp);

//        e(i).clear();
//        e(i).resize(etemp.size());
//        for (int j = 0; j < etemp.size(); j++) e(i)[j] = etemp[j];

//        // Decide if the eigenvalues are real or complex
//        eig_is_real(i).clear();
//        eig_is_real(i).resize(etemp.size());
//        for (int j = 0; j < etemp.size(); j++){
//            if (fabs(etemp[j].i) < epsilon) eig_is_real(i)[j] = true;
//            else                            eig_is_real(i)[j] = false;
//        }
//    }

    Matrix<bool> is_inside;

    fill_values_on_grid(pmin, pmax, 
                        ff, aa, 
                        number_of_grid_pnts,
                        grid,
                        ffv, aav, 
                        e, eig_is_real,
                        0, is_inside);
    return;
}

void Bifurcation_Curve::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                            const FluxFunction *ff, const AccumulationFunction *aa, 
                                            const int *number_of_grid_pnts,
                                            Matrix<RealVector> &grid,
                                            Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                            Matrix< std::vector<double> > &e, Matrix< vector<bool> > &eig_is_real){

    Matrix< std::vector<eigenpair> > temp(grid.rows(), grid.cols());
    fill_values_on_grid(pmin, pmax, ff, aa, number_of_grid_pnts, grid, ffv, aav, temp, eig_is_real);

    for (int i = 0; i < grid.rows(); i++){
        for (int j = 0; j < grid.cols(); j++){
            e(i, j).resize(pmin.size());
//            printf("temp(%d, %d).size() = %d\n", i, j, temp(i, j).size());
            for (int k = 0; k < pmin.size(); k++) e(i, j)[k] = temp(i, j)[k].r;
        }
    }

    return;
}

Bifurcation_Curve::Bifurcation_Curve(){
    epsilon = 1e-6;
}

Bifurcation_Curve::~Bifurcation_Curve(){
}

// This function that fills F, J and H using jets.
//
// Arguments:
//
//    flux_object: Pointer to an object of a FluxFunction-derived class. This
//                 object will perform the jet-related operations.
//              n: Dimension of the space.
//             in: Array that contains the point where F, J and H will be computed.
//         degree: 0: Compute F,
//                 1: Compute F and J,
//                 2: Compute F, J and H.
//              F: An array, externally reserved, where the value of the function
//                 at in is stored.
//              J: A matrix, externally reserved, where the value of the Jacobian
//                 of the function at in is stored. Should be 0 if degree == 0.
//              H: An array, externally reserved, where the value of the Hessian
//                 of the function at in is stored. Should be 0 if degree == 1.
//
// The user MUST reserve the space needed for F, J and H. If some of these are not needed, they
// should be set to zero. For example, the rarefaction only uses J. Therefore, the
// user should reserve an array of n*n doubles for J, and invoke this function passing 0 for F and H.
//
void Bifurcation_Curve::fill_with_jet(RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H){
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
    if (F != 0) for (int i = 0; i < n; i++) F[i] = c_jet.get(i);

    // Fill J
    if (J != 0){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                J[i*n + j] = c_jet.get(i, j);
            }
        }
    }
    
    // Fill H
    if (H != 0){
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                for (int k = 0; k < n; k++){
                    H[(i*n + j)*n + k] = c_jet.get(i, j, k); // Check this!!!!!!!!
                }
            }
        }    
    }

    return;
}

//void Bifurcation_Curve::create_cells(int *number_of_grid_pnts, int family, Matrix<RealEigenvalueCell> &mc, Matrix< std::vector<bool> > *eigenvalues_on_the_grid){
//    int n = number_of_grid_pnts[0] - 1;
//    int m = number_of_grid_pnts[1] - 1;

//    mc.resize(n, m);

//    for (int i = 0; i < n; i++){
//        for (int j = 0; j < m; j++) mc(i, j) = RealEigenvalueCell(i, j, true, family, eigenvalues_on_the_grid); // Only squares for the moment being.
//    }
//    
//    return;
//}

//void Bifurcation_Curve::validate_cells(int family, int *number_of_cells, Matrix<RealEigenvalueCell> &mc, Matrix<bool> &mb){
//    mb.resize(number_of_cells[0], number_of_cells[1]);

//    for (int i = 0; i < number_of_cells[0]; i++){
//        for (int j = 0; j < number_of_cells[1]; j++){
//            bool temp = mc(i, j).is_complex(family); 
//            mb(i, j) = temp;
//        }
//    }
//    return;
//}

// Each cell's indices are those of their lower-left vertex, which lies on a grid:
//
//     3     2
//     o-----o
//     |     |
//     |     |
//     o-----o
//     0     1 
//
//     0 = (i, j),
//     1 = (i + 1, j),
//     2 = (i + 1, j + 1),
//     3 = (i, j + 1).
//

//TODO: Esta era a original, esta faltando modificar Extension_Curve
void Bifurcation_Curve::validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex){
    Matrix<bool> temp_is_inside(original.rows(), original.cols());

    for (int i = 0; i < original.rows(); i++){
        for (int j = 0; j < original.cols(); j++){
            temp_is_inside(i, j) = true;
        }
    }

    validate_cells(family, type_of_cells, original, mb_is_complex, temp_is_inside);

    return;
}

// TODO: A grid may contain both square and triangular cells. Therefore, type_of_cells will become a Matrix<bool>.
void Bifurcation_Curve::validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex, Matrix<bool> &mb_is_inside){
    int rows = original.rows() - 1; 
    int cols = original.cols() - 1;

    mb_is_complex.resize(rows, cols);

    for (int i = 0; i < rows; i++){
        for (int j = 0; j < cols; j++){
            if(mb_is_inside(i+1,j+1)){ // TODO: Only if the whole cell is in domain, it would be validated
            mb_is_complex(i, j) = false;

            // Vertex 0
            if (!(original(i, j)[family])){
                mb_is_complex(i, j) = true;
                return;
            }
            // Vertex 1
            if (!(original(i + 1, j)[family])){
                mb_is_complex(i, j) = true;
                return;
            }
            // Vertex 3
            if (!(original(i, j + 1)[family])){
                mb_is_complex(i, j) = true;
                return;
            }

            // Vertex 2
            // Squares only
            if (type_of_cells){
                if (!(original(i + 1, j + 1)[family])){
                    mb_is_complex(i, j) = true;
                    return;
                }
            }
            }
        }
    }
    return;
}

// Originally this function was: preplftc, and is defined in: locimp.F.
//
// ALWAYS: flux and accum are 2x4 matrices.
//         lambda is a vector with 4 elements.
//
//     3     2
//     o-----o
//     |     |
//     |     |
//     o-----o
//     0     1 
//
//     0 = (i, j),
//     1 = (i + 1, j),
//     2 = (i + 1, j + 1),
//     3 = (i, j + 1).
//
// TODO;: This function must also take into account the fact that cells can be triangles.
void Bifurcation_Curve::prepare_cell(int i, int j, int family, Matrix< std::vector<double> > &eigen, Matrix<RealVector> &flux_values, Matrix<RealVector> &accum_values, double *lambda, Matrix<double> &flux, Matrix<double> &accum){

//    lambda[0] = eigen(i, j)[family];
//    flux(0, 0) = flux_values(i, j).component(0);
//    flux(1, 0) = flux_values(i, j).component(1);
//    accum(0, 0) = accum_values(i, j).component(0);
//    accum(1, 0) = accum_values(i, j).component(1);

//    lambda[1] = eigen(i + 1, j)[family];
//    flux(0, 1) = flux_values(i + 1, j).component(0);
//    flux(1, 1) = flux_values(i + 1, j).component(1);
//    accum(0, 1) = accum_values(i + 1, j).component(0);
//    accum(1, 1) = accum_values(i + 1, j).component(1);

//    lambda[2] = eigen(i + 1, j + 1)[family];
//    flux(0, 2) = flux_values(i + 1, j + 1).component(0);
//    flux(1, 2) = flux_values(i + 1, j + 1).component(1);
//    accum(0, 2) = accum_values(i + 1, j + 1).component(0);
//    accum(1, 2) = accum_values(i + 1, j + 1).component(1);

//    lambda[3] = eigen(i, j + 1)[family];
//    flux(0, 3) = flux_values(i, j + 1).component(0);
//    flux(1, 3) = flux_values(i, j + 1).component(1);
//    accum(0, 3) = accum_values(i, j + 1).component(0);
//    accum(1, 3) = accum_values(i, j + 1).component(1);

    int domain_i, domain_j;

    for (int kr = 0; kr < 4; kr++){
        if      (kr == 0) {domain_i = i;     domain_j = j;}
        else if (kr == 1) {domain_i = i + 1; domain_j = j;}
        else if (kr == 2) {domain_i = i + 1; domain_j = j + 1;}
        else if (kr == 3) {domain_i = i;     domain_j = j + 1;}

        lambda[kr]   = eigen(domain_i, domain_j)[family];

        flux(0, kr)  = flux_values(domain_i, domain_j).component(0);
        flux(1, kr)  = flux_values(domain_i, domain_j).component(1);

        accum(0, kr) = accum_values(domain_i, domain_j).component(0);
        accum(1, kr) = accum_values(domain_i, domain_j).component(1);
    }

    return;
}

//
// where_is_characteristic = CHARACTERISTIC_ON_CURVE or CHARACTERISTIC_ON_DOMAIN.
// 
bool Bifurcation_Curve::prepare_segment(int i, int family, int where_is_characteristic,
                                        const std::vector< std::vector<double> > &eigen, 
                                        const std::vector<RealVector> &flux_values, 
                                        const std::vector<RealVector> &accum_values, 
                                        const std::vector<std::vector<bool> > &eig_is_real,
                                        double *lambda, 
                                        Matrix<double> &flux, 
                                        Matrix<double> &accum){

    //printf("Bifurcation_Curve::prepare_segment. eigen.size() = %d\n", eigen.size());
    //printf("    Family = %d, eigen[%d].size() = %d, eigen[%d].size() = %d\n", family, i, eigen[i].size(), i + 1, eigen[i + 1].size());

    if (where_is_characteristic == CHARACTERISTIC_ON_CURVE){
        if (!eig_is_real[i][family] || !eig_is_real[i + 1][family]) return false;
    }

    lambda[0] = eigen[i][family];                //printf("Bifurcation_Curve::prepare_segment. lambda[0] = %g\n", lambda[0]);
    flux(0, 0) = flux_values[i].component(0);
    flux(1, 0) = flux_values[i].component(1);
    accum(0, 0) = accum_values[i].component(0);
    accum(1, 0) = accum_values[i].component(1);

    lambda[1] = eigen[i + 1][family];            //printf("Bifurcation_Curve::prepare_segment. lambda[1] = %g\n", lambda[1]);
    flux(0, 1) = flux_values[i + 1].component(0);
    flux(1, 1) = flux_values[i + 1].component(1);
    accum(0, 1) = accum_values[i + 1].component(0);
    accum(1, 1) = accum_values[i + 1].component(1);

    //printf("Bifurcation_Curve::prepare_segment exit\n");

    return true;
}

void Bifurcation_Curve::fill_values_on_segments(const FluxFunction *ff, const AccumulationFunction *aa, const std::vector<RealVector> &input, 
                                                std::vector<RealVector> &vff, std::vector<RealVector> &vaa,
                                                std::vector<std::vector<double> > &vee, std::vector< std::vector<bool> > &eig_is_real){
    vff.clear();
    vaa.clear();
    vee.clear();
    
    int n = input.size();

    if (n > 1){
        int dim = input[0].size();

        vff.resize(n);
        vaa.resize(n);
        vee.resize(n);
        eig_is_real.resize(n);

        for (int i = 0; i < n; i++){
            double point[dim];
            for (int j = 0; j < dim; j++) point[j] = input[i].component(j);

            // Fill the values of the functions

            double F[dim], G[dim], JF[dim][dim], JG[dim][dim];
            fill_with_jet((RpFunction*)ff, dim, point, 1, F, &JF[0][0], 0);
            fill_with_jet((RpFunction*)aa, dim, point, 1, G, &JG[0][0], 0);

            vff[i].resize(dim);
            vaa[i].resize(dim);

            for (int j = 0; j < dim; j++){
                vff[i].component(j) = F[j];
                vaa[i].component(j) = G[j];
            }

            // Find the eigenpairs
            vector<eigenpair> etemp;
            Eigen::eig(dim, &JF[0][0], &JG[0][0], etemp);

            vee[i].clear();
            vee[i].resize(etemp.size());

            for (int j = 0; j < etemp.size(); j++) vee[i][j] = etemp[j].r;
            
            // Decide if the eigenvalues are real or complex
            eig_is_real[i].clear();
            eig_is_real[i].resize(etemp.size());
            for (int j = 0; j < etemp.size(); j++){
                if (fabs(etemp[j].i) < epsilon) eig_is_real[i][j] = true;
                else                            eig_is_real[i][j] = false;
            }
        }
    }

    return;
}

void Bifurcation_Curve::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                 const FluxFunction *ff, const AccumulationFunction *aa, 
                                 const int *number_of_grid_pnts,
                                 Matrix<RealVector> &grid,
                                 Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                 Matrix< vector<eigenpair> > &e, Matrix< vector<bool> > &eig_is_real,
                                 const Boundary *b, Matrix<bool> &is_inside){

    // Dimension of space
    int dim = pmin.size();

    // Create the grid proper
    create_grid(pmin, pmax, number_of_grid_pnts, grid);

    // Number of elements in the grid.
    int n = 1;
    for (int i = 0; i < dim; i++) n *= number_of_grid_pnts[i];

    is_inside.resize(number_of_grid_pnts[0], number_of_grid_pnts[1]);

    // Fill the arrays with the value of the flux and accumulation functions at every point in the grid.
    // The eigenpairs must also be stored.
    for (int i = 0; i < n; i++){
        double point[dim];
        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);

        bool inside;
        if (b != NULL) inside = b->inside(grid(i));
        else inside = true;

        is_inside(i) = inside;
            
        if (inside){
            double F[dim], G[dim], JF[dim][dim], JG[dim][dim];
            fill_with_jet((RpFunction*)ff, dim, point, 1, F, &JF[0][0], 0);
            fill_with_jet((RpFunction*)aa, dim, point, 1, G, &JG[0][0], 0);

            // Fill the values of the functions
            ffv(i).resize(dim);
            aav(i).resize(dim);
            for (int j = 0; j < dim; j++){
                ffv(i).component(j) = F[j];	
                aav(i).component(j) = G[j];
            }

            // Find the eigenpairs
            vector<eigenpair> etemp;
            Eigen::eig(dim, &JF[0][0], &JG[0][0], etemp);

            e(i).clear();
            e(i).resize(etemp.size());
            for (int j = 0; j < etemp.size(); j++) e(i)[j] = etemp[j];
            // Decide if the eigenvalues are real or complex
            eig_is_real(i).clear();
            eig_is_real(i).resize(etemp.size());
            for (int j = 0; j < etemp.size(); j++){
                if (fabs(etemp[j].i) < epsilon) eig_is_real(i)[j] = true;
                else                            eig_is_real(i)[j] = false;
            }
        }
    }

    return;
}

void Bifurcation_Curve::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax, 
                                 const FluxFunction *ff, const AccumulationFunction *aa, 
                                 const int *number_of_grid_pnts,
                                 Matrix<RealVector> &grid,
                                 Matrix<RealVector> &ffv, Matrix<RealVector> &aav, 
                                 Matrix< vector<double> > &e, Matrix< vector<bool> > &eig_is_real,
                                 const Boundary *b, Matrix<bool> &is_inside){

    Matrix< std::vector<eigenpair> > temp(grid.rows(), grid.cols());
    fill_values_on_grid(pmin, pmax, ff, aa, number_of_grid_pnts, grid, ffv, aav, temp, eig_is_real, b, is_inside);
//    printf("temp = %d x %d\n", temp.rows(), temp.cols());

    for (int i = 0; i < grid.rows(); i++){
        for (int j = 0; j < grid.cols(); j++){
            e(i, j).resize(pmin.size());
            //printf("is_inside(%d, %d).size() = %d\n", i, j, is_inside(i, j));
            if (is_inside(i, j)) for (int k = 0; k < pmin.size(); k++) e(i, j)[k] = temp(i, j)[k].r;
        }
    }
//    printf("Fill_values_on_grid. Line = %u\n", __LINE__);

    return;
}

