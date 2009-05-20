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
    delete solver_;

}

RPnCurve & RarefactionContinuationMethod::curve(const RealVector &inputVector, int direction){

    vector <RealVector> coords;
//    vector <double> times;

//    ODESolution solution(coords, times);

//   int info = solver_->solve(inputVector, solution);
    for (int i = 0; i < 10; i++) {

        RealVector testeVector(2);
        testeVector(0) = 0.1 * i;
        testeVector(1) = 0.1 * i;

        coords.push_back(testeVector);
    }

    RPnCurve * result = new RPnCurve(coords);
    
    return *result;



}


RarefactionMethod * RarefactionContinuationMethod::clone() const{
    
    return new RarefactionContinuationMethod (*this);
    
}
