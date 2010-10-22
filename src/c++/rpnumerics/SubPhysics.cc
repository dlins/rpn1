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

SubPhysics::SubPhysics(const FluxFunction & fluxFunction,const AccumulationFunction & accumulationFunction,const Boundary  & boundary):fluxFunction_((FluxFunction *)fluxFunction.clone()),accumulationFunction_((AccumulationFunction*)accumulationFunction.clone()),boundary_(boundary.clone()){

}