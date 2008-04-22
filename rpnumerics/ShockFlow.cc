/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockFlow.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ShockFlow.h"

ShockFlow::ShockFlow(const RealVector & xzero,const FluxFunction &flux ):WaveFlow(flux){
    
    xZero_=new RealVector(xzero);
    
    
}
void ShockFlow::setXZero(const RealVector & xzero) {

    delete xZero_;
    xZero_=new RealVector(xzero);
    
}

const RealVector * ShockFlow::XZero(void) const{return xZero_;}

ShockFlow::~ShockFlow(){delete xZero_;}

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

