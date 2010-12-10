#include "ReducedAccum2Comp2PhasesAdimensionalized_Params.h"

ReducedAccum2Comp2PhasesAdimensionalized_Params::ReducedAccum2Comp2PhasesAdimensionalized_Params(Thermodynamics_SuperCO2_WaterAdimensionalized *TD, double *phi) : AccumulationParams(1, phi){
    vec = NULL;
    vec = new double[1];
//    vec[0] = phi;

//    AccumulationParams::AccumulationParams(1, vec);
//    component(0, phi);
    TD_ = TD;
}

ReducedAccum2Comp2PhasesAdimensionalized_Params::~ReducedAccum2Comp2PhasesAdimensionalized_Params(){
    if (vec != NULL) delete [] vec;
}

Thermodynamics_SuperCO2_WaterAdimensionalized * ReducedAccum2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

