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
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();

}

TriPhase::TriPhase() : SubPhysics(TriPhaseFluxFunction(TriPhaseParams(), PermParams(), CapilParams(), ViscosityParams()), TriPhaseAccumulationFunction(), *defaultBoundary(), Multid::PLANE, "TriPhase", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
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

    double eps = triPhaseFluxFunction.visc().getEpsl();

    paramsVector->push_back(eps);

    return paramsVector;


}

void TriPhase::setParams(vector<string> params) {


    RealVector fluxParamVector(7);
    double paramValue;
    //Flux params

    for (int i = 0; i < fluxParamVector.size(); i++) {

        std::stringstream stream(params[i]);
        stream >> paramValue;



        fluxParamVector.component(i) = paramValue;

    }

    TriPhaseFluxFunction & triPhaseFlux = (TriPhaseFluxFunction&) fluxFunction();

    triPhaseFlux.fluxParams(TriPhaseParams(fluxParamVector));

    //Perm params


    for (int i = 8; i < 14; i++) {
        std::stringstream stream(params[i]);
        stream >> paramValue;

        triPhaseFlux.perm().setParams(i - 8, paramValue);
    }
    //Capil params

    for (int i = 15; i < 18; i++) {
        std::stringstream stream(params[i]);
        stream >> paramValue;

        triPhaseFlux.capil().params().setParam(i - 15, paramValue);
    }

    //Viscosity params 
    
    std::stringstream stream(params[params.size() - 1]);
    stream >> paramValue;
    triPhaseFlux.visc().setEpsl(paramValue);

}

Boundary * TriPhase::defaultBoundary() const {
    

    return new Three_Phase_Boundary();


}

TriPhase::TriPhase(const TriPhase & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), Multid::PLANE, "TriPhase", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = defaultBoundary();


}

SubPhysics * TriPhase::clone()const {
    return new TriPhase(*this);
}

TriPhase::~TriPhase() {

}




