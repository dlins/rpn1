#include "Quad4.h"

Quad4::Quad4(const Quad4FluxParams & params) : fluxFunction_(new Quad4FluxFunction(params)), accumulationFunction_(new Quad4AccumulationFunction()),
boundary_(defaultBoundary()) {
    FLUX_ID = "QuadraticR4";
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}

const char * Quad4::ID(void) const {
    return FLUX_ID;
}

Quad4::~Quad4() {

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
}

Quad4::Quad4(const Quad4 & copy) {

    Quad4FluxParams params((Quad4FluxParams &) copy.fluxFunction().fluxParams());
    fluxFunction_ = new Quad4FluxFunction(params);
    accumulationFunction_ = new Quad4AccumulationFunction((Quad4AccumulationFunction &) copy.accumulation());
    boundary_ = copy.boundary().clone();
    FLUX_ID = "QuadraticR4";

}
