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

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//! Code comes here! daniel@impa.br

IsoTriang2DBoundary * Stone::defaultBoundary() {

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

void Stone::boundary(const Boundary & boundary) {

    delete boundary_;

    boundary_ = boundary.clone();

}

Stone::Stone(const Stone & copy) {

    fluxFunction_ = (FluxFunction *) copy.fluxFunction().clone();

    boundary_ = copy.boundary().clone();

    accFunction_ = (AccumulationFunction *) copy.accumulation().clone();

    FLUX_ID = "Stone";


}

const char * Stone::ID()const {
    return FLUX_ID;
}

Physics * Stone::clone()const {
    return new Stone(*this);
}

Stone::~Stone() {

    delete fluxFunction_;
    delete boundary_;
    delete accFunction_;

}