#include "Viscous_Profile.h"

const FluxFunction* Viscous_Profile::f = 0;
const AccumulationFunction* Viscous_Profile::a = 0;
Viscosity_Matrix* Viscous_Profile::vmf = 0;

// Find the intesection between two segments: p1-p2 and q1-q2, store the answer in r.
// If there is no intersection, return false (and r is useless), otherwise return true.
//
bool Viscous_Profile::segment_intersection(double *p1, double *p2, double *q1, double *q2, double *r){
//    {
//        printf("    Sign(q1) = %lg\n", (p2[1] - p1[1])*q1[0] + (p1[0] - p2[0])*q1[1] + (p1[1]*p2[0] - p2[1]*p1[0]));
//        printf("    Sign(q2) = %lg\n", (p2[1] - p1[1])*q2[0] + (p1[0] - p2[0])*q2[1] + (p1[1]*p2[0] - p2[1]*p1[0]));
//    }

    double alpha, beta;

    double A[2][2], b[2];
    for (int i = 0; i < 2; i++){
        A[i][0] = p1[i] - p2[i];
        A[i][1] = q2[i] - q1[i];

        b[i]    = q2[i] - p2[i];
    }

    double delta = A[0][0]*A[1][1] - A[0][1]*A[1][0];
    if (fabs(delta) < 1e-10) {
        return false;
    }

    alpha = (b[0]*A[1][1] - b[1]*A[0][1])/delta;
    beta  = (b[1]*A[0][0] - b[0]*A[1][0])/delta;

    for (int i = 0; i < 2; i++) r[i] = .5*(alpha*p1[i] + (1.0 - alpha)*p2[i] + beta*q1[i] + (1.0 - beta)*q2[i]);

//    printf("        alpha = %g, beta = %g, delta = %g\n", alpha, beta, delta);

    return (alpha >= 0.0 && alpha <= 1.0) && (beta >= 0.0 && beta <= 1.0);
}

// TODO: This method can be straightforwardly extended to arbitrary dimension:
// Do so.

void Viscous_Profile::Newton_improvement(const FluxFunction *ff, const AccumulationFunction *aa,
        double sigma, const RealVector &p, RealVector &ref, RealVector &out) {

    out.resize(2);

    // TODO: Improve this epsilon
    double epsilon = 1e-10;
    double anorm;
    double delta_U[2]; // = {10.0, 10.0};

    double U[2];
    for (int i = 0; i < 2; i++) U[i] = p.component(i);
    //cout << "Newton_U: (" << U[0] << ", " << U[1] << ")" << endl;
    // If this function ever comes to be vectorialized, these lines below are COMMON
    // to all points (since they deal with the ref point).
    //
    double c[2];
    double F_ref[2], G_ref[2];
    for (int i=0; i < 2; i++) c[i] = ref.component(i);
    //cout << "Newton_R: (" << c[0] << ", " << c[1] << ")" << endl;
    ff->fill_with_jet(2, c, 0, F_ref, 0, 0);
    aa->fill_with_jet(2, c, 0, G_ref, 0, 0);
//    ff->fill_with_jet(2, ref.components(), 0, F_ref, 0, 0);
//    aa->fill_with_jet(2, ref.components(), 0, G_ref, 0, 0);
    for (int i = 0; i < 2; i++) {
        c[i] = sigma * G_ref[i] - F_ref[i];
    }

    do {
        double F[2], JF[2][2], G[2], JG[2][2];
        ff->fill_with_jet(2, U, 1, F, &JF[0][0], 0);
        aa->fill_with_jet(2, U, 1, G, &JG[0][0], 0);

        double A[2][2];
        double b[2];

        for (int i = 0; i < 2; i++) {
            // The minus sign is incorporated within the parentesis
            b[i] = (F[i] - sigma * G[i]) + c[i];
            //cout << "b[i] = " << b[i] << "   c[i] = " << c[i] << endl;

            for (int j = 0; j < 2; j++) {
                A[i][j] = sigma * JG[i][j] - JF[i][j];
            }
        }

        // Solve
        double det = A[0][0] * A[1][1] - A[0][1] * A[1][0];
        anorm = 0.0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                anorm += A[i][j] * A[i][j];
            }
        }

        // If Newton does not converge, return the original point.
        if (fabs(det) <= (epsilon * anorm)) {
            cout << "Viscous_Profile::Newton does not converges." << endl;
            for (int i = 0; i < 2; i++) out.component(i) = p.component(i);
            return;
        }

        // Protect against zero-division or use LAPACK (will be done so anyway for n >= 3)
        delta_U[0] = (b[0] * A[1][1] - b[1] * A[0][1]) / det;
        delta_U[1] = (A[0][0] * b[1] - A[1][0] * b[0]) / det;

        // Prepare next step:
        for (int i = 0; i < 2; i++) U[i] += delta_U[i];

    } while (delta_U[0] * delta_U[0] + delta_U[1] * delta_U[1] > epsilon * epsilon);

    // Output
    for (int i = 0; i < 2; i++) out.component(i) = U[i];


    return;
}

