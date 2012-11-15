#include "Cub2.h"
#include "Hugoniot_Curve.h"

Cub2::Cub2(const Cub2FluxParams & params) : SubPhysics(*defaultBoundary(), *new Space("R2", 2), "Cub2", _SIMPLE_ACCUMULATION_) {

    fluxFunction_ = new Cub2FluxFunction(params);
    accumulationFunction_ = new Cub2AccumulationFunction();

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

//    RealVector Uref(2);
//    Uref.component(0) = 0;
//    Uref.component(1) = 0;

//    CubHugoniotFunction * cubHugoniot = new CubHugoniotFunction(Uref, (Cub2FluxFunction&) * fluxFunction_);
    //
    setHugoniotFunction(new Hugoniot_Curve());


}

Cub2::~Cub2() {


}

Cub2::Cub2(const Cub2 & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R2", 2), "Cub2", _SIMPLE_ACCUMULATION_) {

//    CubHugoniotFunction * cubHugoniot = new CubHugoniotFunction((CubHugoniotFunction&) * copy.getHugoniotFunction());
    //
    setHugoniotFunction(new Hugoniot_Curve());

}

SubPhysics * Cub2::clone()const {
    return new Cub2(*this);
}


 Boundary * Cub2::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = -10.0;
    min.component(1) = -10.0;

    RealVector max(2);

    max.component(0) = 10.0;
    max.component(1) = 10.0;

    return new RectBoundary(min, max);

}