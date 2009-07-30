#include "Quad2.h"

Quad2::Quad2(const Quad2FluxParams & params) : fluxFunction_(new Quad2FluxFunction(params)), accumulationFunction_(new Quad2AccumulationFunction()),
boundary_(defaultBoundary()) {
    FLUX_ID = "QuadraticR2";
    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";
}

const char * Quad2::ID(void) const {
    return FLUX_ID;
}

Quad2::~Quad2() {

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
}

Quad2::Quad2(const Quad2 & copy) {
    cout << "Chamando construtor de copia" << endl;
    fluxFunction_ = new Quad2FluxFunction(( Quad2FluxFunction&)copy.fluxFunction());
    accumulationFunction_ = new Quad2AccumulationFunction((Quad2AccumulationFunction &) copy.accumulation());
    boundary_ = copy.boundary().clone();
    FLUX_ID = "QuadraticR2";

}
