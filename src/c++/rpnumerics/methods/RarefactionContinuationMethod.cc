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
#include "LSODESolver.h"
#include "ContinuationShockFlow.h"
#include "RpNumerics.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


RarefactionContinuationMethod::RarefactionContinuationMethod(const RarefactionContinuationMethod & copy) : solver_(copy.getSolver().clone()) {
}

RarefactionContinuationMethod::RarefactionContinuationMethod(const ODESolver & solver) : solver_(solver.clone()) {
}

RarefactionContinuationMethod::RarefactionContinuationMethod(const ODESolver & solver, const Boundary & boundary, int family) : solver_(solver.clone()), boundary_(boundary.clone()), family_(family) {

}

const ODESolver & RarefactionContinuationMethod::getSolver()const {
    return *solver_;
}

RarefactionContinuationMethod::~RarefactionContinuationMethod() {
    delete boundary_;


}



int RarefactionContinuationMethod::init(ContinuationRarefactionFlow * flow,int n, double *in, int indx, const FluxFunction &ff, const AccumulationFunction &gg, int type, int increase, double deltaxi, double *lambda, double *rev){ /* NEW HERE */


//    cout<<"Dentro de init"<<endl;
    // 1. Find the eigenvalue and the eigenvector at in (the initial point):
//    printf("&rev = %p; (rev == 0 = %d)\n", rev, rev == 0);
    /* NEW BELOW */
    if (flow->flux(n, indx, ff, gg, type, in, lambda, rev) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    /* NEW ABOVE */
//    cout <<"after flow flux"<<endl;
    // 2. and 3. Find the eigenpairs at in_plus and in_minus.
    double epsilon = 10*deltaxi;
    double lambdap, lambdam; // Lambda_plus, lambda_minus
    double inp[n], inm[n];   // In_plus and in_minus
    double rep[n], rem[n];   // Eigenvectors at in_plus and in_minus
    int ii;
    for (ii = 0; ii < n; ii++){
        inp[ii] = in[ii] + epsilon*rev[ii];
        inm[ii] = in[ii] - epsilon*rev[ii];
    }
    cout<<"Dentro de init antes de flux"<<endl;
    if (flow->flux(n, indx, ff, gg, type, inp, &lambdap, &rep[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
    if (flow->flux(n, indx, ff, gg, type, inm, &lambdam, &rem[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;

    printf("@ rarefactioncurve(), after init.\nl- = % f, l = % f, l+ = % f\n", lambdam, *lambda, lambdap);
    printf("e = (");
    for (int i = 0; i < n; i++) printf("%6.2f, ", rev[i]);
    printf(")\n");

    // 4. Find the reference eigenvector.
    if (increase == 1){ // Eigenvalues should increase as the orbit advances
        if (*lambda <= lambdap && *lambda <= lambdam){
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't increase!\n");
            #endif
            return LAMBDA_NOT_INCREASING;
        }
        else if (*lambda < lambdap && *lambda > lambdam){
            // Nothing to do, the eigenvector is rev.
        }
        else if (*lambda > lambdap && *lambda < lambdam){
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        }
        else {
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_INCREASING;
        }
    }
    else {              // Eigenvalues should decrease as the orbit advances
        if (*lambda >= lambdap && *lambda >= lambdam){
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, lambda doesn't decrease!\n");
            #endif
            return LAMBDA_NOT_DECREASING;
        }
        else if (*lambda > lambdap && *lambda < lambdam){
            // Nothing to do, the eigenvector is rev.
        }
        else if (*lambda < lambdap && *lambda > lambdam){
            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
        }
        else {
            #ifdef TEST_RAREFACTION
            printf("Inside rarefactioncurve(): Cannot initialize, unexpected!\n");
            #endif
            return LAMBDA_NOT_DECREASING;
        }
    }

    return SUCCESSFUL_PROCEDURE;
}



//int RarefactionContinuationMethod::init(ContinuationRarefactionFlow * flow, int n, double *in, int indx, int increase, double deltaxi, double *lambda, double *rev)const {
//
//    // 1. Find the eigenvalue and the eigenvector at in (the initial point):
//    if (flow->flux(n, indx, in, lambda, rev) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
//    // 2. and 3. Find the eigencouples at in_plus and in_minus.
//    double epsilon = 10 * deltaxi;
//    double lambdap, lambdam; // Lambda_plus, lambda_minus
//    double inp[n], inm[n]; // In_plus and in_minus
//    double rep[n], rem[n]; // Eigenvectors at in_plus and in_minus
//    int ii;
//    for (ii = 0; ii < n; ii++) {
//        inp[ii] = in[ii] + epsilon * rev[ii];
//        inm[ii] = in[ii] - epsilon * rev[ii];
//    }
//    if (flow->flux(n, indx, inp, &lambdap, &rep[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
//    if (flow->flux(n, indx, inm, &lambdam, &rem[0]) == COMPLEX_EIGENVALUE) return COMPLEX_EIGENVALUE;
//
//    // 4. Find the reference eigenvector.
//    if (increase == 1) { // Eigenvalues should increase as the orbit advances
//        if (*lambda <= lambdap && *lambda <= lambdam) {
//            return LAMBDA_NOT_INCREASING;
//        } else if (*lambda < lambdap && *lambda > lambdam) {
//            // Nothing to do, the eigenvector is rev.
//        } else if (*lambda > lambdap && *lambda < lambdam) {
//            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
//        } else {
//            return LAMBDA_NOT_INCREASING;
//        }
//    } else { // Eigenvalues should decrease as the orbit advances
//        if (*lambda >= lambdap && *lambda >= lambdam) {
//            return LAMBDA_NOT_DECREASING;
//        } else if (*lambda > lambdap && *lambda < lambdam) {
//            // Nothing to do, the eigenvector is rev.
//        } else if (*lambda < lambdap && *lambda > lambdam) {
//            for (ii = 0; ii < n; ii++) rev[ii] = -rev[ii];
//        } else {
//            return LAMBDA_NOT_DECREASING;
//        }
//    }
//
//    return SUCCESSFUL_PROCEDURE;
//}

void RarefactionContinuationMethod::curve(const RealVector & inputVector, int direction, vector<RealVector> & output) {
    cout <<"here curve"<<endl;
    output.clear();
//    RealVector inputVector(3);

//    inputVector.component(0)=input.component(0);
//    inputVector.component(1) = input.component(1);

//    inputVector.component(2) = 1;//TODO Hardcoded !!

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
//    int type =_GENERAL_ACCUMULATION_;//TODO REMOVE
//    int type = _SIMPLE_ACCUMULATION_; //TODO REMOVE

     int type = RpNumerics::getPhysics().getPhysicsVector().at(0)->type();
    const FluxFunction & fluxFunction = RpNumerics::getPhysics().fluxFunction();//TODO REMOVE !!
    const AccumulationFunction & accumulationFunction = RpNumerics::getPhysics().accumulation(); //TODO REMOVE !!
    info = init(&testeFlow, dimension, in, indx,fluxFunction,accumulationFunction, type, direction, deltaxi, &lambda, &rev[0]);


//    info = init(&testeFlow, dimension, in, indx, direction, deltaxi, &lambda, &rev[0]);

    cout <<"Vetor de referencia: "<<testeFlow.getReferenceVector();

    for (int i = 0; i < dimension; i++) {
        testeFlow.setReferenceVectorComponent(i, rev[i]);
    }
    cout <<"after init"<<endl;
    if (info == SUCCESSFUL_PROCEDURE) {
        output.push_back(inputVector);
    } else {
        //        cout << "Error: " << info << endl;
    }

    double speed = lambda;

    int step = 0;

    RealVector outputVector(dimension);
    // -------------------------Invoke th solver     ----------------------------------

    double testeDouble = 0; //TODO Dummy value !!
    double nowIn[dimension];
    double speed_prev;

    RealVector outputVector_prev(dimension);


    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {

        outputVector_prev = localInputVector;
        speed_prev = speed;

        info = solver_->solve(localInputVector, outputVector, testeDouble);

        cout <<"saida do solver "<<outputVector<<endl;

        if (info == SUCCESSFUL_PROCEDURE) {

            for (int i = 0; i < dimension; i++) {
                nowIn[i] = outputVector(i);
            }

            //-----------------------------------Stop Criterion---------------------------

            info = testeFlow.flux(dimension,indx,fluxFunction,accumulationFunction,type, &nowIn[0], &speed, 0);



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
            RealVector r(dimension);
            int edge;
            int cllsn = (boundary_->intersection(outputVector, outputVector_prev, r,edge));
            cout <<"depois de intersection"<<endl;
            if (cllsn == -1) {
                // Both outside

                info = ABORTED_PROCEDURE;
                return; // ABORTED_PROCEDURE;
            } else if (cllsn == 1) {
                // Both inside

                output.push_back(outputVector);
            } else if (cllsn == 0) {
                // One inside, one outside: store and get out
                output.push_back(r);
                // Reference vector for the next curve (a composite):
                RealVector ref_vec(r);
                for (int i = 0; i < dimension; i++) ref_vec(i) = r(i) - outputVector_prev(i);

                testeFlow.setReferenceVector(ref_vec);

                info = ABORTED_PROCEDURE;
                return;
            }


            // Mon 10 May 2010 05:07:33 PM BRT 

            // Reference vector

            for (int i = 0; i < dimension; i++) testeFlow.setReferenceVectorComponent(i, outputVector(i) - outputVector_prev(i));
            output.push_back(outputVector);
            localInputVector = outputVector;
            step++;

        }
    }



}

const RealVector & RarefactionContinuationMethod::getReferenceVector() {
    return *referenceVector_;
}

void RarefactionContinuationMethod::setReferenceVector(const RealVector &vector) {

    delete referenceVector_;
    referenceVector_ = new RealVector(vector);
}

RarefactionMethod * RarefactionContinuationMethod::clone() const {

    return new RarefactionContinuationMethod(*this);

}
