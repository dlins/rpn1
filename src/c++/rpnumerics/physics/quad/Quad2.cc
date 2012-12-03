#include "Quad2.h"
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"

Quad2::Quad2(const Quad2FluxParams & params) : SubPhysics(*defaultBoundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {

    fluxFunction_ = new Quad2FluxFunction(params);
    accumulationFunction_ = new Quad2AccumulationFunction();

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

    //    RealVector Uref(2);
    //    Uref.component(0) = 0;
    //    Uref.component(1) = 0;

    //    Quad2HugoniotFunction * quad2Hugoniot = new Quad2HugoniotFunction(Uref, (Quad2FluxFunction&) * fluxFunction_);
    //
    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());


}

Quad2::~Quad2() {


}

Boundary * Quad2::defaultBoundary() const {

    RealVector min(2);

    min.component(0) = -0.5;
    min.component(1) = -0.5;

    RealVector max(2);

    max.component(0) = 0.5;
    max.component(1) = 0.5;

    return new RectBoundary(min, max);

}

SubPhysics * Quad2::clone()const {
    return new Quad2(*this);
}

Quad2::Quad2(const Quad2 & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {


    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());

}

void Quad2::setParams(vector<string> params) {


    RealVector fluxParamVector(10);

    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        double paramValue = atof(params[i].c_str());

        fluxParamVector.component(i) = paramValue;


    }




    fluxFunction_->fluxParams(Quad2FluxParams(fluxParamVector));








}