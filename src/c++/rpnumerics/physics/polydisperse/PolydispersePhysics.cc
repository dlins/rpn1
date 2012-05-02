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

}

SubPhysics * PolydispersePhysics::clone()const {

    return new PolydispersePhysics(*this);
}

PolydispersePhysics::PolydispersePhysics(const PolydispersePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), copy.domain(), "Polydisperse", _SIMPLE_ACCUMULATION_) {
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