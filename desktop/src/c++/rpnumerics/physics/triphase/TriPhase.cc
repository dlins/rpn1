/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Triphase.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TriPhase.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




TriPhase::TriPhase(const TriPhaseParams & params, const PermParams & permParams,
        const CapilParams & capilParams, const ViscosityParams & viscParams) :
SubPhysics(TriPhaseFluxFunction(params, permParams, capilParams, viscParams), TriPhaseAccumulationFunction(), *defaultBoundary(), Multid::PLANE, "TriPhase", _SIMPLE_ACCUMULATION_) {

    setHugoniotFunction(new Hugoniot_Curve());

    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    preProcessedBoundary_ = defaultBoundary();

}

TriPhase::TriPhase() : SubPhysics(TriPhaseFluxFunction(TriPhaseParams(), PermParams(), CapilParams(), ViscosityParams()), TriPhaseAccumulationFunction(), *defaultBoundary(), Multid::PLANE, "TriPhase", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());

    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    preProcessedBoundary_ = defaultBoundary();


}

vector<double> * TriPhase::getParams() {

    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction().fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction().fluxParams().component(i));
    }
    TriPhaseFluxFunction & triPhaseFluxFunction = (TriPhaseFluxFunction &) fluxFunction();


    for (int i = 0; i < triPhaseFluxFunction.perm().params().size(); i++) {
        paramsVector->push_back(triPhaseFluxFunction.perm().params().getValue(i));
    }

    for (int i = 0; i < triPhaseFluxFunction.capil().params().size(); i++) {
        paramsVector->push_back(triPhaseFluxFunction.capil().params().getParam(i));
    }

    double eps = triPhaseFluxFunction.visc().epsl();

    paramsVector->push_back(eps);

    return paramsVector;


}

void TriPhase::setParams(vector<string> params) {


    RealVector fluxParamVector(7);

    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        double paramValue = atof(params[i].c_str());

        fluxParamVector.component(i) = paramValue;

    }

    TriPhaseFluxFunction & triPhaseFlux = (TriPhaseFluxFunction&) fluxFunction();

    triPhaseFlux.fluxParams(TriPhaseParams(fluxParamVector));

    //Perm params


    for (int i = 8; i < 14; i++) {

        double paramValue = atof(params[i].c_str());
        triPhaseFlux.perm().setParams(i - 8, paramValue);
    }
    //Capil params

    for (int i = 15; i < 18; i++) {

        double paramValue = atof(params[i].c_str());
        triPhaseFlux.capil().params().setParam(i - 15, paramValue);
    }

    //Viscosity params 

    triPhaseFlux.visc().epsl() = atof(params[params.size() - 1].c_str());

}

Boundary * TriPhase::defaultBoundary() const {
    //
    //    RealVector A(2);
    //
    //    A.component(0) = 0;
    //    A.component(1) = 0;
    //
    //    RealVector B(2);
    //
    //    B.component(0) = 0;
    //    B.component(1) = 1;
    //
    //    RealVector C(2);
    //
    //    C.component(0) = 1;
    //    C.component(1) = 0;


    return new Three_Phase_Boundary();


}

TriPhase::TriPhase(const TriPhase & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), Multid::PLANE, "TriPhase", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());

    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    preProcessedBoundary_ = defaultBoundary();


}

SubPhysics * TriPhase::clone()const {
    return new TriPhase(*this);
}

TriPhase::~TriPhase() {

}




