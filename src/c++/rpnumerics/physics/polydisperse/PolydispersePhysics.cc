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
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"
#include "Shock.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */



PolydispersePhysics::PolydispersePhysics() : SubPhysics(Polydisperse(Polydisperse_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "Polydisperse", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    preProcessedBoundary_=defaultBoundary();
}

SubPhysics * PolydispersePhysics::clone()const {

    return new PolydispersePhysics(*this);
}

PolydispersePhysics::PolydispersePhysics(const PolydispersePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), copy.domain(), "Polydisperse", _SIMPLE_ACCUMULATION_) {
    setHugoniotFunction(new Hugoniot_Curve());
    setDoubleContactFunction(new Double_Contact());
    setShockMethod(new Shock());
    preProcessedBoundary_=copy.getPreProcessedBoundary()->clone();
   
}

PolydispersePhysics::~PolydispersePhysics() {

}

void PolydispersePhysics::setParams(vector<string> newParams) {

 RealVector fluxParamVector(7);

    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        double paramValue = atof(newParams[i].c_str());

        fluxParamVector.component(i) = paramValue;


    }

    Polydisperse_Params newPolydisperseParams(fluxParamVector);


    fluxFunction_->fluxParams(newPolydisperseParams);

}


vector<double> *  PolydispersePhysics::getParams(){
     
     
     
     vector<double> * paramsVector = new vector<double>();
      
      for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
          paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

      return paramsVector;

}

Boundary * PolydispersePhysics::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = 0.0;
    min.component(1) = 0.0;

    RealVector max(2);

    max.component(0) = 1.0;
    max.component(1) = 1.0;

    return new Three_Phase_Boundary(min, max);
}