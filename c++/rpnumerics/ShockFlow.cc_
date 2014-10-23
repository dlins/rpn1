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

ShockFlow::ShockFlow(const RealVector & xzero, const FluxFunction &flux) : WaveFlow(flux),params_(new ShockFlowParams(xzero,0)) {

}

ShockFlow::ShockFlow(const ShockFlowParams & params,const FluxFunction  &flux) : WaveFlow(flux),params_(new ShockFlowParams(params)) {

}


const ShockFlowParams & ShockFlow::getParams() const{
    return *params_;
}

void ShockFlow::setParams(const ShockFlowParams & params) {
    delete params_;
    params_ = new ShockFlowParams(params);
    
}


ShockFlow::~ShockFlow() {
    delete params_;
}



