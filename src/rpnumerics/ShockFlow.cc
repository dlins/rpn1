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

ShockFlow::ShockFlow(const RealVector & xzero, const FluxFunction &flux) : WaveFlow(flux) {

//    xZero_ = new RealVector(xzero);
    

    
    params_=new ShockFlowParams(xzero,0); //sigma =0
    
    




}

ShockFlow::ShockFlow(const ShockFlowParams & params,const FluxFunction  &flux) : WaveFlow(flux),params_(new ShockFlowParams(params)) {
}

void ShockFlow::setXZero(const RealVector & xzero) {

    //    delete xZero_;

    PhasePoint newPhasePoint(xzero);

    params_->setPhasePoint(newPhasePoint);
    //    xZero_=new RealVector(xzero);

}

const RealVector & ShockFlow::XZero(void) const {


    return (const RealVector &) params_->getPhasePoint();



    //    return xZero_;
}

ShockFlow::~ShockFlow() {//delete xZero_;
    delete params_;
}

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

