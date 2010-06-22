#include "jetcomposite.h"

// Pointer to FluxFunction, to be used throughout this file.
// When rarefactioncurve is invoked, it will set this pointer
// with the address of an externally declared object of a 
// FluxFunction-derived class.
//
FluxFunction *cmp_flux_object;

// Computes the composite.
// This function receives the following parameters:
//
//        neq: The dimension of the problem, neq == 2*dimension of space.
//         xi: The independent variable. Totally useless, it is passed
//             because of the solver.
//         in: The point at which the solution is found. It is a vector with 2*neq elements:
//
//             in[0:neq/2 - 1] = U-;    in[neq/2:neq - 1] = U+.
//
//        out: A vector where the solution is stored. It is a vector with 2*neq elements.
//             
//             out[0:neq/2 - 1]   = rarefaction part of the composite,
//             out[neq/2:neq - 1] = shock part of the composite,
//             
//     nparam: The number of elements of param (q.v.)
//      param: A vector of nparam components. This vector contains
//             extra information, such as the family, etc., and
//             should avoid the use of global variables. In particular, 
//             for the composite, we have that:
//               
//             nparam                 = 1 + neq;
//             param[0]               = family index.
//             param[1:neq/2]         = reference vector for the rarefaction
//             param[neq/2 + 1:neq/2] = reference vector for the shock
//
// The function returns:
// ABORTED_PROCEDURE: Something went wrong,
// SUCCESSFUL_PROCEDURE: OK.

int composite(int *neq, double *xi, double *in, double *out, int *nparam, double *param) {
    int n = (*neq) / 2;

    // Family
    int family = (int) param[0];

    // Reference vector for the rarefaction
    double rar_ref[n];
    for (int i = 0; i < n; i++) rar_ref[i] = param[1 + i];

    // Reference vector for the shock
    double shk_ref[n];
    for (int i = 0; i < n; i++) shk_ref[i] = param[1 + n + i];

    // A double for inner products
    double prod;

    ////////////////// Compute the directional derivative ///////////////////////
    double um[n];
    for (int i = 0; i < n; i++) um[i] = in[i];
    double Jm[n][n];
    double Hm[n][n][n];
    fill_with_jet(cmp_flux_object, n, um, 2, 0, &Jm[0][0], &Hm[0][0][0]);

    // Extract the left and right eigenvalues of Jm
    vector<eigencouple> e;
    Eigen::eig(n, &Jm[0][0], e);

    double l[n], r[n];

    for (int i = 0; i < n; i++) {
        l[i] = e[family].vlr[i];
        r[i] = e[family].vrr[i];
    }

    // Compute D
    double D[n][n];
    for (int i = 0; i < n; i++) {
        double SubH[n][n];
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                SubH[j][k] = Hm[i][j][k];
            }
        }

        double row[n];
        matrixmult(n, n, 1, &SubH[0][0], &r[0], &row[0]);

        for (int j = 0; j < n; j++) D[i][j] = row[j];
    }

    // Compute D*r
    double Dtimesr[n];
    matrixmult(n, n, 1, &D[0][0], &r[0], &Dtimesr[0]);

    // Result
    double dirdrv = prodint(n, Dtimesr, l) / prodint(n, r, l);
    ////////////////// Compute the directional derivative ///////////////////////

    // The first n-elements of the field are the i-th right-eigenvector of Jm.
    // Check wether r is compatible with the corresponding reference vector or not, act accordingly.
    prod = 0;
    for (int i = 0; i < n; i++) prod += r[i] * rar_ref[i];
    if (prod > 0) for (int i = 0; i < n; i++) out[i] = r[i];
    else for (int i = 0; i < n; i++) out[i] = -r[i];

    // lambda, the i-th eigenvalue due to Um
    double lambda = e[family].r;

    // Solve the system that forms the second part of the field:
    //
    // (J(u+) - lambda(u-)*I)*(du+/dxi) = dirdrv*(u+ - u-)
    //
    // First obtain the matrix:
    double up[n];
    for (int i = 0; i < n; i++) up[i] = in[n + i];
    double A[n][n];
    fill_with_jet(cmp_flux_object, n, up, 1, 0, &A[0][0], 0);

    for (int i = 0; i < n; i++) A[i][i] -= lambda;

    // Then obtain the right-member vector
    double c[n];
    for (int i = 0; i < n; i++) c[i] = dirdrv * (up[i] - um[i]);

    // Now solve the system. The vector needs not be normalized.
    double dup_dxi[n];
    if (cdgesv(n, &A[0][0], c, dup_dxi) == ABORTED_PROCEDURE) return ABORTED_PROCEDURE;

    for (int i = 0; i < n; i++) out[n + i] = dup_dxi[i];

    return SUCCESSFUL_PROCEDURE;
}

