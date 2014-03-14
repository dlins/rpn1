#include "Quad2.h"
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"

Quad2::Quad2(const Quad2FluxParams & params) : SubPhysics(*defaultBoundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {

    fluxFunction_ = new Quad2FluxFunction(params);
    accumulationFunction_ = new Quad2AccumulationFunction();

    DEFAULT_SIGMA = "-.021";
    DEFAULT_XZERO = ".13 .07";

    setHugoniotFunction(new Hugoniot_Curve(&fluxFunction(),&accumulation()));
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();


}

Quad2::~Quad2() {


}

Boundary * Quad2::defaultBoundary() const {

    RealVector min(2);

    min.component(0) = -0.5 * 8.;
    min.component(1) = -0.5 * 8.;

    RealVector max(2);

    max.component(0) = 0.5 * 8.;
    max.component(1) = 0.5 * 8.;

    return new RectBoundary(min, max);

}

SubPhysics * Quad2::clone()const {
    return new Quad2(*this);
}

Quad2::Quad2(const Quad2 & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), *new Space("R2", 2), "QuadraticR2", _SIMPLE_ACCUMULATION_) {


    setHugoniotFunction(new Hugoniot_Curve(&copy.fluxFunction(),&copy.accumulation()));
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();
}

void Quad2::setParams(vector<string> params) {


    RealVector fluxParamVector(10);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        std::stringstream stream(params[i]);
        stream >> paramValue;

        fluxParamVector.component(i) = paramValue;

    }

    //Viscosity Matrix


    double f1, g1, f2, g2;

    std::stringstream f1Stream(params[10]);
    std::stringstream g1Stream(params[11]);
    std::stringstream f2Stream(params[12]);
    std::stringstream g2Stream(params[13]);


    f1Stream >> f1;
    g1Stream >> g1;
    f2Stream >> f2;
    g2Stream >> g2;

    if (!(f1 == 1.0 && g1 == 0.0 && f2 == 0.0 && g2 == 1.0)) {

        RealVector viscosityElements(4);
        viscosityElements(0) = f1;
        viscosityElements(1) = g1;
        viscosityElements(2) = f2;
        viscosityElements(3) = g2;

        Quad2_Viscosity_Matrix * viscosityMatrix = new Quad2_Viscosity_Matrix(viscosityElements);

        delete getViscosityMatrix();

        setViscosityMatrix(viscosityMatrix);
    } else {
        delete getViscosityMatrix();
        setViscosityMatrix(new Viscosity_Matrix());

    }

    fluxFunction_->fluxParams(Quad2FluxParams(fluxParamVector));

}

vector<double> * Quad2::getParams() {



    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

    return paramsVector;

}