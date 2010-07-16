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

ShockContinuationMethod::ShockContinuationMethod(const ODESolver & solver,const Boundary  & boundary, int family ) : solver_(solver.clone()),boundary_(boundary.clone()),family_(family) {

}

// FUNCTION shockinit
//
// This function initializes the shock curve. See Eq. (3.3).
//
// Parameters:
//           n: Dimension of the space
//          Um: Initial point
//      family: The family of the curve, 0 <= family <= (n - 1).
//    increase: 1 if the speed should increase as time advances,
//              0 if the speed should decrease as time advances.
//       Unext: Output, the second point in the curve 
//              (the first point is Um).
//
// Returns:
//     SUCCESSFUL_PROCEDURE: OK, Unext is usable,
//     ABORTED_PROCEDURE: Something went wrong, Unext should not be used.

int ShockContinuationMethod::shockinit(int n, double Um[], int family, int increase, double Unext[]) {
    // Compute the eigencouple at Um:
    // Find the Jacobian of the field at U-:
    double J[n][n];

    // This line was replaced:
    // DF(n, Um, &J[0][0]);

    ContinuationShockFlow & flow = (ContinuationShockFlow &) solver_->getProfile().getFunction();

    const FluxFunction & shock_flux_object = flow.fluxFunction();

    fill_with_jet(shock_flux_object, n, Um, 1, 0, &J[0][0], 0);

    // Find the eigenpairs of J:
     vector<eigencouple> e;
    int info = Eigen::eig(n, &J[0][0], e);
        if (info == ABORTED_PROCEDURE) {
#ifdef TEST_SHOCK
        printf("Inside shockinit(): cdgeev() aborted!\n");
#endif
        return ABORTED_PROCEDURE;
    }

    // STOP CRITERION AT THIS POINT:
    // The i-th eigenvalue must be real.
    if (e[family].i != 0) {
#ifdef TEST_SHOCK
        printf("Inside shockinit(): Init step, eigenvalue %d is complex: % f %+f.\n", family, e[family].r, e[family].i);
#endif
        return ABORTED_PROCEDURE;
    }

    // Extract the indx-th right-eigenvector of J:
    int i;
    double r[n];
    for (i = 0; i < n; i++) r[i] = e[family].vrr[i];
    //for (i = 0; i < n; i++) printf("r(%d) = % f\n", i, r[i]);

    // Set epsilon, see Eq. (2.3).
    double epsilon = 1e-3;

    // Find U+right and U+left.
    double Upr[n], Upl[n];
    for (i = 0; i < n; i++) {
        Upr[i] = Um[i] + epsilon * r[i];
        Upl[i] = Um[i] - epsilon * r[i];
    }

    // Compute the speed at Upr, Upl and Um
    double spr, spl, sm;
    spr = flow.shockspeed(n, family, 1, Um, Upr);
    spl = flow.shockspeed(n, family, 1, Um, Upl);
    sm = flow.shockspeed(n, family, 0, Um, Um);

    // Choose the point according to the increase/decrease of the speed:

    printf("\n\n\n");
    printf("Family: %d, increase = %d\n", family, increase);
    printf("spr = %f, sm = %f, spl = %f\n", spr, sm, spl);
    for (i = 0; i < n; i++) printf("Upr[%d] = %f     Upl[%d] = %f\n", i, Upr[i], i, Upl[i]);

    // Speed should increase
    if (increase == 1) {
        if (spl > sm && sm > spr) {
            for (i = 0; i < n; i++) Unext[i] = Upl[i];
            printf("shock init: 1\n");
        } else if (spl < sm && sm < spr) {
            for (i = 0; i < n; i++) Unext[i] = Upr[i];
            printf("shock init: 2\n");
        } else if ((spl < sm && sm > spr) || (spl > sm && sm < spr)) {
            printf("3\n");
            return ABORTED_PROCEDURE;
        }
    }// Speed should decrease
    else if (increase == -1) {
        if (spl < sm && sm < spr) {
            for (i = 0; i < n; i++) Unext[i] = Upl[i];
            printf("shock init: 4\n");
        } else if (spl > sm && sm > spr) {
            for (i = 0; i < n; i++) Unext[i] = Upr[i];
            printf("shock init: 5\n");
        } else if ((spl < sm && sm > spr) || (spl > sm && sm < spr)) {
            printf("6\n");
            return ABORTED_PROCEDURE;
        }
    } else return ABORTED_PROCEDURE;

    return SUCCESSFUL_PROCEDURE;
}

