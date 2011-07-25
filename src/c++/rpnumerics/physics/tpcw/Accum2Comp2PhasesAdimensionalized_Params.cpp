#include "Accum2Comp2PhasesAdimensionalized_Params.h"

Accum2Comp2PhasesAdimensionalized_Params::Accum2Comp2PhasesAdimensionalized_Params(const Thermodynamics_SuperCO2_WaterAdimensionalized & TD, double phi) : AccumulationParams(1,new double [1]) {
    component(0,phi);
    TD_ = new Thermodynamics_SuperCO2_WaterAdimensionalized(TD);

}

Accum2Comp2PhasesAdimensionalized_Params::~Accum2Comp2PhasesAdimensionalized_Params() {

    delete TD_;
}

Thermodynamics_SuperCO2_WaterAdimensionalized * Accum2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

double Accum2Comp2PhasesAdimensionalized_Params::getPhi() {
    return component(0);
}