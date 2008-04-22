/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Capillarity.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Capillarity.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Capillarity::Capillarity(const Capillarity & copy) {
    
    params_= new CapilParams(copy.params().acw(),copy.params().acg(),copy.params().lcw(),copy.params().lcg());
    
}

Capillarity::Capillarity(const CapilParams & params){
    
    params_ = new CapilParams(params.acw(), params.acg(), params.lcw(), params.lcg());
    
}

Capillarity::~Capillarity(){
    delete params_;
    
}

void Capillarity::jacobian(const RealVector & U , RealMatrix2 & capillarity_jacobian )const {
    
    double sw = U(0);
    double so = U(1);
    double sg = 1. - sw - so;
    capillarity_jacobian(0, 0, -dpcowdsw(sw) + dpcogdsw(sg));
    capillarity_jacobian(0, 1, dpcogdso(sg));
    capillarity_jacobian(1, 0, dpcogdsw(sg));
    capillarity_jacobian(1, 1, dpcogdso(sg));
    
}



double Capillarity::dpcowdsw(double sw) const {
    return -params_->acw() * (params_->lcw() + (1. - params_->lcw()) * 2. * (1. - sw));
}

double Capillarity::dpcogdsw(double sg) const{
    // finds DPcog(1-sw-so)/Dsw
    return params_->acg() * (params_->lcg() + (1. - params_->lcg()) * 2. * (1. - sg));
}

double Capillarity::dpcogdso(double sg)const {
    // finds DPcog(1-sw-so)/Dso
    return params_->acg() * (params_->lcg() + (1. - params_->lcg()) * 2. * (1. - sg));
}


//! Code comes here! daniel@impa.br

