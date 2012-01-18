#include "Hugoniot_Curve.h"

// TODO: Move or blend fill_with_jet as a method or methods of JetMatrix.
//

void Hugoniot_Curve::fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree,
        double *F, double *J, double *H) {
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

void Hugoniot_Curve::set_reference_point(const RealVector &ref) {
    Uref.resize(2);

    for (int i = 0; i < 2; i++) Uref.component(i) = ref.component(i);

    fill_with_jet(ff, 2, Uref.components(), 1, Fref, JFref, 0);
    fill_with_jet(aa, 2, Uref.components(), 1, Gref, JGref, 0);

    return;
}

Hugoniot_Curve::Hugoniot_Curve(const FluxFunction *f, const AccumulationFunction *a,
        Boundary *b,

        const RealVector &min, const RealVector &max,
        const int *cells,
        const RealVector &ref) {

    ff = f;
    aa = a;


    boundary = b;

    pmin.resize(2);
    pmax.resize(2);


    Fref = new double[2];
    Gref = new double[2];
    JFref = new double[4];
    JGref = new double[4];

    set_reference_point(ref);

    pmin.resize(2);
    pmax.resize(2);

    // Create the grid...
    //
    number_of_cells = new int[2];
    for (int i = 0; i < 2; i++) {
        number_of_cells[i] = cells[i];
        pmin.component(i) = min.component(i);
        pmax.component(i) = max.component(i);
    }

    int dim = 2;

    double delta[2];
    for (int i = 0; i < pmin.size(); i++) delta[i] = (fabs(pmax.component(i) - pmin.component(i))) / (double) (number_of_cells[i]);

    grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    F_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);
    G_on_grid.resize(number_of_cells[0] + 1, number_of_cells[1] + 1);

    for (int i = 0; i <= number_of_cells[0]; i++) {
        for (int j = 0; j <= number_of_cells[1]; j++) {
            grid(i, j).resize(dim);
            F_on_grid(i, j).resize(dim);
            G_on_grid(i, j).resize(dim);

            grid(i, j).component(0) = pmin.component(0) + (double) i * delta[0];
            grid(i, j).component(1) = pmin.component(1) + (double) j * delta[1];
        }
    }

    // ...and fill several values on the grid.
    //
    hc_fill_values_on_grid();
}

int Hugoniot_Curve::classified_curve(std::vector<HugoniotPolyLine> &hugoniot_curve) {

    // Compute the Hugoniot curve as usual
    //
    vector<RealVector> vrs;
    int info = curve(vrs);

    // Prepare the Hugoniot curve to classify it
    vector<vector<RealVector> > unclassifiedCurve;

    for (int i = 0; i < vrs.size() / 2; i++) {
        vector< RealVector> temp;
        temp.push_back(vrs[2 * i]);
        temp.push_back(vrs[2 * i]);
        temp.push_back(vrs[2 * i + 1]);
        //temp.push_back(vrs[2 * i + 1]);
        unclassifiedCurve.push_back(temp);
    }

    ColorCurve colorCurve(*ff, *aa);
    colorCurve.classify_curve(unclassifiedCurve, Uref, 2, 11, hugoniot_curve);

    return info;

}

