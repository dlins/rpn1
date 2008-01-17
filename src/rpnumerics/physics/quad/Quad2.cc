#include "Quad2.h"

Quad2::Quad2(const Quad2FluxParams & params)
: params_(new Quad2FluxParams(params)),
        fluxFunction_(new Quad2FluxFunction(params)), accumulationFunction_(new Quad2AccumulationFunction()),
        boundary_(defaultBoundary())
        
{
    FLUX_ID = "QuadraticR2";
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}

const char * Quad2::ID(void) {
    return FLUX_ID;
}

Quad2::~Quad2(){
    
    delete params_;
    delete fluxFunction_;
    delete  accumulationFunction_;
    delete  boundary_;

    
}


Quad2::Quad2(const Quad2 & copy){
    
    params_=new Quad2FluxParams((Quad2FluxParams &)copy.fluxFunction()->fluxParams());
    fluxFunction_=new Quad2FluxFunction(*params_);
    accumulationFunction_= new Quad2AccumulationFunction((Quad2AccumulationFunction &)copy.accumulation());
    boundary_=copy.boundary()->clone();
    
}
