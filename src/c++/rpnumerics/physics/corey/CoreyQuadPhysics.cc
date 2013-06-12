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
<<<<<<< HEAD
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"
#include "Shock.h"
=======
>>>>>>> b534e68b19e6831710f42e3cd6a6513d38a6f3f1

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



CoreyQuadPhysics::CoreyQuadPhysics() : SubPhysics(CoreyQuad(CoreyQuad_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "CoreyQuad", _SIMPLE_ACCUMULATION_) {
    
    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    setHugoniotFunction(new Hugoniot_Curve());
<<<<<<< HEAD
    preProcessedBoundary_= defaultBoundary();
=======

    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();






>>>>>>> b534e68b19e6831710f42e3cd6a6513d38a6f3f1
}

SubPhysics * CoreyQuadPhysics::clone()const {

    return new CoreyQuadPhysics(*this);
}

CoreyQuadPhysics::CoreyQuadPhysics(const CoreyQuadPhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), copy.domain(), "CoreyQuad", _SIMPLE_ACCUMULATION_) {

    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    setHugoniotFunction(new Hugoniot_Curve());
<<<<<<< HEAD
    preProcessedBoundary_= defaultBoundary();
=======


    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();



>>>>>>> b534e68b19e6831710f42e3cd6a6513d38a6f3f1
}

CoreyQuadPhysics::~CoreyQuadPhysics() {

}

void CoreyQuadPhysics::setParams(vector<string> newParams) {


    RealVector fluxParamVector(7);
    double paramValue ;
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
