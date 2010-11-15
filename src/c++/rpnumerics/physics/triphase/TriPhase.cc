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
SubPhysics(TriPhaseFluxFunction(params, permParams, capilParams, viscParams), TriPhaseAccumulationFunction(), *defaultBoundary(),Multid::PLANE,"TriPhase",_SIMPLE_ACCUMULATION_) {



}

Boundary * TriPhase::defaultBoundary() const{

    RealVector A(2);

    A.component(0) = 0;
    A.component(1) = 0;

    RealVector B(2);

    B.component(0) = 0;
    B.component(1) = 1;

    RealVector C(2);

    C.component(0) = 1;
    C.component(1) = 0;


    return new IsoTriang2DBoundary(A, B, C);


}

TriPhase::TriPhase(const TriPhase & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(),Multid::PLANE,"TriPhase",_SIMPLE_ACCUMULATION_) {



}

SubPhysics * TriPhase::clone()const {
    return new TriPhase(*this);
}

TriPhase::~TriPhase() {

}




