/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Stone.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Stone.h"
#include "TriPhaseAccumulationFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Boundary * Stone::defaultBoundary() const{

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

Stone::Stone():SubPhysics(StoneFluxFunction(StoneParams(),StonePermParams()),TriPhaseAccumulationFunction(),*defaultBoundary(),Multid::PLANE,"Stone",_SIMPLE_ACCUMULATION_){

}

Stone::Stone(const Stone & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),Multid::PLANE,"Stone",_SIMPLE_ACCUMULATION_) {

}

SubPhysics * Stone::clone()const {
    return new Stone(*this);
}

Stone::~Stone() {
}


