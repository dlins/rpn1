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
#include "RarefactionContinuationMethod.h"
#include "LSODEProfile.h"

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
    //    delete solver_; //TODO Memory leaking ???

}

void RarefactionContinuationMethod::curve(const RealVector & inputVector, int direction, vector<RealVector> & output) {

    RealVector localInputVector(inputVector);

    int i = 0;
    int info = SUCCESSFUL_PROCEDURE;
    int dimension = inputVector.size();
    ContinuationRarefactionFlow & testeFlow = (ContinuationRarefactionFlow &) solver_->getProfile().getFunction();

    int indx = 1; //TODO family index ??
    double deltaxi = 0.05;
    double  in[dimension];

    for (i = 0; i < dimension; i++) {
        in[i] = inputVector(i);
    }

    double lambda;
    double rev[dimension];

    info = testeFlow.rarinit(dimension, in, indx, direction, deltaxi, &lambda, &rev[0]);
    
    if (info==SUCCESSFUL_PROCEDURE) {
        output.push_back(inputVector);
    }
        

    double previouseigenvalue = lambda;

    RealVector outputVector(dimension);

    int step = 0;

    while (step < solver_->getProfile().maxStepNumber() && info == SUCCESSFUL_PROCEDURE) {

        double testeDouble =0; //TODO Dummy value !!
        double nowIn[dimension];
        double oldRefVec[dimension];
        double tempRefVector[dimension];
        double noweigenvalue;

        for (i = 0; i < dimension; i++) {
            oldRefVec[i] = rev[i];
        }

        LSODEProfile & lsodeProfile = (LSODEProfile &) solver_->getProfile();

        for (i = 0; i < dimension; i++) {

            lsodeProfile.setParamComponent(i, rev[i]); //Setando o vetor de referencia
        }

        info = solver_->solve(localInputVector, outputVector, testeDouble);
        
        
        if (info== SUCCESSFUL_PROCEDURE){

        for (i = 0; i < dimension; i++) {
            nowIn[i] = outputVector(i);
        }

        //--------------------------------Criterio de parada -----------------------------
        
       info= testeFlow.flux(dimension, indx, &nowIn[0], &noweigenvalue, &tempRefVector[0]);

            if (info == ABORTED_PROCEDURE) return;

        // Eigenvalues should follow a monotonous trend. If they don't, abort.

        if (noweigenvalue > previouseigenvalue && direction == -1) {
            info = ABORTED_PROCEDURE;

        }
        if (noweigenvalue < previouseigenvalue && direction == 1) {
            info = ABORTED_PROCEDURE;

        }
        
        // Reference vector

        if (testeFlow.prodint(dimension, oldRefVec, tempRefVector) > 0) for (int ii = 0; ii < dimension; ii++) rev[ii] = tempRefVector[ii];
        else for (int ii = 0; ii < dimension; ii++) rev[ii] = -tempRefVector[ii];


        previouseigenvalue = noweigenvalue;

        output.push_back(outputVector);
        
        cout << "Tamanho da saida " << output.size() << endl;

        localInputVector = outputVector;
        
        step++;
        
        cout << "Saida do solver " << outputVector << endl;
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

        localInputVector = outputVector;
        i++;
        cout << "Saida do solver " << outputVector << endl;
    }

    return *result;

}

RarefactionMethod * RarefactionContinuationMethod::clone() const {

    return new RarefactionContinuationMethod(*this);

}
