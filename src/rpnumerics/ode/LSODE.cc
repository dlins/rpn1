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


LSODE::LSODE(const LSODEProfile & profile): profile_(new LSODEProfile(profile)), rpFunction_(profile.getFunction()){}


LSODE::~LSODE() {
    delete profile_;
    delete rpFunction_;
}

ODESolution & LSODE::solve(const RealVector & , int ) const{
    

    
    
    
    
}

ODESolverProfile & LSODE::getProfile(){ return *profile_;}


void LSODE::setProfile(const LSODEProfile & profile) {
    
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
    
    rpFunction_->jet(wState, jMatrix, 0);
    
    for(i=0;i< *neq;i++){
        
        out[i]=jMatrix.operator ()(i);
        
    }
    
}



//! Code comes here! daniel@impa.br

