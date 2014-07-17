/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersePhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "PolydispersePhysics.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */



PolydispersePhysics::PolydispersePhysics() : SubPhysics(Polydisperse(Polydisperse_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "Polydisperse", _SIMPLE_ACCUMULATION_) {
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();
    
    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(fluxFunction_, accumulationFunction_, &getBoundary());

    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(hugoniot_continuation_method_);

    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);
    
    
    
}

SubPhysics * PolydispersePhysics::clone()const {

    return new PolydispersePhysics(*this);
}

PolydispersePhysics::PolydispersePhysics(const PolydispersePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), copy.domain(), "Polydisperse", _SIMPLE_ACCUMULATION_) {
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();
    
    
    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(fluxFunction_, accumulationFunction_, &getBoundary());

    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(hugoniot_continuation_method_);

    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);
    

}

PolydispersePhysics::~PolydispersePhysics() {

}

void PolydispersePhysics::setParams(vector<string> newParams) {

    RealVector fluxParamVector(7);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        std::stringstream stream(newParams[i]);
        stream >> paramValue;


        fluxParamVector.component(i) = paramValue;


    }

    Polydisperse_Params newPolydisperseParams(fluxParamVector);


    fluxFunction_->fluxParams(newPolydisperseParams);

}

vector<double> * PolydispersePhysics::getParams() {



    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

    return paramsVector;

}

Boundary * PolydispersePhysics::defaultBoundary() const {

    RealVector min(2);

    min.component(0) = 0.0;
    min.component(1) = 0.0;

    RealVector max(2);

    max.component(0) = 1.0;
    max.component(1) = 1.0;

    return new Three_Phase_Boundary(min, max);
}