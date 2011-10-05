#include "Cub2.h"

Cub2::Cub2(const Cub2FluxParams & params) : SubPhysics(*defaultBoundary(), *new Space("R2", 2), "Cub2", _SIMPLE_ACCUMULATION_) {

    fluxFunction_ = new Cub2FluxFunction(params);
    accumulationFunction_ = new Cub2AccumulationFunction();

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

    RealVector Uref(2);
    Uref.component(0) = 0;
    Uref.component(1) = 0;

    CubHugoniotFunction * cubHugoniot = new CubHugoniotFunction(Uref, (Cub2FluxFunction&) * fluxFunction_);
    //
    setHugoniotFunction(cubHugoniot);


}

Cub2::~Cub2() {


}

Cub2::Cub2(const Cub2 & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R2", 2), "Cub2", _SIMPLE_ACCUMULATION_) {

    CubHugoniotFunction * cubHugoniot = new CubHugoniotFunction((CubHugoniotFunction&) * copy.getHugoniotFunction());
    //
    setHugoniotFunction(cubHugoniot);

}