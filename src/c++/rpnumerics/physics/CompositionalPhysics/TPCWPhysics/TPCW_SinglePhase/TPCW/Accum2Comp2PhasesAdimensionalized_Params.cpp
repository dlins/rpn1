#include "Accum2Comp2PhasesAdimensionalized_Params.h"
#include <iostream>
Accum2Comp2PhasesAdimensionalized_Params::Accum2Comp2PhasesAdimensionalized_Params(Thermodynamics * TD, double phi) : AccumulationParams(RealVector(1)) {
    component(0,phi);
    TD_ = TD;
}

Accum2Comp2PhasesAdimensionalized_Params::Accum2Comp2PhasesAdimensionalized_Params(const Accum2Comp2PhasesAdimensionalized_Params & copy):AccumulationParams(copy.params()){
    TD_= copy.get_thermodynamics();
}


Accum2Comp2PhasesAdimensionalized_Params::~Accum2Comp2PhasesAdimensionalized_Params(){

}

Thermodynamics * Accum2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

double Accum2Comp2PhasesAdimensionalized_Params::getPhi() const{
    return params().component(0);
}