Hugoniot_Curve::~Hugoniot_Curve() {
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
//           pmin, pmax: Extremes of the domain. Ideally, 
//
//                             pmin[i] <= pmax[i] for 0 <= i < dimension of space.
//
//                       In practice, this will be checked out within the body of the function.
//               ff, aa: Flux and accumulationdouble *Fref, *Gref; functions that apply from R^n to R^n.
//  number_of_grid_pnts: The number of cells in each dimension (array defined externally).
//                 grid: The spatial grid created. Space to hold must be reserved outside the function.
//                       The i-th dimension will have number_of_cells[i] cells. Thus, for 2D, which is
//                       the case that is being implemented, each vertex (i, j) will be of the form:
//
//                              grid[i*number_of_grid_pnts[1] + j].component(k)
//                                           =   pmin[k] + j*(pmax[k] - pmin[k])/(number_of_grid_pnts[k])
//
//                       where:
//
//                              0 <= i < number_of_grid_pnts[0],
//                              0 <= j < number_of_grid_pnts[1],
//                              0 <= k < 2.
//
//                       Thus, grid needs to be of size 
//
//                              number_of_grid_pnts[0]*...*number_of_grid_pnts[pmax.size() - 1].
//
//                   dd: Array of vectors that will hold the value of the directional derivatives
//                       at each vertex of the grid. This array, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//                      
//                    e: Array of vectors of eigenpairs that will hold all the eigenpairs at each
//                       vertex of the grid. These arrays, like the grid, must be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//
//          eig_is_real: Array of vectors of booleans that state if each eigenvalue at a given grid
//                       vertex is real (true) or complex (false). These arrays, like the grid, must
//                       be of size
//
//                              number_of_cells[0]*...*number_of_cells[pmax.size() - 1].
//

// TODO: Change indices i, j to k, l. i & j are reserved for grid- or cell-like uses.

void Hugoniot_Curve::hc_fill_values_on_grid(void) {

    // Dimension of space
    int dim = pmin.size();

    // Number of elements in the grid.
    int n = 1;
    for (int i = 0; i < dim; i++) n *= number_of_cells[i] + 1;

    // Fill the arrays with the value of the flux and accumulation functions at every point in the grid.
    double point[dim], F[dim], G[dim];


    for (int i = 0; i < n; i++) {

        // We only compute the value of the function on points that are inside the physical domain
        // given by "boundary".
        for (int j = 0; j < dim; j++) point[j] = grid(i).component(j);
        if (boundary->inside(point)) {
            fill_with_jet((RpFunction*) ff, dim, point, 0, F, 0, 0);
            fill_with_jet((RpFunction*) aa, dim, point, 0, G, 0, 0);


            for (int j = 0; j < dim; j++) {
                F_on_grid(i).component(j) = F[j];
                G_on_grid(i).component(j) = G[j];
            }
        }
    }



    return;
}

/**
 *  Inside the calculus of the RH condition, when the differences of the accumulation are small, it is
 *  better to approximate the flux differences by the accumulation. Notice that epsilon must be small,
 *  however large enough!
 *     The approximation is done in order to change [ df1 = f1(i,j) - f1(Uref) ] with the aid of the 
 *  approximation :
 *                     dF         dF       dU         dF      [ dG ]^(-1)
 *                    ----   =   ----  *  ----   =   ----  *  [----]
 *                     dG         dU       dG         dU      [ dU ]
 *  Here the inverse matrix [dG/dU]^(-1) is calculated in the standard form insde the [else] part.
 **/

int Hugoniot_Curve::function_on_square(double *foncub, int i, int j, int is_square) {
    double dg1, dg2;
    double f_aux[4];
    double epsilon = 1.0e-5;

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            dg1 = G_on_grid(i + l, j + k).component(0) - Gref[0];
            dg2 = G_on_grid(i + l, j + k).component(1) - Gref[1];

            if (fabs(dg1) + fabs(dg2) >= epsilon) {
                double df1 = F_on_grid(i + l, j + k).component(0) - Fref[0];
                double df2 = F_on_grid(i + l, j + k).component(1) - Fref[1];

                f_aux[l * 2 + k] = dg2 * df1 - dg1*df2;
            } else {
                // First-order expansion of F in terms of G.
                //
                // printf("Hugoniot_Curve::function_on_square(): if epsilon.\n");

                double inv_det = 1.0 / (JGref[0] * JGref[3] - JGref[1] * JGref[2]);

                f_aux[l * 2 + k] = ((JFref[0] * JGref[3] - JFref[2] * JGref[1] + JFref[1] * JGref[2] - JFref[3] * JGref[0]) * dg1 * dg2 +
                        (JFref[1] * JGref[3] - JFref[3] * JGref[1]) * dg2 * dg2 +
                        (JFref[0] * JGref[2] - JFref[2] * JGref[0]) * dg1 * dg1) * inv_det;
            }
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]

    // Only useful if the cell is a square. Otherwise this information
    // will be discarded by contour2d.
    //
    foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

int Hugoniot_Curve::curve(std::vector<RealVector> &hugoniot_curve) {
    hugoniot_curve.clear();

    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);


    int info = ContourMethod::contour2d(this, boundary, rect, number_of_cells, hugoniot_curve);


    return info;
}

void Hugoniot_Curve::map(const RealVector &p, double &f, RealVector &map_Jacobian) {
    int n = p.size();
    map_Jacobian.resize(n);

    double F[n], JF[n][n], G[n], JG[n][n];

    double pd[n];
    for (int i = 0; i < n; i++) pd[i] = p.component(i);

    fill_with_jet(ff, n, pd, 1, F, &JF[0][0], 0);
    fill_with_jet(aa, n, pd, 1, G, &JG[0][0], 0);

    double dg1 = G[0] - Gref[0];
    double dg2 = G[1] - Gref[1];

    // TODO: The following commented lines can be uncommented in case of the Newton_Improvement do
    //       a division by zero!
    //    double epsilon = 1.0e-3;
    //    if (fabs(dg1) + fabs(dg2) >= epsilon){
    double df1 = F[0] - Fref[0];
    double df2 = F[1] - Fref[1];

    f = dg2 * df1 - dg1*df2;

    map_Jacobian.component(0) = JF[0][0] * dg2 + JG[1][0] * df1 - JF[1][0] * dg1 - JG[0][0] * df2;
    map_Jacobian.component(1) = JF[0][1] * dg2 + JG[1][1] * df1 - JF[1][1] * dg1 - JG[0][1] * df2;
    //    }
    //    else {
    //        // First-order expansion of F in terms of G.
    //        //

    //        double det1 = (JFref[0]*JGref[3] - JFref[2]*JGref[1] + JFref[1]*JGref[2] - JFref[3]*JGref[0]);
    //        double det2 = (JFref[1]*JGref[3] - JFref[3]*JGref[1]);
    //        double det3 = (JFref[0]*JGref[2] - JFref[2]*JGref[0]);

    //        f = det1*dg1*dg2 + det2*dg2*dg2 + det3*dg1*dg1;

    //        map_Jacobian.component(0) = det1*(JG[0][0]*dg2 + JG[1][0]*dg1) + 2*det2*JG[0][0]*dg1 + 2*det3*JG[1][0]*dg2;
    //        map_Jacobian.component(1) = det1*(JG[0][1]*dg2 + JG[1][1]*dg1) + 2*det2*JG[0][1]*dg1 + 2*det3*JG[1][1]*dg2;
    //    }

    return;
}

bool Hugoniot_Curve::improvable(void) {
    return true;
}

