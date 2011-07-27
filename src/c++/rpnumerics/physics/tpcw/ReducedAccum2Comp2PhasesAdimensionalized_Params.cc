#include "ReducedAccum2Comp2PhasesAdimensionalized_Params.h"

ReducedAccum2Comp2PhasesAdimensionalized_Params::ReducedAccum2Comp2PhasesAdimensionalized_Params(Thermodynamics_SuperCO2_WaterAdimensionalized *TD, double phi) : AccumulationParams(RealVector(1)){
  
    component(0, phi);
    TD_ = TD;
}

ReducedAccum2Comp2PhasesAdimensionalized_Params::~ReducedAccum2Comp2PhasesAdimensionalized_Params(){

}

Thermodynamics_SuperCO2_WaterAdimensionalized * ReducedAccum2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