void Viscous_Profile::critical_points_linearization(const FluxFunction *ff, const AccumulationFunction *aa,
        Viscosity_Matrix *v,
        double speed,  RealVector &cp, RealVector &ref,
        std::vector<eigenpair> &ep) {
    ep.clear();

    RealVector out;

    Newton_improvement(ff, aa, speed, cp, ref, out);

    cp=out;

    

    Matrix<double> JF(2, 2), JG(2, 2);
    ff->fill_with_jet(2, cp.components(), 1, 0, JF.data(), 0);
    aa->fill_with_jet(2, cp.components(), 1, 0, JG.data(), 0);

    // Find the eigenpairs of:
    //
    // [-speed*JG(cp[i]) + JF(cp[i])]*U_mu = mu*D(cp[i])*U_mu.
    //
    Matrix<double> RH(2, 2), viscous(2, 2);
    for (int k = 0; k < 2; k++) {
        for (int j = 0; j < 2; j++) {
            RH(k, j) = -speed * JG(k, j) + JF(k, j);
        }
    }

    // Fill the viscous matrix
    //v->fill_viscous_matrix(cp, viscous);
    v->fill_viscous_matrix(cp, viscous);

    //std::vector<eigenpair> e;
    Eigen::eig(2, RH.data(), viscous.data(), ep);

    //cout << "Ponto no metodo: " << cp << endl;

    //cout << "eigen0RR: " << ep[0].r << endl;
    //cout << "eigen1RR: " << ep[1].r << endl;

    //ep.push_back(e);

    return;
}



