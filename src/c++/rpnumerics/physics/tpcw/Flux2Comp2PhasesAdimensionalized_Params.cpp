#include "Flux2Comp2PhasesAdimensionalized_Params.h"

Flux2Comp2PhasesAdimensionalized_Params::Flux2Comp2PhasesAdimensionalized_Params(double abs_perm, double sin_beta, double const_gravity, 
                                                 bool has_gravity, 
                                                 bool has_horizontal,
                                                 Thermodynamics_SuperCO2_WaterAdimensionalized *TD,
                                                 FracFlow2PhasesHorizontalAdimensionalized *FH,
                                                 FracFlow2PhasesVerticalAdimensionalized *FV) : FluxParams(3){
    component(0, abs_perm);
    component(1, sin_beta);
    component(2, const_gravity);

    has_gravity_    = has_gravity;
    has_horizontal_ = has_horizontal;

    TD_ = TD;
    FH_ = FH;
    FV_ = FV;
}

Flux2Comp2PhasesAdimensionalized_Params::~Flux2Comp2PhasesAdimensionalized_Params(){
}

Thermodynamics_SuperCO2_WaterAdimensionalized * Flux2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

FracFlow2PhasesHorizontalAdimensionalized * Flux2Comp2PhasesAdimensionalized_Params::get_horizontal(void) const {
    return FH_;
}

FracFlow2PhasesVerticalAdimensionalized * Flux2Comp2PhasesAdimensionalized_Params::get_vertical(void) const {
    return FV_;
}

bool Flux2Comp2PhasesAdimensionalized_Params::has_gravity(void) const {
    return has_gravity_;
}

bool Flux2Comp2PhasesAdimensionalized_Params::has_horizontal(void) const {
    return has_horizontal_;
}

