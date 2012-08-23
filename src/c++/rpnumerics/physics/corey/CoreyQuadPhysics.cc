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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



CoreyQuadPhysics::CoreyQuadPhysics() : SubPhysics(CoreyQuad(CoreyQuad_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "CoreyQuad", _SIMPLE_ACCUMULATION_) {

}

SubPhysics * CoreyQuadPhysics::clone()const {

    return new CoreyQuadPhysics(*this);
}

CoreyQuadPhysics::CoreyQuadPhysics(const CoreyQuadPhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), copy.domain(), "CoreyQuad", _SIMPLE_ACCUMULATION_) {
}

CoreyQuadPhysics::~CoreyQuadPhysics() {

}

void CoreyQuadPhysics::setParams(vector<string> newParams) {

   
    RealVector fluxParamVector(13);

    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        double paramValue = atof(newParams[i].c_str());

        fluxParamVector.component(i) = paramValue;


    }


    CoreyQuad_Params newCoreyQuadParams(fluxParamVector);
    fluxFunction_->fluxParams(newCoreyQuadParams);

}