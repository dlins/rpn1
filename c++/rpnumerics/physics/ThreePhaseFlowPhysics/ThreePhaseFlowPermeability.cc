#include "ThreePhaseFlowPermeability.h"
#include "ThreePhaseFlowSubPhysics.h"

ThreePhaseFlowPermeability::ThreePhaseFlowPermeability(ThreePhaseFlowSubPhysics *s): AuxiliaryFunction(),
                                                                                     #ifdef JETTESTER_ENABLED_PERMEABILITY
                                                                                     TestableJet(),
                                                                                     #endif
                                                                                     subphysics_(s){
}

ThreePhaseFlowPermeability::~ThreePhaseFlowPermeability(){
}