int Viscous_Profile::orbit(const FluxFunction *ff, const AccumulationFunction *aa,
        Viscosity_Matrix *v,
        const Boundary *boundary, const RealVector &init, const RealVector &ref, double speed,
        double deltaxi,
        int orbit_direction,
        std::vector<RealVector> &out, 
        std::vector<RealVector> *segment){
    f = ff;
    a = aa;
    vmf = v;

    out.clear();



    // The vector of parameters holds 6 elements:
    //
    // param[0]   = sigma (speed)
    // param[1:2] = F(Uref)
    // param[3:4] = G(Uref)
    // param[5]   = ORBIT_FORWARD or ORBIT_BACKWARD
    //
    // It will remain invariable through the following computations.
    //
    int nparam = 6;
    double param[nparam];

    double Fref[2], Gref[2];
    RealVector tref(ref);
    f->fill_with_jet(2, tref.components(), 0, Fref, 0, 0);
    a->fill_with_jet(2, tref.components(), 0, Gref, 0, 0);

    param[0] = speed;
    for (int i = 0; i < 2; i++) {
        param[1 + i] = Fref[i];
        param[3 + i] = Gref[i];
    }

    if (orbit_direction == ORBIT_FORWARD) param[5] = 1.0;
    else param[5] = -1.0;

    // BEGIN Prepare the parameters to be passed to LSODE //
    int n = 2;

    int ml; // Not used.
    int mu; // Not used.

    // ???
    int nrpd = 4;

    // Is the tolerance the same for all the elements of U (1) or not (2)?
    int itol = 2; // 1: atol scalar; 2: atol array.
    double rtol = 1e-4;
    double atol[n];
    for (int i = 0; i < n; i++) atol[i] = 1e-6;

    // The Jacobian is provided by the user.
    // int mf = 21;
    // The Jacobian is NOT provided by the user.
    int mf = 22;
    // Lsode uses rwork to perform its computations.
    // lrw is the declared length of rwork
    int lrw;
    if (mf == 10) lrw = 20 + 16 * n;
    else if (mf == 21 || mf == 22) lrw = 22 + 9 * n + n * n;
    else if (mf == 24 || mf == 25) lrw = 22 + 10 * n + (2 * ml + mu) * n;
    double rwork[lrw];

    // Normal computation of values at tout.
    int itask = 1;

    // Set to 1 initially.
    // This is where LSODE's info parameter. Must be set to 1 the first time.
    int istate = 1;
    // No optional inputs
    int iopt = 0;

    // Lsode uses iwork to perform its computations.
    // liw is the declared length of iwork
    int liw;
    if (mf == 10) liw = 20;
    else if (mf == 21 || mf == 22 || mf == 24 || mf == 25) liw = 20 + n;
    int iwork[liw];
    // END   Prepare the parameters to be passed to LSODE //

    // Current point
    RealVector new_point(2), previous_point(2);

    double p[2];
    for (int i = 0; i < 2; i++) p[i] = new_point.component(i) = init.component(i);
    double xi = 0.0, new_xi = deltaxi;

    out.push_back(new_point);

    // Distance between the last and before-the-last points in the orbit, when
    // it is too small, end the calculations.
    //
    double largest_distance = 0.0;

    // Find the orbit
    while (true) {
        // TEMPORAL
        if (out.size() > 5000) {
            printf("Max reached!!!\n");
            return ABORTED_PROCEDURE;
        }
        // TEMPORAL

        for (int i = 0; i < n; i++) previous_point.component(i) = new_point.component(i);

        lsode_(&orbit_flux, &n, p, &xi, &new_xi, &itol, &rtol, atol, &itask, &istate, &iopt, rwork, &lrw, iwork, &liw, 0, &mf, &nparam, param);

        // Update new_point.
        for (int i = 0; i < n; i++) new_point.component(i) = p[i];

        // BEGIN Verify that the orbit does indeed advance, and does not stay at the same point
        {
            double d = distance(new_point, previous_point);
            if (largest_distance < d) largest_distance = d; //printf("d = %g, largest = %g\n", d, largest_distance);

            if (out.size() > 5 && d < 0.01*largest_distance){
                return ORBIT_STAGNANT;
            }
        }
        // END   Verify that the orbit does indeed advance, and does not stay at the same point

        // BEGIN Verify that the orbit does not intersect the segment (if given)
        if (segment != 0){
            RealVector r(2);
            if (segment_intersection(segment->at(0).components(), 
                                     segment->at(1).components(), 
                                     previous_point.components(), 
                                     new_point.components(), 
                                     r.components())){
                out.push_back(r);

               // printf("Reached segment\n");
                return ORBIT_REACHED_SEGMENT;
            }
            else {
//               // printf("(%lg, %lg)-(%lg, %lg) not intesecting (%lg, %lg)-(%lg, %lg)\n",
//                         previous_point.component(0), previous_point.component(1),
//                         new_point.component(0), new_point.component(1),
//                         segment->at(0).component(0), segment->at(0).component(1),
//                         segment->at(1).component(0), segment->at(1).component(1));
            }
        }
        // END   Verify that the orbit does not intersect the segment (if given)

        // BEGIN Check Boundary //
        // Modified RectBoundary so that the intersection can be tested using RealVectors of size
        // greater than the dimension of the space the RectBoundary is in.
        int where_out;
        RealVector r;
        int intersection_info = boundary->intersection(previous_point, new_point, r, where_out);

        if (intersection_info == 1) {
            // Both points inside. Carry on with the rest of the tests, etc.
            out.push_back(new_point);
        } else if (intersection_info == 0) {
            // One point is inside, the other is outside.
            // Store the point lying in the domain's border and get out.
//            out.push_back(r);

            printf("Reached boundary\n");

            return SUCCESSFUL_PROCEDURE;
        } else {
            // Both points lie outside the domain. Something went awfully wrong here.
            printf("Both outside\n");
            printf("previous_point = (");
            for (int i = 0; i < n; i++) {
                printf("%g", previous_point.component(i));
                if (i < n - 1) printf(", ");
            }
            printf(")\n");

            printf("new_point      = (");
            for (int i = 0; i < n; i++) {
                printf("%g", new_point.component(i));
                if (i < n - 1) printf(", ");
            }
            printf(")\n");

            return ABORTED_PROCEDURE;
        }
        // END   Check Boundary //

        // Update the independent parameters.
        xi = new_xi;
        new_xi += deltaxi;
    }
}

