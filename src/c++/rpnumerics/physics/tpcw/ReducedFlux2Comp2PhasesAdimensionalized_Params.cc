#include "ReducedFlux2Comp2PhasesAdimensionalized_Params.h"

ReducedFlux2Comp2PhasesAdimensionalized_Params::ReducedFlux2Comp2PhasesAdimensionalized_Params(double abs_perm, 
                                                 Thermodynamics_SuperCO2_WaterAdimensionalized *TD,
                                                 FracFlow2PhasesHorizontalAdimensionalized *FH) : FluxParams(3){
    component(0, abs_perm);

    TD_ = TD;
    FH_ = FH;
}

ReducedFlux2Comp2PhasesAdimensionalized_Params::~ReducedFlux2Comp2PhasesAdimensionalized_Params(){
}

Thermodynamics_SuperCO2_WaterAdimensionalized * ReducedFlux2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

FracFlow2PhasesHorizontalAdimensionalized * ReducedFlux2Comp2PhasesAdimensionalized_Params::get_horizontal(void) const {
    return FH_;
}

