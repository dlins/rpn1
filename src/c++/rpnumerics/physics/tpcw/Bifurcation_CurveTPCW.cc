#include "Bifurcation_CurveTPCW.h"
using namespace std;
#include <iostream>

void Bifurcation_CurveTPCW::create_grid(const RealVector &pmin, const RealVector &pmax, const int *number_of_cells, Matrix<RealVector> &p) {
    int dim = pmin.size();

    double delta[pmin.size()];

    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i))) / (double) (number_of_cells[i] - 1);

    for (int i = 0; i < number_of_cells[0]; i++) {
        for (int j = 0; j < number_of_cells[1]; j++) {
            //            printf("Here\n");

            p(i, j).resize(dim);

            p(i, j).component(0) = pmin.component(0) + (double) i * delta[0];
            p(i, j).component(1) = pmin.component(1) + (double) j * delta[1];
        }
    }

    //    printf("Inside create_grid()\n");

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

// TODO: Modify this method so it looks more like fill_values_on_curve() (qv below).

void Bifurcation_CurveTPCW::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
        const FluxFunction *ff, const AccumulationFunction *aa,
        const int *number_of_grid_pnts,
        Matrix<RealVector> &grid,
        Matrix<RealVector> &Redffv, Matrix<RealVector> &Redaav,
        Matrix< vector<eigenpair> > &Rede, Matrix< vector<bool> > &Redeig_is_real) {

    // Dimension of space
    int dim = pmin.size();

    int dimP = dim + 1; // TODO: ISTO É NOVO.  E A DIMENSAO PARA CALCULAR AUTOVALORES.

    // Create the grid proper
    create_grid(pmin, pmax, number_of_grid_pnts, grid);

    // Number of elements in the grid.
    int n = 1;
    for (int i = 0; i < dim; i++) n *= number_of_grid_pnts[i];

    // Fill the arrays with the value of the REDUCED flux and REDUCED accumulation functions at every point in the grid.
    //
    // The eigenvalues must also be stored:  In the TPCW case we must find them using a trick.  We find the 
    // generalized eigenvalues using u=1 for the third component of the variables.


    Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized*) ff;
    Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) aa;

    for (int i = 0; i < n; i++) {
        double point[dim];
        double pointP[dimP];

        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);
        for (int j = 0; j < dim; j++) pointP[j] = point[j];
        pointP[2] = 1.0; // THIS IS THE TRICK!

        double JF[dimP][dimP], JG[dimP][dimP];
        double F[dimP], G[dimP];
        double RedF[dimP], RedG[dimP];

        // Here we are filling the big jets.
        fill_with_jet((RpFunction*) fluxFunction, dimP, pointP, 1, F, &JF[0][0], 0); // TODO: PRECISA COLOCAR O F NO FILL WITH JET O NO?????

        fill_with_jet((RpFunction*) accumulationFunction, dimP, pointP, 1, G, &JG[0][0], 0);

        // Here we are filling the reduced jets.

        fill_with_jet((RpFunction*) fluxFunction->getReducedFlux(), dimP, pointP, 0, RedF, 0, 0);
        fill_with_jet((RpFunction*) accumulationFunction->getReducedAccumulation(), dimP, pointP, 0, RedG, 0, 0);


        // Fill the values of the functions
        Redffv(i).resize(dimP);
        Redaav(i).resize(dimP);
        for (int j = 0; j < dimP; j++) {
            Redffv(i).component(j) = RedF[j];
            Redaav(i).component(j) = RedG[j];
        }

        // Find the eigenpairs
        vector<eigenpair> Redetemp;
        Eigen::eig(dimP, &JF[0][0], &JG[0][0], Redetemp); // TODO: NAO PRECISA DE AUTOVETOR!

        Rede(i).clear();
        Rede(i).resize(Redetemp.size());
        for (int j = 0; j < Redetemp.size(); j++) Rede(i)[j] = Redetemp[j]; // TODO: ESCLARESCER ESTA LINHA

        // Decide if the eigenvalues are real or complex
        Redeig_is_real(i).clear();
        Redeig_is_real(i).resize(Redetemp.size());
        for (int j = 0; j < Redetemp.size(); j++) {
            if (fabs(Redetemp[j].i) < epsilon) Redeig_is_real(i)[j] = true;
            else Redeig_is_real(i)[j] = false;
        }
    }

    return;
}

void Bifurcation_CurveTPCW::fill_values_on_grid(const RealVector &pmin, const RealVector &pmax,
        const FluxFunction *ff, const AccumulationFunction *aa,
        const int *number_of_grid_pnts,
        Matrix<RealVector> &grid,
        Matrix<RealVector> &ffv, Matrix<RealVector> &aav,
        Matrix< std::vector<double> > &e, Matrix< vector<bool> > &eig_is_real) {

    Matrix< std::vector<eigenpair> > temp(grid.rows(), grid.cols());
    fill_values_on_grid(pmin, pmax, ff, aa, number_of_grid_pnts, grid, ffv, aav, temp, eig_is_real);

    for (int i = 0; i < grid.rows(); i++) {
        for (int j = 0; j < grid.cols(); j++) {
            e(i, j).resize(pmin.size());

            for (int k = 0; k < pmin.size(); k++) e(i, j)[k] = temp(i, j)[k].r;
        }
    }

    return;
}

