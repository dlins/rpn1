/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersivePhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "PolydispersivePhysics.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//PolydispersivePhysics::PolydispersivePhysics(const Polydispersive & polyFlux, const Boundary & boundary, const Space & space):SubPhysics(polyFlux,StoneAccumulation(),boundary,space,"Polydispersive",_SIMPLE_ACCUMULATION_){
//
//}

PolydispersivePhysics::PolydispersivePhysics() : SubPhysics(Polydispersive(Polydispersive_Params()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "Polydispersive", _SIMPLE_ACCUMULATION_) {
    RealVector refVec(2);
    PolydispersiveHugoniotFunction * stoneHugoniotFunction = new PolydispersiveHugoniotFunction(refVec, (Polydispersive (Polydispersive_Params())));
    setHugoniotFunction(stoneHugoniotFunction);

}

SubPhysics * PolydispersivePhysics::clone()const {

    return new PolydispersivePhysics(*this);
}

PolydispersivePhysics::PolydispersivePhysics(const PolydispersivePhysics & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), copy.domain(), "Polydispersive", _SIMPLE_ACCUMULATION_) {

    RealVector refVec(2);
    PolydispersiveHugoniotFunction * stoneHugoniotFunction = new PolydispersiveHugoniotFunction(refVec, (Polydispersive(Polydispersive_Params())));
    setHugoniotFunction(stoneHugoniotFunction);



}

PolydispersivePhysics::~PolydispersivePhysics() {

}