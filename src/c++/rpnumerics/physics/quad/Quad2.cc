#include "Quad2.h"

Quad2::Quad2(const Quad2FluxParams & params) : SubPhysics(*defaultBoundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {

    fluxFunction_ = new Quad2FluxFunction(params);
    accumulationFunction_ = new Quad2AccumulationFunction();

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

    RealVector Uref(2);
    Uref.component(0) = 0;
    Uref.component(1) = 0;

    Quad2HugoniotFunction * quad2Hugoniot = new Quad2HugoniotFunction(Uref, (Quad2FluxFunction&) * fluxFunction_);
    //
    setHugoniotFunction(quad2Hugoniot);


}

Quad2::~Quad2() {


}

Quad2::Quad2(const Quad2 & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {

    Quad2HugoniotFunction * quad2Hugoniot = new Quad2HugoniotFunction((Quad2HugoniotFunction&) * copy.getHugoniotFunction());
    //
    setHugoniotFunction(quad2Hugoniot);

}