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
# include "Quad2AccumulationFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


PolydispersivePhysics::PolydispersivePhysics(const Polydispersive & polyFlux, const Boundary & boundary, const Space & space):SubPhysics(polyFlux,Quad2AccumulationFunction(),boundary,space,"Polydispersive",_SIMPLE_ACCUMULATION_){

}

PolydispersivePhysics::PolydispersivePhysics():SubPhysics(Polydispersive(Polydispersive_Params()),Quad2AccumulationFunction(),*defaultBoundary(),Multid::PLANE,"Polydispersive",_SIMPLE_ACCUMULATION_){


}

 SubPhysics * PolydispersivePhysics::clone()const{

     return new PolydispersivePhysics(*this);
 }

PolydispersivePhysics::PolydispersivePhysics(const PolydispersivePhysics & copy ):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),copy.domain(),"Polydispersive",_SIMPLE_ACCUMULATION_){


}


PolydispersivePhysics::~PolydispersivePhysics(){

}