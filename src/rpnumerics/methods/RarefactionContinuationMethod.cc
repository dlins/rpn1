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
    
    vector <RealVector> coords;
    vector <double> times;
    
    solution_=new ODESolution(coords,times);
}

const ODESolver & RarefactionContinuationMethod::getSolver()const{

    return *solver_;
}
        
RarefactionContinuationMethod::~RarefactionContinuationMethod() {
    delete solver_;
    delete solution_;
}

void RarefactionContinuationMethod::curve(const RealVector &inputVector, int direction){

    int info = 2;
    int steps = 0;

    info = solver_->solve(inputVector, *solution_);


}

vector <RealVector> RarefactionContinuationMethod::getCoords() const {
    return solution_->getCoords();
}

RarefactionMethod * RarefactionContinuationMethod::clone() const{
    
    return new RarefactionContinuationMethod (*this);
    
}
