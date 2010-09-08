/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



RarefactionFlow::RarefactionFlow(const RarefactionFlow & copy ):WaveFlow(copy.fluxFunction()){
    familyIndex_=copy.getFamilyIndex();
    timeDirection_=copy.direction();
    referenceVector_= new RealVector(copy.getReferenceVector());
}


RarefactionFlow::RarefactionFlow(const RealVector  & referenceVector,const int familyIndex, const int timeDirection, const FluxFunction & flux):WaveFlow(flux){

    timeDirection_=timeDirection;
    familyIndex_= familyIndex;
    referenceVector_ = new RealVector(referenceVector);

    
}


RarefactionFlow::~RarefactionFlow() {

    delete referenceVector_;
}



