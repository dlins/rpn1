#include "ThreePhaseFlowViscosity.h"
#include "ThreePhaseFlowSubPhysics.h"

ThreePhaseFlowViscosity::ThreePhaseFlowViscosity(ThreePhaseFlowSubPhysics *t): subphysics_(t){
}

ThreePhaseFlowViscosity::~ThreePhaseFlowViscosity(){
}

double ThreePhaseFlowViscosity::gas_viscosity(){
    return subphysics_->mug()->value();
}

