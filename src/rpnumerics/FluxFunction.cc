#include "FluxFunction.h"

FluxFunction::FluxFunction(const FluxParams & params):params_(params){}

FluxFunction::~FluxFunction(void) {
}

FluxParams FluxFunction::fluxParams(void) const {
    return params_;
}

void FluxFunction::fluxParams(const FluxParams & params) {
    params_ = params;
}
