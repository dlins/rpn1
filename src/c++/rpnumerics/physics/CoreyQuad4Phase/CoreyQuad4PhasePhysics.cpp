/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyQuad4PhasePhysics.cc
 */

#include "CoreyQuad4PhasePhysics.h"
#include "Double_Contact.h"
#include "HugoniotContinuation2D2D.h"
#include "ImplicitHugoniotCurve.h"

CoreyQuad4PhasePhysics::CoreyQuad4PhasePhysics() : SubPhysics(CoreyQuad4Phase(CoreyQuad4Phase_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "CoreyQuad4Phase", _SIMPLE_ACCUMULATION_) {

    StoneParams * params = new StoneParams();

    StonePermParams * permParams = new StonePermParams();

    StoneFluxFunction * stoneFluxFunction = new StoneFluxFunction(*params, *permParams);

    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(&fluxFunction(),&accumulation(), &getBoundary());
    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);


    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(getHugoniotContinuationMethod());

    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();
}

SubPhysics * CoreyQuad4PhasePhysics::clone()const {

    return new CoreyQuad4PhasePhysics(*this);
}

CoreyQuad4PhasePhysics::CoreyQuad4PhasePhysics(const CoreyQuad4PhasePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), copy.domain(), "CoreyQuad4Phase", _SIMPLE_ACCUMULATION_) {

    StoneParams * params = new StoneParams();

    StonePermParams * permParams = new StonePermParams();

    StoneFluxFunction * stoneFluxFunction = new StoneFluxFunction(*params, *permParams);


    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(getHugoniotContinuationMethod());

    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(&fluxFunction(),&accumulation(), &getBoundary());


    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);

    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();
}

CoreyQuad4PhasePhysics::~CoreyQuad4PhasePhysics() {
}

void CoreyQuad4PhasePhysics::setParams(vector<string> newParams) {

    RealVector fluxParamVector(9);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {
        std::stringstream stream(newParams[i]);
        stream >> paramValue;

        fluxParamVector.component(i) = paramValue;
    }

    CoreyQuad4Phase_Params newCoreyQuad4PhaseParams(fluxParamVector);
    fluxFunction_->fluxParams(newCoreyQuad4PhaseParams);
}

vector<double> * CoreyQuad4PhasePhysics::getParams() {

    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));
    }

    return paramsVector;
}


Boundary * CoreyQuad4PhasePhysics::defaultBoundary() const {

    RealVector min(3);

    min.component(0) = 0.0;
    min.component(1) = 0.0;
    min.component(2) = 0.0;

    RealVector max(3);

    max.component(0) = 1.0;
    max.component(1) = 1.0;
    max.component(2) = 1.0;

    return new Three_Phase_Boundary(min, max);
}
