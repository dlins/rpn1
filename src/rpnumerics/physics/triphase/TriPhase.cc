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
    
    TriPhase * copy2 = (TriPhase *)copy.clone();


    TriPhaseFluxFunction flux = (TriPhaseFluxFunction &) copy2->fluxFunction();
    
    RealVector paramsRealVector = flux.fluxParams().params();

    TriPhaseParams triphaseParams(paramsRealVector,paramsRealVector.size());
    
    PermParams perm = flux.perm().params();
    CapilParams capil = flux.capil().params();
    ViscosityParams visc = flux.visc();
    
    
    
    fluxFunction_ = new TriPhaseFluxFunction(triphaseParams,perm,capil,visc);
    boundary_=copy2->boundary().clone();
    accFunction_=(AccumulationFunction *)copy2->accumulation().clone();
    FLUX_ID="TriPhase";
    delete copy2;
    
}

const char * TriPhase::ID()const {return FLUX_ID;}

Physics * TriPhase::clone()const {return new TriPhase(*this);}

TriPhase::~TriPhase(){
    
    delete fluxFunction_;
    delete boundary_;
    delete accFunction_;
    
}




