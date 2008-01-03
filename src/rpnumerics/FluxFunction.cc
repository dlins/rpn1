#include "FluxFunction.h"

FluxFunction::FluxFunction(const FluxParams & params):params_(new FluxParams(params)){}

FluxFunction::~FluxFunction(void) {
    
    delete params_;

}


FluxParams & FluxFunction::fluxParams(void)  {
    return *params_;
}

void FluxFunction::fluxParams(const FluxParams & params) {
    
    delete params_;
    params_=new FluxParams(params.params());
    
}
