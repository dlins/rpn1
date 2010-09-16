/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Corey.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Corey.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




Corey::Corey(const CoreyParams & params, const PermParams & permParams,
        const CapilParams & capilParams, const ViscosityParams & viscParams){
    fluxFunction_ = new CoreyFluxFunction(params, permParams, capilParams, viscParams);
    
    FLUX_ID="Corey";

    accFunction_ = new CoreyAccumulationFunction(); //TODO Using default accumulationFunction

    boundary_= defaultBoundary();
    
}


IsoTriang2DBoundary * Corey::defaultBoundary(){
    
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



void Corey::boundary(const Boundary & boundary){
    
    delete boundary_;
    
    boundary_=boundary.clone();
    
}

Corey::Corey(const Corey & copy){

    fluxFunction_ = (FluxFunction *) copy.fluxFunction().clone();

    boundary_= copy.boundary().clone();

    accFunction_ = (AccumulationFunction *)copy.accumulation().clone();

    FLUX_ID="Corey";

    
}

const char * Corey::ID()const {return FLUX_ID;}

Physics * Corey::clone()const {return new Corey(*this);}

Corey::~Corey(){
    
    delete fluxFunction_;
    delete boundary_;
    delete accFunction_;
    
}




