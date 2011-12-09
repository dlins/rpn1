#include "FluxFunction.h"

FluxFunction::FluxFunction(const FluxParams & params):params_(new FluxParams(params)){}

FluxFunction::~FluxFunction(void) {
    delete params_;
}


const FluxParams & FluxFunction::fluxParams(void) const {
    return *params_;
}

void FluxFunction::fluxParams(const FluxParams & params) {
    delete params_;
    params_=new FluxParams(params.params());
    
}
