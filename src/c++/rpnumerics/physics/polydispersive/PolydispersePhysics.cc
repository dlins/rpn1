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


//PolydispersePhysics::PolydispersePhysics(const Polydisperse & polyFlux, const Boundary & boundary, const Space & space):SubPhysics(polyFlux,StoneAccumulation(),boundary,space,"Polydisperse",_SIMPLE_ACCUMULATION_){
//
//}

PolydispersePhysics::PolydispersePhysics() : SubPhysics(Polydisperse(Polydisperse_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "Polydisperse", _SIMPLE_ACCUMULATION_) {
    RealVector refVec(2);
    PolydisperseHugoniotFunction * stoneHugoniotFunction = new PolydisperseHugoniotFunction(refVec, (Polydisperse (Polydisperse_Params())));
    setHugoniotFunction(stoneHugoniotFunction);

}

SubPhysics * PolydispersePhysics::clone()const {

    return new PolydispersePhysics(*this);
}

PolydispersePhysics::PolydispersePhysics(const PolydispersePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), copy.domain(), "Polydisperse", _SIMPLE_ACCUMULATION_) {

    RealVector refVec(2);
    PolydisperseHugoniotFunction * stoneHugoniotFunction = new PolydisperseHugoniotFunction(refVec, (Polydisperse(Polydisperse_Params())));
    setHugoniotFunction(stoneHugoniotFunction);



}

PolydispersePhysics::~PolydispersePhysics() {

}
