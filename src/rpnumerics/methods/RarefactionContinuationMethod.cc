/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionContinuationMethod.cc
 **/

#include <vector>


#include "RPnCurve.h"

/*
 * ---------------------------------------------------------------
 * Includes:
 */
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
#ifdef TEST_RAREFACTION
            //            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't increase!\n");
#endif
            return LAMBDA_NOT_INCREASING;
        } else if (*lambda < lambdap && *lambda > lambdam) {
            // Nothing to do, the eigenvector is rev.
        } else if (*lambda > lambdap && *lambda < lambdam) {
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        } else {
#ifdef TEST_RAREFACTION
            //            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
#endif
            return LAMBDA_NOT_INCREASING;
        }
    } else { // Eigenvalues should decrease as the orbit advances
        if (*lambda >= lambdap && *lambda >= lambdam) {
#ifdef TEST_RAREFACTION
            //            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't decrease!\n");
#endif
            return LAMBDA_NOT_DECREASING;
        } else if (*lambda > lambdap && *lambda < lambdam) {
            // Nothing to do, the eigenvector is rev.
        } else if (*lambda < lambdap && *lambda > lambdam) {
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        } else {
#ifdef TEST_RAREFACTION
            //            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
#endif
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
    }
    double previouseigenvalue = lambda;

    int step = 0;

    RealVector outputVector(2);
    // -------------------------LOOP COM CHAMADA DO SOLVER - CALCULO DOS PONTOS DA CURVA----------------------------------

    double testeDouble = 0; //TODO Dummy value !!
    double nowIn[dimension];
    double oldRefVec[dimension];
    double tempRefVector[dimension];
    double noweigenvalue;

    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {

        for (int i = 0; i < dimension; i++) {
            oldRefVec[i] = testeFlow.getReferenceVectorComponent(i);
        }

        info = solver_->solve(localInputVector, outputVector, testeDouble);

        if (info == SUCCESSFUL_PROCEDURE) {

            for (int i = 0; i < dimension; i++) {
                nowIn[i] = outputVector(i);
            }

            //-----------------------------------Criterio de parada -----------------------------

            info = testeFlow.flux(dimension, indx, &nowIn[0], &noweigenvalue, &tempRefVector[0]);


            if (info == ABORTED_PROCEDURE) return;

            // Eigenvalues should follow a monotonous trend. If they don't, abort.

            if (noweigenvalue > previouseigenvalue && direction == -1) {
                info = ABORTED_PROCEDURE;
            }


            if (noweigenvalue < previouseigenvalue && direction == 1) {
                info = ABORTED_PROCEDURE;
            }

            // Reference vector

            if (testeFlow.prodint(dimension, oldRefVec, tempRefVector) > 0) for (int ii = 0; ii < dimension; ii++) testeFlow.setReferenceVectorComponent(ii, tempRefVector[ii]); //rev[ii] = tempRefVector[ii];
            else for (int ii = 0; ii < dimension; ii++) testeFlow.setReferenceVectorComponent(ii, -tempRefVector[ii]); //rev[ii] = -tempRefVector[ii];

            previouseigenvalue = noweigenvalue;

            output.push_back(outputVector);

            localInputVector = outputVector;

            step++;

            LSODE::tout_ += ((LSODEProfile &) solver_->getProfile()).deltaTime();
        }
    }

}

RPnCurve & RarefactionContinuationMethod::curve(const RealVector &inputVector, int direction) {

    vector <RealVector> coords;
    RealVector outputVector(inputVector.size());
    RealVector localInputVector(inputVector);
    int i = 0;
    double testeDouble = 0;
    RPnCurve * result = new RPnCurve(coords);

    while (i < solver_->getProfile().maxStepNumber()) {

        solver_->solve(localInputVector, outputVector, testeDouble);

        result->add(outputVector);
        LSODE::tout_ += ((LSODEProfile &) solver_->getProfile()).deltaTime();


        localInputVector = outputVector;
        i++;

    }

    return *result;

}

RarefactionMethod * RarefactionContinuationMethod::clone() const {

    return new RarefactionContinuationMethod(*this);

}
