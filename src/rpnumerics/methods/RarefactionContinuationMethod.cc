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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


RarefactionContinuationMethod::RarefactionContinuationMethod(const RarefactionContinuationMethod & copy) : solver_(copy.getSolver().clone()) {
}

RarefactionContinuationMethod::RarefactionContinuationMethod(const ODESolver & solver) : solver_(solver.clone()) {

}

const ODESolver & RarefactionContinuationMethod::getSolver()const{

    return *solver_;
}
        
RarefactionContinuationMethod::~RarefactionContinuationMethod() {
  //    delete solver_; //TODO Memory leaking ???

}

void RarefactionContinuationMethod::curve(const RealVector & inputVector, int direction, vector<RealVector> & output) {

    RealVector localInputVector(inputVector);
    double testeDouble = 0;
    int i = 0;

    ContinuationRarefactionFlow testeFlow;

    int indx = 0; //TODO family index ??
    double deltaxi = 0.1;
    double * in = new double[inputVector.size()];
    
    for (i=0;i < inputVector.size();i++) {
        in[i] = inputVector(i);
    }
    
    double lambda;
    double rev[inputVector.size()];
    
    testeFlow.rarinit(inputVector.size(),in,indx,direction,deltaxi,&lambda,&rev[0]);
    
     for (i=0;i < localInputVector.size();i++) {
        localInputVector(i) = rev[i];
    }

//    for (i=0;i < inputVector.size();i++){
//        cout <<"Vector de referencia "<<rev[i]<<endl;
//    }
    
    while (i < solver_->getProfile().maxStepNumber()) {
        RealVector outputVector(inputVector.size());
        
        solver_->solve(localInputVector, outputVector, testeDouble);

        output.push_back(outputVector);
        cout <<"Tamanho da saida "<<output.size()<<endl;

        localInputVector = outputVector;
        i++;
        cout <<"Saida do solver "<<outputVector<<endl;
    }


}



RPnCurve & RarefactionContinuationMethod::curve(const RealVector &inputVector, int direction){

    vector <RealVector> coords;
    RealVector outputVector(inputVector.size());
    RealVector localInputVector(inputVector);
    int i=0;
    double testeDouble = 0;
    RPnCurve * result = new RPnCurve(coords);
    while (i < solver_->getProfile().maxStepNumber()) {

        solver_->solve(localInputVector, outputVector, testeDouble);

        result->add(outputVector);

        localInputVector = outputVector;
        i++;
        cout <<"Saida do solver "<<outputVector<<endl;
    }
    
    return *result;

}


RarefactionMethod * RarefactionContinuationMethod::clone() const{
    
    return new RarefactionContinuationMethod (*this);
    
}
