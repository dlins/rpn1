/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Triphase.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TriPhase.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




TriPhase::TriPhase(const TriPhaseParams & params, const PermParams & permParams,
        const CapilParams & capilParams, const ViscosityParams & viscParams){
    fluxFunction_ = new TriPhaseFluxFunction(params, permParams, capilParams, viscParams);
    
    FLUX_ID="TriPhase";

    accFunction_ = new TriPhaseAccumulationFunction(); //TODO Using default accumulationFunction

    boundary_= defaultBoundary();
    
}


IsoTriang2DBoundary * TriPhase::defaultBoundary(){
    
    RealVector A(2);
    
    A.component(0)=0;
    A.component(1)=0;
    
    RealVector B(2);
    
    B.component(0)=0;
    B.component(1)=1;
    
    RealVector C(2);
    
    C.component(0)=1;
    C.component(1)=0;
    
    
    return new IsoTriang2DBoundary(A, B, C);
    
    
}



void TriPhase::boundary(const Boundary & boundary){
    
    delete boundary_;
    
    boundary_=boundary.clone();
    
}

TriPhase::TriPhase(const TriPhase & copy){

    fluxFunction_ = (FluxFunction *) copy.fluxFunction().clone();

    boundary_= copy.boundary().clone();

    accFunction_ = (AccumulationFunction *)copy.accumulation().clone();

    FLUX_ID="TriPhase";

    
}

const char * TriPhase::ID()const {return FLUX_ID;}

Physics * TriPhase::clone()const {return new TriPhase(*this);}

TriPhase::~TriPhase(){
    
    delete fluxFunction_;
    delete boundary_;
    delete accFunction_;
    
}