// For LSODE
//
// nparam = 1 + 2 + 2 + 1
// param = [sigma; Fref; Gref; orbit_direction]
//

int Viscous_Profile::orbit_flux(int *neq, double *xi, double *in, double *out, int *nparam, double *param) {
    // Compute the viscous matrix for the current point and invert it
    RealVector p(2, in);
    Matrix<double> vm(2, 2);
    vmf->fill_viscous_matrix(p, vm);

    double inv_det = 1.0 / (vm(0, 0) * vm(1, 1) - vm(0, 1) * vm(1, 0));

    Matrix<double> inv_D(2, 2);
    inv_D(0, 0) = vm(1, 1) * inv_det;
    inv_D(0, 1) = -vm(0, 1) * inv_det;
    inv_D(1, 0) = -vm(1, 0) * inv_det;
    inv_D(1, 1) = vm(0, 0) * inv_det;

    // Fill some stuff
    //
    double F[2], G[2];
    f->fill_with_jet(2, in, 0, F, 0, 0);
    a->fill_with_jet(2, in, 0, G, 0, 0);

    // Sigma
    double sigma = param[0];

    // Fref & Gref
    double Fref[2], Gref[2];
    for (int i = 0; i < 2; i++) {
        Fref[i] = param[1 + i];
        Gref[i] = param[3 + i];
    }

    double direction = param[5];

    // The field proper
    for (int i = 0; i < 2; i++) {
        out[i] = 0.0;
        for (int j = 0; j < 2; j++) out[i] += direction * inv_D(i, j)*(-sigma * (G[j] - Gref[j]) + (F[j] - Fref[j]));
    }

    return SUCCESSFUL_PROCEDURE;
}

// Compute the field on a rectangular domain.
//
//     grid: Array that holds the vertices of the grid.
//     dir:  Array that holds the field in each element of the grid.
//
// To plot the field:
//
//     for (int i = 0; i < grid.size(); i++) line(grid[i], grid[i] + dir[i]);
//
// or
//
//     for (int i = 0; i < grid.size(); i++) line(grid[i] - dir[i], grid[i] + dir[i]);
//

void Viscous_Profile::viscous_field(const FluxFunction *f, const AccumulationFunction *a,
        RealVector &ref, double speed,
        const RealVector &pmin, const RealVector &pmax,
        const std::vector<int> &noc,
        std::vector<RealVector> &grid, std::vector<RealVector> &dir) {

    int n = noc.size();
    grid.clear();
    dir.clear();

    int nparam = 6;
    double param[nparam];

    double Fref[2], Gref[2];
    f->fill_with_jet(2, ref.components(), 0, Fref, 0, 0);
    a->fill_with_jet(2, ref.components(), 0, Gref, 0, 0);

    param[0] = speed;
    for (int i = 0; i < 2; i++) {
        param[1 + i] = Fref[i];
        param[3 + i] = Gref[i];
    }

    // Always growing
    param[5] = 1.0;

    double delta[2];
    for (int i = 0; i < n; i++) delta[i] = (pmax.component(i) - pmin.component(i)) / ((double) noc[i] - 1.0);

    RealVector p(n);

    for (int i = 0; i < noc[0]; i++) {
        p.component(0) = pmin.component(0) + (double) i * delta[0];
        for (int j = 0; j < noc[1]; j++) {
            p.component(1) = pmin.component(1) + (double) j * delta[1];
            grid.push_back(p);

            double xi = 0.0;
            RealVector field(2);
            orbit_flux(&n, &xi, p.components(), field.components(), &nparam, param);

            dir.push_back(field);
        }
    }

    return;
}
