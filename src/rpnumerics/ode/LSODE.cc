/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODE.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "LSODE.h"
#include "FluxParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


ODESolution & LSODE::solve(const RealVector & , int ) const{}
ODESolverProfile & LSODE::getProfile(){ return *profile_;}

LSODE::~LSODE(){delete profile_;}


void LSODE::setProfile(const LSODEProfile & profile){
    
    delete profile_;
    profile_=new LSODEProfile(profile);
}

int LSODE::function(int * neq , double * xi , double* U , double * out){
    
    int i ;
    
    WaveState  wState(*neq) ;
    
    for (i=0;i < *neq;i++){
        wState(i)=U[i];
    }
    
    JetMatrix jMatrix(*neq);
    
    RpFunction * function = getProfile().getFunction();
    
    function->jet(wState, jMatrix, 0);
    
    for(i=0;i< *neq;i++){
        
        out[i]=jMatrix.operator ()(i);
        
    }
    delete function;
    
    
}



//! Code comes here! daniel@impa.br

