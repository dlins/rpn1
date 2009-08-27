/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockContinuationMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ShockContinuationMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




ShockContinuationMethod::ShockContinuationMethod(const ODESolver & solver) : solver_(solver.clone()) {

}


// FUNCTION shockcurve
//
// This function computes the shockcurve.
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
//                        As the bare minimum, the number of rows of out must be n, since
//                        every point computed must be stored, and the dimension of the
//                        space is n. However, the speed (a scalar), an eigenvector, etc., 
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
//             n (INPUT): The dimension of the problem.
//           *in (INPUT): An array of n elements containing the initial point, i.e., U-.
//        nummax (INPUT): The maximum number of points that will be computed.
//          indx (INPUT): The "index" of the family. To comply with the C/C++ standard,
//                        indx can be as small as 0 and as great as (n - 1). 
//                        If indx lies outside that range, the function aborts.
//           xi0 (INPUT): The initial value of xi (the independent variable).
//       deltaxi (INPUT): The step for xi (the independent variable). 
//      increase (INPUT): 1 if the speed will increase as the orbit advances, -1 contrariwise. 
//     collision (INPUT): A function that answers wether or not a segment belongs in a domain.
//        domain (INPUT): The domain where the shock curve is being calculated.
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

void ShockContinuationMethod::curve(const RealVector & input, int direction, vector<RealVector> & output) {

    //(int *numtotal,
    //        double *xifinal,
    //        struct store *out,
    //        int n,
    //        double *in,
    //        int nummax,
    //        int indx,
    //        double xi0,
    //        double deltaxi,
    //        int increase,
    //        const double domain[]) {

    // Set the flux object


    ContinuationShockFlow & flow = (ContinuationShockFlow &) solver_->getProfile().getFunction();

    int indx = flow.getFamily();
    int increase = flow.direction();

    // Common index for the loops
    int i;
    int n = input.size();
    RealVector localInputVector(input);
    double in[n];
    for (i = 0; i < n; i++) {
        in[i] = input(i);
    }


    // Initialize the shockcurve
    double Pn[n];
    if (shockinit(n, in, indx, increase, Pn) == ABORTED_PROCEDURE) {
        printf("Couldn't init shock!\n");
        return; //ABORTED_PROCEDURE;
    }

    printf("After shock init:\n");
    for (int i = 0; i < n; i++) printf("P[%d] = %f\n", i, Pn[i]);

    // Store the first point
    //    double res[n + 1];

    RealVector outputShockPoint(n + 1);
    for (i = 0; i < n; i++) {
        //        res[i] = Pn[i];
        outputShockPoint(i) = Pn[i];
    }

    // sn = s(Pn), the speed at Pn.
    double sn = flow.shockspeed(n, indx, 1, in, Pn);
    //    res[n] = sn;
    outputShockPoint(n) = sn;
    output.push_back(outputShockPoint);
    //    add(out, res);
    printf("Speed at first point: %f\n", sn);

    // Parameters to be passed to shock():
    int nparam = 1 + 2 * n;
    double param[nparam];
    param[0] = (double) indx; // Family index.
    for (i = 0; i < n; i++) param[i + 1] = in[i]; // Um, the first point of the curve.

    double dHdu[n];
    int shockinfo = flow.shockfield(n, in, 1, Pn, indx, dHdu);



    /* NEW Reference vector */
    double p = 0;
    for (i = 0; i < n; i++) p += (Pn[i] - in[i]) * dHdu[i];
    if (p > 0) {
        for (i = 0; i < n; i++) param[n + i + 1] = dHdu[i];
    } else {
        for (i = 0; i < n; i++) param[n + i + 1] = -dHdu[i];
    }
    /* NEW Reference vector */


    //
    //    // Start the integration
    //    // FOR THE SOLVER:
    //
    //    // The dimension of the space
    //    int neq = n;
    //
    //    // ???
    //    int ml;
    //    int mu;
    //
    //    // ???
    //    int nrpd = 4;

    // Initial and final times
    //    double t = xi0;
    //    double tout = t + deltaxi;
    //printf("Before while: t = %f, tout = %f, xi0 = %f\n", t, tout, xi0);

    // Is the tolerance the same for all the elements of U (1) or not (2)?
    //    int itol = 2; // 1: atol scalar; 2: atol array.
    //    double rtol = 1e-4;
    //    double atol[neq];
    //    for (i = 0; i < neq; i++) atol[i] = 1e-6;

    // The Jacobian is provided by the user.
    // int mf = 21; 
    // The Jacobian is NOT provided by the user.
    //    int mf = 22;

    // Lsode uses rwork to perform its computations.
    // lrw is the declared length of rwork
    //    int lrw;
    //    if (mf == 10) lrw = 20 + 16 * neq;
    //    else if (mf == 21 || mf == 22) lrw = 22 + 9 * neq + neq * neq;
    //    else if (mf == 24 || mf == 25) lrw = 22 + 10 * neq + (2 * ml + mu) * neq;
    //    double rwork[lrw];

    // Normal computation of values at tout.
    //    int itask = 1;

    // Set to 1 initially.+
    //    int istate = 1;

    // No optional inputs
    //    int iopt = 0;

    // Lsode uses iwork to perform its computations.
    // liw is the declared length of iwork
    //    int liw;
    //    if (mf == 10) liw = 20;
    //    else if (mf == 21 || mf == 22 || mf == 24 || mf == 25) liw = 20 + neq;
    //    int iwork[liw];

    // Lsode returns an integer (that serves as a flag), to tell if the computation
    // process was successful or not. Refer to clsode.c for details.
    int info = SUCCESSFUL_PROCEDURE;

    // Reset numtotal:
    //    (*numtotal) = 1;

    // Main cicle: compute the curve
    // Store a copy of the speed and of the reference vector
    double oldspeed;
    double oldrefvec[n];
    RealVector outputVector(input.size());
    double testeDouble = 0; //TODO Dummy value !!
    int step = 0;

    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {

        // Store a copy of the reference vector, so that it can be compared later with the
        // eigenvector at the point returned by the solver and thus prepare
        // the reference vector for the next iteration
        oldspeed = sn;
        for (i = 0; i < n; i++) oldrefvec[i] = param[1 + n + i];

        // Invoke the solver
        //printf("Before solver: tout = %lf, numtotal = %d\n", tout, *numtotal);
        info = solver_->solve(localInputVector, outputVector, testeDouble);

        //        info = solver(&shock, &neq, &Pn[0], &t, &tout, &itol, &rtol, &atol[0],
        //                &itask, &istate, &iopt, &rwork[0], &lrw, &iwork[0], &liw,
        //                //&jacrarefaction, &mf, &nparam, &param[0]);
        //                0, &mf, &nparam, &param[0]);

        //        tout += deltaxi;
        //printf("After solver: tout = %lf, numtotal = %d, info = %d\n", tout, *numtotal, info);

        if (info == SUCCESSFUL_PROCEDURE) {

            // Is the speed monotonic?
            double nowspeed = flow.shockspeed(n, indx, 1, in, Pn);
            printf("nowspeed = %f, oldspeed = %f\n", nowspeed, oldspeed);
            if ((increase == 1 && nowspeed < oldspeed) ||
                    (increase == -1 && nowspeed > oldspeed)) {
                //printf("nowspeed = %f, oldspeed = %f\n", nowspeed, oldspeed);
                printf("Non-monotonous!\n");
                return; // ABORTED_PROCEDURE;
            }



            //            /* COLLISION DETECTION */
            //            // Retrieve the previous U
            //            double Uprev[3];
            //            get(out, Uprev, out->count - 1);
            //
            //            // Check if the segment formed by the last two points is inside/outside/across 
            //            // the domain. If inside, carry on. If outside, abort. If across, find
            //            // the intersection point, add it to the curve and abort.                
            //            double seg[4] = {Uprev[0], Uprev[1], Pn[0], Pn[1]};
            //            double pnt[2];
            //
            //            int cllsn = collision(domain, seg, pnt);
            //            if (cllsn == -1) {
            //                return ABORTED_PROCEDURE;
            //            } else if (cllsn == 0) {
            //                // INSIDE:
            //                for (i = 0; i < n; i++) res[i] = Pn[i];
            //                res[n] = sn = nowspeed;
            //                add(out, res);
            //            } else if (cllsn == 1) {
            //                // Store and get out
            //                double ppnt[3] = {pnt[0], pnt[1], nowspeed};
            //                add(out, ppnt);
            //                return ABORTED_PROCEDURE;
            //            }
            //            /* COLLISION DETECTION */



            /*
            // Effectively add the point
             *
             *
            for (i = 0; i < n; i++) res[i] = Pn[i];
            res[n] = sn = nowspeed;
            add(out, res);*/



            // The reference vector
            //double dHdu[n];
            shockinfo = flow.shockfield(n, in, 1, Pn, indx, dHdu);
            p = 0;
            for (i = 0; i < n; i++) p += oldrefvec[i] * dHdu[i];
            if (p > 0) {
                for (i = 0; i < n; i++) param[n + i + 1] = dHdu[i];
            } else {
                for (i = 0; i < n; i++) param[n + i + 1] = -dHdu[i];
            }

            // Update counter
            //            (*numtotal)++;

            output.push_back(outputVector);

            localInputVector = outputVector;

            step++;

            LSODE::increaseTime();

        } else {
            //            printf("Inside while, info = %d, numtotal = %d\n", info, *numtotal);
            return; // ABORTED_PROCEDURE;
        }
    }

    return; // SUCCESSFUL_PROCEDURE;
}

ShockContinuationMethod::~ShockContinuationMethod() {
}







//! Code comes here! daniel@impa.br

