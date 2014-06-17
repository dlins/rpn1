/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyQuadPhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CoreyQuadPhysics.h"
#include "Double_Contact.h"
#include "HugoniotContinuation2D2D.h"
#include "ImplicitHugoniotCurve.h"
#include "CoreyQuadExplicitHugoniotCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



CoreyQuadPhysics::CoreyQuadPhysics() : SubPhysics(CoreyQuad(CoreyQuad_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "CoreyQuad", _SIMPLE_ACCUMULATION_) {

    StoneParams * params = new StoneParams();

    StonePermParams * permParams = new StonePermParams();

    StoneFluxFunction * stoneFluxFunction = new StoneFluxFunction(*params, *permParams);

   Stone_Explicit_Bifurcation_Curves * stoneExplicitBifurcation = new Stone_Explicit_Bifurcation_Curves(stoneFluxFunction);
   cout<<"sebc antes: "<<stoneExplicitBifurcation<<endl;
    hugoniotCurveArray_->operator []("COREY") = new CoreyQuadExplicitHugoniotCurve((CoreyQuad *) & fluxFunction(),&accumulation(), stoneExplicitBifurcation, &getBoundary());
    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(&fluxFunction(),&accumulation(), &getBoundary());
    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);
    
    
     hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(getHugoniotContinuationMethod());
    
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();

}

SubPhysics * CoreyQuadPhysics::clone()const {

    return new CoreyQuadPhysics(*this);
}

CoreyQuadPhysics::CoreyQuadPhysics(const CoreyQuadPhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), copy.domain(), "CoreyQuad", _SIMPLE_ACCUMULATION_) {

    
    
    StoneParams * params = new StoneParams();

    StonePermParams * permParams = new StonePermParams();

    StoneFluxFunction * stoneFluxFunction = new StoneFluxFunction(*params, *permParams);

   Stone_Explicit_Bifurcation_Curves * stoneExplicitBifurcation = new Stone_Explicit_Bifurcation_Curves(stoneFluxFunction);

    
     hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(getHugoniotContinuationMethod());
    hugoniotCurveArray_->operator []("COREY") = new CoreyQuadExplicitHugoniotCurve((CoreyQuad *) & fluxFunction(), &copy.accumulation(),stoneExplicitBifurcation, &getBoundary());

    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(&fluxFunction(),&accumulation(), &getBoundary());
    


    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);

    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();

}

CoreyQuadPhysics::~CoreyQuadPhysics() {

}

void CoreyQuadPhysics::setParams(vector<string> newParams) {


    RealVector fluxParamVector(7);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {
        std::stringstream stream(newParams[i]);
        stream >> paramValue;


        fluxParamVector.component(i) = paramValue;


    }


    CoreyQuad_Params newCoreyQuadParams(fluxParamVector);
    fluxFunction_->fluxParams(newCoreyQuadParams);

}

vector<double> * CoreyQuadPhysics::getParams() {


    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

    return paramsVector;

}

Boundary * CoreyQuadPhysics::defaultBoundary() const {

    RealVector min(2);

    min.component(0) = 0.0;
    min.component(1) = 0.0;

    RealVector max(2);

    max.component(0) = 1.0;
    max.component(1) = 1.0;

    return new Three_Phase_Boundary(min, max);
}
