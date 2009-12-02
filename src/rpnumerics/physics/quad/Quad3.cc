#include "Quad3.h"

Quad3::Quad3(const Quad3FluxParams & params) : fluxFunction_(new Quad3FluxFunction(params)), accumulationFunction_(new Quad3AccumulationFunction()),
boundary_(defaultBoundary()) {
    FLUX_ID = "QuadraticR3";
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
    printf("Hey\n");
}

const char * Quad3::ID(void) const {
    return FLUX_ID;
}

Quad3::~Quad3() {

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
}

Quad3::Quad3(const Quad3 & copy) {

    fluxFunction_ = new Quad3FluxFunction(( Quad3FluxFunction&)copy.fluxFunction());
    accumulationFunction_ = new Quad3AccumulationFunction((Quad3AccumulationFunction &) copy.accumulation());
    boundary_ = copy.boundary().clone();
    FLUX_ID = "QuadraticR3";

}
