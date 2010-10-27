/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) SubPhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

SubPhysics::SubPhysics(const FluxFunction & fluxFunction, const AccumulationFunction & accumulationFunction, const Boundary & boundary, const Space & space, const char * id) : fluxFunction_((FluxFunction *) fluxFunction.clone()), accumulationFunction_((AccumulationFunction*) accumulationFunction.clone()), boundary_(boundary.clone()), space_(new Space(space)), ID_(id) {



}

const Space &SubPhysics::domain() const {
    return *space_;
}

const char * SubPhysics::ID() const {
    return ID_;
}

const Boundary & SubPhysics::boundary() const {
    return *boundary_;


}

const FluxFunction & SubPhysics::fluxFunction() const {
    return *fluxFunction_;
}

const AccumulationFunction & SubPhysics::accumulation() const {
    return *accumulationFunction_;
}

void SubPhysics::fluxParams(const FluxParams & newFluxParams) {
    fluxFunction_->fluxParams(newFluxParams);
}

void SubPhysics::accumulationParams(const AccumulationParams & newAccumulationParams){

    accumulationFunction_->accumulationParams(newAccumulationParams);
}