// FUNCTION compositecurve
//
// This function computes the compositecurve.
// The list of parameters:
//
//    *numtotal (OUTPUT): The number of points computed before a stop criterion was met.
//     *xifinal (OUTPUT): The value of xi when the computation of the orbit stops.
//         *out (OUTPUT): A pointer of type struct store (see alloc.c, alloc.h), 
//                        to store the results.
//
//                        The user will reserve enough space in out for storing the
//                        results.
//
//                        By columns, the array contains the coordinates of the points
//                        being computed and any other data deemed of interest. 
//                        As the bare minimum, the number of rows of out must be 2*n + 1, since
//                        every point computed must be stored, and the dimension of the
//                        space is n. Plus, the speed = eigenvalue at the point will be stored.
//                        The first n elements correspond to the rarefaction.
//                        The last n elements correspond to the shock.
//                        However, the speed (a scalar), an eigenvector, etc., 
//                        could also be recorded. The user will modify the code accordingly.
//                        Comments of the form
//
//                        /************* DATA STORAGE BEGINS HERE *************/
//
//                        and
//
//                        /*************  DATA STORAGE ENDS HERE  *************/
//
//                        will mark the places where storing of data occurs,
//                        so the user will find such places easily.
//           neq (INPUT): The dimension of the problem (twice the dimension of the space).
//           *in (INPUT): An array of 2*n elements containing the initial point, i.e., U-. 
//                        The first n elements correspond to the rarefaction.
//                        The last n elements correspond to the shock.
//        nummax (INPUT): The maximum number of points that will be computed.
//          indx (INPUT): The "index" of the family. To comply with the C/C++ standard,
//                        indx can be as small as 0 and as great as (n - 1). 
//                        If indx lies outside that range, the function aborts.
//           xi0 (INPUT): The initial value of xi (the independent variable).
//       deltaxi (INPUT): The step for xi (the independent variable). 
//      increase (INPUT): 1 if the speed will increase as the orbit advances, -1 contrariwise. 
//     collision (INPUT): A function that answers wether or not a segment belongs in a domain.
//        domain (INPUT): The domain where the shock curve is being calculated.
//       the_flux_object: A pointer to a FluxFunction-derived object. This object will
//                        perform all the jet-related operations.
//                origin: An integer that indicates wether this curve is the first one of a series or
//                        a continuation of another one.
//         input_ref_vec: An array where the last reference vector of the previous curve is stored,
//                        that will serve to start this curve.
//
// NOTES:
//
// The solution will advance up to 
//
//     xi = xi0 + deltaxi*nummax
//
// if no problems are found during the integration process.
//
// Returns:
//     SUCCESSFUL_PROCEDURE: No errors were detected during the computation
//                           of the orbit.
//     ABORTED_PROCEDURE: Something went wrong.

