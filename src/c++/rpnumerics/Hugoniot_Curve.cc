#include "Hugoniot_Curve.h"

// TODO: Move or blend fill_with_jet as a method or methods of JetMatrix.
//
void Hugoniot_Curve::fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
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

void Hugoniot_Curve::set_reference_point(const RealVector &ref){
    Uref.resize(2);

    for (int i = 0; i < 2; i++) Uref.component(i) = ref.component(i);

    fill_with_jet(ff, 2, Uref.components(), 1, Fref, JFref, 0);
//    fill_with_jet(aa, 2, Uref.components(), 1, Gref, JGref, 0);

    return;
}

Hugoniot_Curve::Hugoniot_Curve(const FluxFunction *f, const AccumulationFunction *a, 
                               const RealVector &min, const RealVector &max, 
                               const int *cells,
                               const RealVector &ref){

    ff = f; aa = a;

    Fref  = new double[2];
    Gref  = new double[2];
    JFref = new double[4];
    JGref = new double[4];

    set_reference_point(ref);
    pmin=min;
    pmax=max;

    // Create the grid...
    //
    number_of_cells = new int[2];
    for (int i = 0; i < 2; i++){
        number_of_cells[i] = cells[i];
//        pmin.component(i) = min.component(i);
//        pmax.component(i) = max.component(i);
    }

    int dim = 2;

    double delta[2];
    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i)))/(double)(number_of_cells[i]);

    grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    F_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    G_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

    for (int i = 0; i <= number_of_cells[0]; i++){
        for (int j = 0; j <= number_of_cells[1]; j++){
            grid(i, j).resize(dim);
            F_on_grid(i, j).resize(dim);
            G_on_grid(i, j).resize(dim);

            grid(i, j).component(0) = pmin.component(0) + (double)i*delta[0];
            grid(i, j).component(1) = pmin.component(1) + (double)j*delta[1];
        }
    }

    // ...and fill several values on the grid.
    //
    hc_fill_values_on_grid();
}

Hugoniot_Curve::~Hugoniot_Curve(){
    delete [] number_of_cells;

    delete [] JGref;
    delete [] JFref;

    delete [] Gref;
    delete [] Fref;
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
//                  ff, aa: Flux and accumulationdouble *Fref, *Gref; functions that apply from R^n to R^n.
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
//                      dd: Array of vectors that will hold the value of the directional derivatives
//                          at each vertex of the grid. This array, like the grid, must be of size
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
void Hugoniot_Curve::hc_fill_values_on_grid(void){
    
    // Dimension of space
    int dim = pmin.size();

    // Number of elements in the grid.
    int n = 1;
    for (int i = 0; i < dim; i++) n *= number_of_cells[i] + 1;

    // Fill the arrays with the value of the flux and accumulation functions at every point in the grid.
    double point[dim], F[dim], G[dim];
    for (int i = 0; i < n; i++){
        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);

        fill_with_jet((RpFunction*)ff, dim, point, 0, F, 0, 0);
        fill_with_jet((RpFunction*)aa, dim, point, 0, G, 0, 0);

        for (int j = 0; j < dim; j++){
            F_on_grid(i).component(j) = F[j];
            G_on_grid(i).component(j) = G[j];
        }

    }

    return;
}

/* DE AQUI EN ADELANTE ES NUEVO... */
/***********************************************************************
c
c      this routine computes the directional derivative of the eigen-
c      value in the direction of the right eigenvector at the vertices
c      of the bottom triangle or the top triangle of a rectangle with
c      lower left corner  (x0,y0)  and side lengths  dx,dy.  the
c      choice is made by  sqrtyp  (1-lower left triangle only, 2-both
c      triangles).  output values  (forig, fside, ftop  for sqrtyp=1,
c      fopps  also for sqrtyp = 2)  are used in routine  vcontu  to
c      find the curve where there is loss of genuine nonlinearity.
c
c      ignore the diagrams below
c
c       corner coords     diag slope -1      diag slope +1
c      ---------------    -------------      -------------
c      (x0,y1) (x1,y1)    top      opps      opps     top
c
c
c
c      (x0,y0) (x1,y0)    orig     side      side     orig
c
c
c      call setfam ( family )  before using this routine.
c
***********************************************************************/

int Hugoniot_Curve::function_on_square(double *foncub, int i, int j, int is_square) { // ver como esta en el ContourMethod.cc
    double dhw, dho;
    double f_aux[4];
    double epsilon = 1.0e-2;

    for (int l = 0; l < 2; l++){
        for (int k = 0; k < 2; k++){
            dhw = G_on_grid(i + l, j + k).component(0) - Gref[0];
            dho = G_on_grid(i + l, j + k).component(1) - Gref[1];

            if (fabs(dhw) + fabs(dho) >= epsilon){
                double dfw = F_on_grid(i + l, j + k).component(0) - Fref[0];
                double dfo = F_on_grid(i + l, j + k).component(1) - Fref[1];

                f_aux[l*2 + k] = dho*dfw - dhw*dfo;
            }
            else {
                // First-order expansion of F in terms of G.
                //
                printf("Hugoniot_Curve::function_on_square(): if epsilon.\n");

                f_aux[l*2 + k] = (JFref[0]*JGref[3] - JFref[2]*JGref[1] + JFref[1]*JGref[2] - JFref[3]*JGref[0])*dhw*dho +
                                 (JFref[1]*JGref[3] - JFref[3]*JGref[1])*dho*dho + 
                                 (JFref[0]*JGref[2] - JFref[2]*JGref[0])*dhw*dhw;
            }
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]
   
    // Only useful if the cell is a square. Otherwise this information
    // will be discarded by contour2d.
    //
    foncub[2] = f_aux[3];  // Was: foncub[0][2]

    return 1;
}

int Hugoniot_Curve::curve(std::vector<RealVector> &hugoniot_curve){
    hugoniot_curve.clear();
    
    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);

    int info = ContourMethod::contour2d(this, rect, number_of_cells, hugoniot_curve);

    return info;
}