Bifurcation_CurveTPCW::Bifurcation_CurveTPCW() {
    epsilon = 1e-6;
}

Bifurcation_CurveTPCW::~Bifurcation_CurveTPCW() {
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

void Bifurcation_CurveTPCW::fill_with_jet(RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];


    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    //    if (flux_object==NULL)cout<<"Nulo !"<<endl;

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
                    H[(i * n + j) * n + k] = c_jet(i, j, k); // Check this!!!!!!!!
                }
            }
        }
    }

    return;
}



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

// TODO: A grid may contain both square and triangular cells. Therefore, type_of_cells will become a Matrix<bool>.

void Bifurcation_CurveTPCW::validate_cells(int family, bool type_of_cells, Matrix< std::vector<bool> > &original, Matrix<bool> &mb_is_complex) {
    int rows = original.rows() - 1;
    int cols = original.cols() - 1;

    mb_is_complex.resize(rows, cols);

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            mb_is_complex(i, j) = false;

            // Vertex 0
            if (!(original(i, j)[family])) {
                mb_is_complex(i, j) = true;
                return;
            }
            // Vertex 1
            if (!(original(i + 1, j)[family])) {
                mb_is_complex(i, j) = true;
                return;
            }
            // Vertex 3
            if (!(original(i, j + 1)[family])) {
                mb_is_complex(i, j) = true;
                return;
            }

            // Vertex 2
            // Squares only
            if (type_of_cells) {
                if (!(original(i + 1, j + 1)[family])) {
                    mb_is_complex(i, j) = true;
                    return;
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

void Bifurcation_CurveTPCW::prepare_cell(int i, int j, int family, Matrix< std::vector<double> > &eigen, Matrix<RealVector> &flux_values, Matrix<RealVector> &accum_values, double *lambda, Matrix<double> &flux, Matrix<double> &accum) {

    //    lambda[0]   = eigen(i, j)[family];
    //    flux(0, 0)  = flux_values(i, j).component(0);
    //    flux(1, 0)  = flux_values(i, j).component(1);
    //    flux(2, 0)  = flux_values(i, j).component(2);
    //    accum(0, 0) = accum_values(i, j).component(0);
    //    accum(1, 0) = accum_values(i, j).component(1);
    //    accum(2, 0) = accum_values(i, j).component(2);

    //    lambda[1] = eigen(i + 1, j)[family];
    //    flux(0, 1) = flux_values(i + 1, j).component(0);
    //    flux(1, 1) = flux_values(i + 1, j).component(1);
    //    flux(2, 1) = flux_values(i + 1, j).component(2);
    //    accum(0, 1) = accum_values(i + 1, j).component(0);
    //    accum(1, 1) = accum_values(i + 1, j).component(1);
    //    accum(2, 1) = accum_values(i + 1, j).component(2);

    //    lambda[2] = eigen(i + 1, j + 1)[family];
    //    flux(0, 2) = flux_values(i + 1, j + 1).component(0);
    //    flux(1, 2) = flux_values(i + 1, j + 1).component(1);
    //    flux(2, 2) = flux_values(i + 1, j + 1).component(2);
    //    accum(0, 2) = accum_values(i + 1, j + 1).component(0);
    //    accum(1, 2) = accum_values(i + 1, j + 1).component(1);
    //    accum(2, 2) = accum_values(i + 1, j + 1).component(2);

    //    lambda[3] = eigen(i, j + 1)[family];
    //    flux(0, 3) = flux_values(i, j + 1).component(0);
    //    flux(1, 3) = flux_values(i, j + 1).component(1);
    //    flux(2, 3) = flux_values(i, j + 1).component(2);
    //    accum(0, 3) = accum_values(i, j + 1).component(0);
    //    accum(1, 3) = accum_values(i, j + 1).component(1);
    //    accum(2, 3) = accum_values(i, j + 1).component(2);

    int domain_i, domain_j;

    for (int kr = 0; kr < 4; kr++) {
        if (kr == 0) {
            domain_i = i;
            domain_j = j;
        } else if (kr == 1) {
            domain_i = i + 1;
            domain_j = j;
        } else if (kr == 2) {
            domain_i = i + 1;
            domain_j = j + 1;
        } else if (kr == 3) {
            domain_i = i;
            domain_j = j + 1;
        }

        lambda[kr] = eigen(domain_i, domain_j)[family];

        flux(0, kr) = flux_values(domain_i, domain_j).component(0);
        flux(1, kr) = flux_values(domain_i, domain_j).component(1);
        flux(2, kr) = flux_values(domain_i, domain_j).component(2);

        accum(0, kr) = accum_values(domain_i, domain_j).component(0);
        accum(1, kr) = accum_values(domain_i, domain_j).component(1);
        accum(2, kr) = accum_values(domain_i, domain_j).component(2);
    }

    return;
}

//
// where_is_characteristic = CHARACTERISTIC_ON_CURVE or CHARACTERISTIC_ON_DOMAIN.
//

bool Bifurcation_CurveTPCW::prepare_segment(int i, int family, int where_is_characteristic,
        const std::vector< std::vector<double> > &eigen,
        const std::vector<RealVector> &reduced_flux_values,
        const std::vector<RealVector> &reduced_accum_values,
        const std::vector<std::vector<bool> > &eig_is_real,
        double *lambda,
        Matrix<double> &reduced_flux,
        Matrix<double> &reduced_accum) {

    if (where_is_characteristic == CHARACTERISTIC_ON_CURVE) {
        if (!eig_is_real[i][family] || !eig_is_real[i + 1][family]) return false;
    }

    // TODO: Verify the normalization of lambdas.

    lambda[0] = eigen[i][family];
    reduced_flux(0, 0) = reduced_flux_values[i].component(0);
    reduced_flux(1, 0) = reduced_flux_values[i].component(1);
    reduced_flux(2, 0) = reduced_flux_values[i].component(2);
    reduced_accum(0, 0) = reduced_accum_values[i].component(0);
    reduced_accum(1, 0) = reduced_accum_values[i].component(1);
    reduced_accum(2, 0) = reduced_accum_values[i].component(2);

    lambda[1] = eigen[i + 1][family];
    reduced_flux(0, 1) = reduced_flux_values[i + 1].component(0);
    reduced_flux(1, 1) = reduced_flux_values[i + 1].component(1);
    reduced_flux(2, 1) = reduced_flux_values[i + 1].component(2);
    reduced_accum(0, 1) = reduced_accum_values[i + 1].component(0);
    reduced_accum(1, 1) = reduced_accum_values[i + 1].component(1);
    reduced_accum(2, 1) = reduced_accum_values[i + 1].component(2);

    return true;
}

void Bifurcation_CurveTPCW::fill_values_on_segments(const Flux2Comp2PhasesAdimensionalized *ff, const Accum2Comp2PhasesAdimensionalized *aa,
        const std::vector<RealVector> &input,
        std::vector<RealVector> &Redvff, std::vector<RealVector> &Redvaa,
        std::vector<std::vector<double> > &Redvee, std::vector< std::vector<bool> > &Redeig_is_real) {
    Redvff.clear();
    Redvaa.clear();
    Redvee.clear();

    int n = input.size();

    // Exit if the curve is formed by a point only (no segments are detected).
    if (n <= 1) return;

    // Proceed otherwise
    int dim = input[0].size();
    int dimP = dim + 1;

    Redvff.resize(n);
    Redvaa.resize(n);
    Redvee.resize(n);
    Redeig_is_real.resize(n);

    double point[dim];
    double pointP[dimP];

    double JF[dimP][dimP], JG[dimP][dimP];
    double RedF[dimP], RedG[dimP];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < dim; j++) {
            point[j] = input[i].component(j);
            pointP[j] = input[i].component(j);
        }
        pointP[dimP - 1] = 1.0;


        Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized*) ff;
        Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) aa;

        // Fill the values of the functions
        fill_with_jet((RpFunction*) ff, dimP, pointP, 1, 0, &JF[0][0], 0);
        fill_with_jet((RpFunction*) aa, dimP, pointP, 1, 0, &JG[0][0], 0);

        fill_with_jet((RpFunction*) fluxFunction->getReducedFlux(), dimP, pointP, 0, RedF, 0, 0);
        fill_with_jet((RpFunction*) accumulationFunction->getReducedAccumulation(), dimP, pointP, 0, RedG, 0, 0);

        Redvff[i].resize(dimP);
        Redvaa[i].resize(dimP);

        for (int j = 0; j < dimP; j++) {
            Redvff[i].component(j) = RedF[j];
            Redvaa[i].component(j) = RedG[j];
        }

        // Find the eigenpairs
        vector<eigenpair> Redetemp;
        Eigen::eig(dimP, &JF[0][0], &JG[0][0], Redetemp);

        Redvee[i].clear();
        Redvee[i].resize(Redetemp.size());

        for (int j = 0; j < Redetemp.size(); j++) Redvee[i][j] = Redetemp[j].r;

        // Decide if the eigenvalues are real or complex
        Redeig_is_real[i].clear();
        Redeig_is_real[i].resize(Redetemp.size());
        for (int j = 0; j < Redetemp.size(); j++) {
            if (fabs(Redetemp[j].i) < epsilon) Redeig_is_real[i][j] = true;
            else Redeig_is_real[i][j] = false;
        }
    }

    return;
}

