/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionContinuationMethod.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <vector>
#include "RarefactionContinuationMethod.h"
#include "LSODEProfile.h"
#include "LSODE.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


RarefactionContinuationMethod::RarefactionContinuationMethod(const RarefactionContinuationMethod & copy) : solver_(copy.getSolver().clone()) {
}

RarefactionContinuationMethod::RarefactionContinuationMethod(const ODESolver & solver) : solver_(solver.clone()) {
}

const ODESolver & RarefactionContinuationMethod::getSolver()const {
    return *solver_;
}

RarefactionContinuationMethod::~RarefactionContinuationMethod() {
}

int RarefactionContinuationMethod::init(ContinuationRarefactionFlow * flow, int n, double *in, int indx, int increase, double deltaxi, double *lambda, double *rev)const {

    // 1. Find the eigenvalue and the eigenvector at in (the initial point):
    if (flow->flux(n, indx, in, lambda, rev) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    // 2. and 3. Find the eigencouples at in_plus and in_minus.
    double epsilon = 10 * deltaxi;
    double lambdap, lambdam; // Lambda_plus, lambda_minus
    double inp[n], inm[n]; // In_plus and in_minus
    double rep[n], rem[n]; // Eigenvectors at in_plus and in_minus
    int ii;
    for (ii = 0; ii < n; ii++) {
        inp[ii] = in[ii] + epsilon * rev[ii];
        inm[ii] = in[ii] - epsilon * rev[ii];
    }
    if (flow->flux(n, indx, inp, &lambdap, &rep[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    if (flow->flux(n, indx, inm, &lambdam, &rem[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;

    //    printf("@ rarefactioncurve(), after init.\nl- = % f, l = % f, l+ = % f\n", lambdam, *lambda, lambdap);
    //    printf("e = (% f, % f), ", rev[0], rev[1]);

    // 4. Find the reference eigenvector.
    if (increase == 1) { // Eigenvalues should increase as the orbit advances
        if (*lambda <= lambdap && *lambda <= lambdam) {
            return LAMBDA_NOT_INCREASING;
        } else if (*lambda < lambdap && *lambda > lambdam) {
            // Nothing to do, the eigenvector is rev.
        } else if (*lambda > lambdap && *lambda < lambdam) {
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        } else {
            return LAMBDA_NOT_INCREASING;
        }
    } else { // Eigenvalues should decrease as the orbit advances
        if (*lambda >= lambdap && *lambda >= lambdam) {
            return LAMBDA_NOT_DECREASING;
        } else if (*lambda > lambdap && *lambda < lambdam) {
            // Nothing to do, the eigenvector is rev.
        } else if (*lambda < lambdap && *lambda > lambdam) {
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        } else {
            return LAMBDA_NOT_DECREASING;
        }
    }

    return SUCCESSFUL_PROCEDURE;
}

void RarefactionContinuationMethod::curve(const RealVector & inputVector, int direction, vector<RealVector> & output) {

    output.clear();


    int info = SUCCESSFUL_PROCEDURE;
    int dimension = inputVector.size();
    RealVector localInputVector(inputVector);

    ContinuationRarefactionFlow & testeFlow = (ContinuationRarefactionFlow &) solver_->getProfile().getFunction();

    int indx = testeFlow.getFamilyIndex();

    double deltaxi = ((LSODEProfile &) solver_->getProfile()).deltaTime();

    double in[dimension];

    for (int i = 0; i < dimension; i++) {
        in[i] = localInputVector(i);
    }

    double lambda;

    double rev[dimension];

    info = init(&testeFlow, dimension, in, indx, direction, deltaxi, &lambda, &rev[0]);


    for (int i = 0; i < dimension; i++) {
        testeFlow.setReferenceVectorComponent(i, rev[i]);
    }

    if (info == SUCCESSFUL_PROCEDURE) {
        output.push_back(inputVector);
    } else {
        cout << "Error: " << info << endl;
    }

    double speed = lambda;

    int step = 0;

    RealVector outputVector(inputVector.size());
    // -------------------------Invoke th solver     ----------------------------------

    double testeDouble = 0; //TODO Dummy value !!
    double nowIn[dimension];
    //double oldRefVec[dimension];
    //double tempRefVector[dimension];
    double speed_prev;

    RealVector outputVector_prev;
//    cout << testeFlow.getReferenceVector() << endl;
//    cout << "Info = " << info << endl;


    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {

        outputVector_prev = localInputVector;
        speed_prev = speed;

        /*
                for (int i = 0; i < dimension; i++) {
                    oldRefVec[i] = testeFlow.getReferenceVectorComponent(i);
                }
         */
//        cout << "before solver" << endl;
        info = solver_->solve(localInputVector, outputVector, testeDouble);
//        cout << "info (solver) = " << info << endl;

        if (info == SUCCESSFUL_PROCEDURE) {

            for (int i = 0; i < dimension; i++) {
                nowIn[i] = outputVector(i);
            }

            //-----------------------------------Stop Criterion---------------------------

            info = testeFlow.flux(dimension, indx, &nowIn[0], &speed, 0);
//            cout << "info (flux) = " << info << endl;
//            cout << "speed = " << speed << ", speed_prev = " << speed_prev << endl;

            if (info == ABORTED_PROCEDURE) return;

            // Eigenvalues should follow a monotonous trend. If they don't, abort.


            if (speed > speed_prev && direction == -1) {
                info = ABORTED_PROCEDURE;
                return;
            }


            if (speed < speed_prev && direction == 1) {
                info = ABORTED_PROCEDURE;
                return;
            }

            // Mon 10 May 2010 05:07:33 PM BRT 
            RealVector r;

            int cllsn = (solver_->getProfile().boundary().intersection(outputVector, outputVector_prev, r));
            //cout << "cllsn = " << cllsn << endl;
            if (cllsn == -1) {
                // Both outside
                info = ABORTED_PROCEDURE;
                return; // ABORTED_PROCEDURE;
            } else if (cllsn == 1) {
                // Both inside
                //for (int i = 0; i < n; i++) Ustore[i] = U[i];
                //Ustore[n] = speed;
                output.push_back(outputVector);
            } else if (cllsn == 0) {
                // One inside, one outside: store and get out
                //double ppnt[n + 1];
                //for (int i = 0; i < n; i++) ppnt[i] = r[i];
                //flux(n, indx, flux_object, ppnt, &(ppnt[n]), 0);

                // Store, update the counter and the time
                //add(out, ppnt);
                //(*numtotal)++;
                //tout = t + deltaxi;
                LSODE::increaseTime();
                output.push_back(r);

                // Reference vector for the next curve (a composite):
                //for (int i = 0; i < n; i++) ref_vec[i] = ppnt[i] - Uprev[i];
                RealVector ref_vec(r);
                for (int i = 0; i < dimension; i++) ref_vec(i) = r(i) - outputVector_prev(i);

                testeFlow.setReferenceVector(ref_vec);
                //normalize(n, ref_vec);

                info = ABORTED_PROCEDURE;
                return;
            }


            // Mon 10 May 2010 05:07:33 PM BRT 

            // Reference vector

            /*            if (testeFlow.prodint(dimension, oldRefVec, tempRefVector) > 0) for (int ii = 0; ii < dimension; ii++) testeFlow.setReferenceVectorComponent(ii, tempRefVector[ii]); //rev[ii] = tempRefVector[ii];
                        else for (int ii = 0; ii < dimension; ii++) testeFlow.setReferenceVectorComponent(ii, -tempRefVector[ii]); //rev[ii] = -tempRefVector[ii];
             */
            for (int i = 0; i < dimension; i++) testeFlow.setReferenceVectorComponent(i, outputVector(i) - outputVector_prev(i));
//            for (int i = 0; i < dimension; i++) cout << "refvec[" << i << "] = " << outputVector(i) - outputVector_prev(i) << endl;


            //previouseigenvalue = noweigenvalue;

            output.push_back(outputVector);

            localInputVector = outputVector;

            step++;

            LSODE::increaseTime();


        }
    }

}

RarefactionMethod * RarefactionContinuationMethod::clone() const {

    return new RarefactionContinuationMethod(*this);

}