int compositecurve(int *numtotal,
        double *xifinal,
        struct store *out,
        int neq,
        double *in,
        int nummax,
        int indx,
        double xi0,
        double deltaxi,
        int increase,
        Boundary *domain,
        FluxFunction *the_flux_object,
        int previous,
        double *input_ref_vec,
        double rar_first_lambda,
        double *output_ref_vec) {

    // Set the flux object that will be used by all the jet-dependent functions.
    cmp_flux_object = the_flux_object;

    // The dimension of the space is twice the dimension of the rarefaction, or the shock
    int n = neq / 2;

//    if (previous == INITIAL_CURVE) {
//        // TODO: Initialize the composite curve. In rare occasions the composite will be the
//        // initial curve.
//        // Here the input_ref_vec should be modified, but I may need more than that.
//    }

    // Store the first point of the curve...
    double U[2 * n + 1];
    for (int i = 0; i < n; i++) {
        U[i] = in[i];
        U[i + n] = in[i];
    }

    // ...and the speed (eigenvalue) to it associated
    double lambda, rev[n];
    int info_flux = flux(n, indx, cmp_flux_object, in, &(U[2 * n]), 0); // Declared in jetrarefaction.h é apenas um interface com o lapack. O flux da rarefacao corrige a saida de acordo com um parametro essa implementacao esta em jetrarefaction.cpp
    //U[2*n] = lambda;
    if (info_flux != SUCCESSFUL_PROCEDURE) return info_flux;
    add(out, U);// Equivalente a um push back

    // Now that U and lambda were stored, numtotal is updated:
    (*numtotal) = 1;

    // Add the second point of the curve before LSODE can be invoked:
    // For the rarefaction:
    //
    // U- = U0 - h*input_ref_vec,
    //
    // for the shock:
    //
    // U+ = U0 + 2*h*input_ref_vec,
    //
    // where h is some small value (Julio Daniel).
    double h = 1e-2; // Start with this value due to the experience gained when initializing the shock.
    for (int i = 0; i < n; i++) {
        U[i] = in[i] - h * input_ref_vec[i];
        U[i + n] = in[i] + 2 * h * input_ref_vec[i];
    }

    printf("Composite's second point:\n");
    for (int i = 0; i < 2 * n; i++) {
        printf("U[%d] = %f\n", i, U[i]);
    }

    // Again, find the speed associated with this point, etc.
    double second_point[n];
    for (int i = 0; i < n; i++) second_point[i] = U[i];
    info_flux = flux(n, indx, cmp_flux_object, second_point, &(U[2 * n]), 0);
    //U[2*n] = lambda;
    if (info_flux != SUCCESSFUL_PROCEDURE) return info_flux;
    add(out, U);
    (*numtotal)++;

    //return ABORTED_PROCEDURE;

    // Initialize LSODE (BEGIN)

    // ???
    int ml;
    int mu;

    // ???
    int nrpd = 4;

    // Initial and final times
    double t = xi0;
    double tout = t + deltaxi;

    // Is the tolerance the same for all the elements of U (1) or not (2)?
    int itol = 2; // 1: atol scalar; 2: atol array.
    double rtol = 1e-4;
    double atol[neq];
    for (int i = 0; i < neq; i++) atol[i] = 1e-6;

    // The Jacobian is provided by the user.
    // int mf = 21; 
    // The Jacobian is NOT provided by the user.
    int mf = 22;

    // Lsode uses rwork to perform its computations.
    // lrw is the declared length of rwork
    int lrw;
    if (mf == 10) lrw = 20 + 16 * neq;
    else if (mf == 21 || mf == 22) lrw = 22 + 9 * neq + neq * neq;
    else if (mf == 24 || mf == 25) lrw = 22 + 10 * neq + (2 * ml + mu) * neq;
    double rwork[lrw];

    // Normal computation of values at tout.
    int itask = 1;

    // Set to 1 initially.
    int istate = 1;

    // No optional inputs
    int iopt = 0;

    // Lsode uses iwork to perform its computations.
    // liw is the declared length of iwork
    int liw;
    if (mf == 10) liw = 20;
    else if (mf == 21 || mf == 22 || mf == 24 || mf == 25) liw = 20 + neq;
    int iwork[liw];

    // Lsode returns an integer (that serves as a flag), to tell if the computation
    // process was successful or not. Refer to clsode.c for details.
    int info = SUCCESSFUL_PROCEDURE;

    // Initialize LSODE (END)

    // The parameters are set here (thus avoiding global variables):
    int nparam = 1 + 2 * n;
    double param[nparam];

    // TODO: Should these reference vectors be updated, or they're fine as they're now?
    // Reason: These are the ORIGINAL refvecs, associated to the FIRST point in the curve.
    // With them we computed the SECOND point in the curve. In all purity the refvecs
    // should be computed for the second point as well, but I think *maybe* that would
    // result in no-so-different refvecs, since the first and second points are not
    // extremely far from each other.
    param[0] = indx; // Family
    for (int i = 0; i < n; i++) param[1 + i] = -input_ref_vec[i]; // Rar. ref. vector
    for (int i = 0; i < n; i++) param[1 + n + i] = input_ref_vec[i]; // Shk. ref. vector

    // A vector only for LSODE to work with.
    double Ulsode[2 * n];

    /* NOT ANYMORE */
    // for (int i = 0; i < 2*n; i++) Ulsode[i] = in[i];
    // for (int i = 0; i < 2*n; i++) Ulsode[i] += deltaxi*param[1 + i];
    /* NOT ANYMORE */
    for (int i = 0; i < 2 * n; i++) Ulsode[i] = U[i];

    // The previous point (to be used when computing the reference vector for the rarefaction):
    double U_rar_prev[n];

    // For the collision:
    //
    // Remember that in the case of the composite BOTH the rarefaction and shock parts
    // must be tested.
    // Subindex r = rarefaction, s = shock
//    vector<double> p_r, q_r, r_r;
    //    p_r.resize(n);
    //    q_r.resize(n);
    //    r_r.resize(n);

    RealVector p_r(n);
    RealVector q_r(n);
    RealVector r_r(n);




//    vector<double> p_s, q_s, r_s;

    RealVector p_s(n);
    RealVector q_s(n);
    RealVector r_s(n);




//    p_s.resize(n);
//    q_s.resize(n);
//    r_s.resize(n);

    // The previous point, holding both rarefaction and shock
    double Uprev[2 * n];

    // Compute the curve proper (BEGIN)
    while (info == SUCCESSFUL_PROCEDURE && (*numtotal) < nummax) {
        //        // Store a copy of the reference vector, so that it can be compared later with the
        //        // eigenvector at the point returned by the solver and thus prepare
        //        // the reference vector for the next iteration
        //        double old_rar_refvec[n];
        //        for (int i = 0; i < n; i++) old_rar_refvec[i] = param[1 + i];

        for (int i = 0; i < 2 * n; i++) Uprev[i] = U[i];
        for (int i = 0; i < n; i++) U_rar_prev[i] = U[i];

        // Invoke the solver
        info = solver(&composite, &neq, &Ulsode[0], &t, &tout, &itol, &rtol, &atol[0],
                &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw,
                //&jacrarefaction, &mf, &nparam, &param[0]);
                0, &mf, &nparam, &param[0]);

        if (info == SUCCESSFUL_PROCEDURE) {
            // Generate the next reference vector (and the lambda to be stored here).
            //double rar_ref[n];
            for (int i = 0; i < n; i++) param[1 + i] = Ulsode[i] - U_rar_prev[i];

            double rar_in[n];
            for (int i = 0; i < n; i++) rar_in[i] = Ulsode[i];
            double lambda_u;

            //int rar_info = flux(n, indx, cmp_flux_object, rar_in, &lambda_u, rar_ref);
            int rar_info = flux(n, indx, cmp_flux_object, rar_in, &lambda_u, 0);

            // Prepare the point to be stored
            for (int i = 0; i < 2 * n; i++) U[i] = Ulsode[i];
            U[2 * n] = lambda_u;

            if (fabs(rar_first_lambda - lambda_u) < 10 * deltaxi) {
                add(out, U);
                (*numtotal)++;

                printf("Composite: the speed now matches the speed at the beginning.\n");

                return SUCCESSFUL_PROCEDURE;
                // TODO: Add some stuff here, store the point, etc.
            }

            /* COLLISION DETECTION */
            // All new, using the Domain class

            for (int i = 0; i < n; i++) {
                p_r(i) = Uprev[i];
                q_r(i) = Ulsode[i];

                p_s(i) = Uprev[i + n];
                q_s(i) = Ulsode[i + n];
            }

            // Check if the segment formed by the last two points is inside/outside/across 
            // the domain. If inside, carry on. If outside, abort. If across, find
            // the intersection point, add it to the curve and abort.                
            int cllsn_r = domain->intersection(p_r, q_r, r_r);
            int cllsn_s = domain->intersection(p_s, q_s, r_s);

            if (cllsn_r == -1 || cllsn_s == -1) {
                // Both outside
                return ABORTED_PROCEDURE;
            } else if (cllsn_r == 1 && cllsn_s == 1) {
                // Both inside
                add(out, U);
            } else if (cllsn_r == 0 && cllsn_s == 0) {
                // One inside, one outside: store and get out
                double ppnt[2 * n + 1];

                for (int i = 0; i < n; i++) {
                    ppnt[i] = r_r[i];
                    ppnt[i + n] = r_s[i];
                }
                flux(n, indx, cmp_flux_object, ppnt, &ppnt[2 * n], 0); // Just to find the speed.

                add(out, ppnt);
                (*numtotal)++;
                return ABORTED_PROCEDURE;
            }
            /* COLLISION DETECTION */

            // CHECK MONOTONICITY

            // Update the counter and the time
            (*numtotal)++;
            tout += deltaxi;
        } else {
            printf("Cmp aborted, numtotal = %d\n", (*numtotal));
            return ABORTED_PROCEDURE;
        }
    }
    // Compute the curve proper (END)

    return SUCCESSFUL_PROCEDURE;
}

