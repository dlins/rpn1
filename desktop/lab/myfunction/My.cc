#include "My.h"

My::My(const MyFluxParams & params) : 
	params_(new MyFluxParams(params)),
        fluxFunction_(new MyFluxFunction(params))
        
{
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}

My::~My(){
    delete params_;
    delete fluxFunction_;
}

My::My(const My & copy){
    params_=new MyFluxParams((MyFluxParams &)copy.fluxFunction().fluxParams());
    fluxFunction_=new MyFluxFunction(*params_);
}
