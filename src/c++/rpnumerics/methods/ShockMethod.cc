/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockMethod.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ShockMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//ShockMethod::ShockMethod(int dimension, const FluxFunction & fluxFunction, const AccumulationFunction & accFunction,const Boundary & boundary):
//dimension_(dimension),
//fluxFunction_((FluxFunction*)fluxFunction.clone()),
//accFunction_((AccumulationFunction *) accFunction.clone()),
//boundary_(boundary.clone()),
//sorter_(new ColorCurve(fluxFunction,accFunction)){
//
//}

const FluxFunction & ShockMethod::fluxFunction()const{return *fluxFunction_;}

const AccumulationFunction & ShockMethod::accumulationFunction()const{return *accFunction_;}

int ShockMethod::dimension()const {return dimension_;}

const Boundary & ShockMethod::boundary()const {return *boundary_;}

//ShockMethod::~ShockMethod() {
//    delete boundary_;
//    delete fluxFunction_;
//    delete accFunction_;
//    delete sorter_;
//}