void ShockContinuationMethod::fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H) {
    RealVector r(n);
    double *rp = r;
    for (int i = 0; i < n; i++) rp[i] = in[i];

    // Will this work? There is a const somewhere in fluxParams.
    //FluxParams fp(r);
    //flux_object->fluxParams(FluxParams(r)); // flux_object->fluxParams(fp);

    WaveState state_c(r);
    JetMatrix c_jet(n);

    flux_object.jet(state_c, c_jet, degree);

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

    output.clear();
    ContinuationShockFlow & flow = (ContinuationShockFlow &) solver_->getProfile().getFunction();
    //
    int indx = flow.getFamily();
    int increase = flow.direction();
    const RealVector & startPoint = flow.getStartPoint();

    // Common index for the loops

    int i;
    int n = input.size();
    RealVector localInputVector(input);

    double in[n];
    double Um[n];
    for (i = 0; i < n; i++) {
        Um[i] = startPoint(i);
        in[i] = localInputVector(i);
    }


    // Initialize the shockcurve

    double Pn[n];
    if (shockinit(n, Um, indx, increase, Pn) == ABORTED_PROCEDURE) {
        printf("Couldn't init shock!\n");
        return; //ABORTED_PROCEDURE;
    }
    //
    printf("After shock init:\n");
    for (int i = 0; i < n; i++) printf("P[%d] = %f\n", i, Pn[i]);
    // Store the first point
    //    double res[n + 1];

    RealVector outputShockPoint(n);
    for (i = 0; i < n; i++) {
        //        res[i] = Pn[i];
        outputShockPoint(i) = Pn[i];
    }
    //
    //    // nowspeed = s(Pn), the speed at Pn.
    double sn = flow.shockspeed(n, indx, 1, in, Pn);
    //    res[n] = sn;
    //    outputShockPoint(n) = sn;
    output.push_back(outputShockPoint);
    printf("Speed at first point: %f\n", sn);

    //--------------------------------------FIM INICIALIZACAO--------------------------------------------------------
    //
    // Parameters to be passed to shock():
    //    int nparam = 1 + 2 * n;
    //    double param[nparam];
    //    //    param[0] = (double) indx; // Family index.
    //    //    for (i = 0; i < n; i++) param[i + 1] = in[i]; // Um, the first point of the curve.
    //
    double dHdu[n];
    //
    int shockinfo = flow.shockfield(n, in, 1, Pn, indx, dHdu);
    //
    //    /* NEW Reference vector */
    double p = 0;
    for (i = 0; i < n; i++) p += (Pn[i] - in[i]) * dHdu[i];
    if (p < 0) for (i = 0; i < n; i++) dHdu[i] = -dHdu[i];
    //
    RealVector newReferenceVector(n);
    //
    for (i = 0; i < n; i++) newReferenceVector(i) = dHdu[i];
    //
    flow.setReferenceVector(newReferenceVector);
    //---------------------------------------FIM DO CALCULO DO NOVO VETOR DE REFERENCIA-------------------------------
    int info = SUCCESSFUL_PROCEDURE;
    //
    //    // Reset numtotal:
    //    //    (*numtotal) = 1;
    //
    //    // Main cicle: compute the curve
    // Store a copy of the speed and of the reference vector
    double oldspeed;
    double oldrefvec[n];
    RealVector outputVector(n);
    RealVector inputVector(n);
    double testeDouble = 0; //TODO Dummy value !!
    int step = 0;
    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {
        oldspeed = sn;
        for (i = 0; i < n; i++) {
            oldrefvec[i] = flow.getReferenceVector()(i); //param[1 + n + i];
            inputVector(i) = Pn[i];
        }
        //        cout << "Input Vector: " << inputVector << endl;
        info = solver_->solve(inputVector, outputVector, testeDouble);
        //        cout << "Output: " << outputVector << endl;
        LSODE::increaseTime();
        if (info == SUCCESSFUL_PROCEDURE) {
            //            cout << "SUCCE" << endl;
            //
            //            // Store a copy of the reference vector, so that it can be compared later with the
            //            // eigenvector at the point returned by the solver and thus prepare
            //            // the reference vector for the next iteration
            for (i = 0; i < n; i++) {
                Pn[i] = outputVector(i);
            }

            //
            double nowspeed = flow.shockspeed(n, indx, 1, in, Pn);
            //            //
            //            printf("Depois do solver nowspeed = %f, oldspeed = %f\n", nowspeed, oldspeed);
            //            //            // Is the speed monotonic?
            if ((increase == 1 && nowspeed < oldspeed) ||
                    (increase == -1 && nowspeed > oldspeed)) {
                printf("nowspeed = %f, oldspeed = %f\n", nowspeed, oldspeed);
                printf("Non-monotonous!\n");
                return; // ABORTED_PROCEDURE;
            }
            sn = nowspeed;
            //         The reference vector
            shockinfo = flow.shockfield(n, in, 1, Pn, indx, dHdu);

            p = 0;

            for (i = 0; i < n; i++) p += oldrefvec[i] * dHdu[i];

            if (p < 0) for (i = 0; i < n; i++) dHdu[i] = -dHdu[i];

            RealVector newReferenceVector(n);

            RealVector outputPoint(n);

            for (i = 0; i < n; i++) {
                newReferenceVector(i) = dHdu[i]; //param[n + i + 1];
                outputPoint(i) = Pn[i];
                //                outputPoint(i) = dHdu[i];
            }
            flow.setReferenceVector(newReferenceVector);

            //            outputPoint(n) = sn;

            output.push_back(outputPoint);

            step++;
            //cout << "Step: " << step << endl;

        } else {
            cout << "ELSE" << endl;
            //                        printf("Inside while, info = %d, numtotal = %d\n", info, *numtotal);
            return; // ABORTED_PROCEDURE;
        }
    }

    return; // SUCCESSFUL_PROCEDURE;
}

ShockContinuationMethod::~ShockContinuationMethod() {

}


