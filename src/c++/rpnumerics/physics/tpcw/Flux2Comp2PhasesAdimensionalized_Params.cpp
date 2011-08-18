#include "Flux2Comp2PhasesAdimensionalized_Params.h"



Flux2Comp2PhasesAdimensionalized_Params::Flux2Comp2PhasesAdimensionalized_Params(const RealVector & paramsVector,Thermodynamics_SuperCO2_WaterAdimensionalized * TD ):FluxParams(paramsVector){
  TD_ = TD;
  if (paramsVector.component(3)==1.0)
      has_gravity_=true;
  else
      has_gravity_=false;

  if(paramsVector.component(4)==1.0)
      has_horizontal_=true;
  else
      has_horizontal_=false;
}

//Flux2Comp2PhasesAdimensionalized_Params::Flux2Comp2PhasesAdimensionalized_Params(double abs_perm, double sin_beta, double const_gravity,
//        bool has_gravity,
//        bool has_horizontal,
//        Thermodynamics_SuperCO2_WaterAdimensionalized *TD) : FluxParams(RealVector(3)) {
//
//    component(0, abs_perm);
//    component(1, sin_beta);
//    component(2, const_gravity);
//
//    has_gravity_ = has_gravity;
//    has_horizontal_ = has_horizontal;
//
//
//
//}

Flux2Comp2PhasesAdimensionalized_Params::~Flux2Comp2PhasesAdimensionalized_Params() {

}

Flux2Comp2PhasesAdimensionalized_Params::Flux2Comp2PhasesAdimensionalized_Params(const Flux2Comp2PhasesAdimensionalized_Params & copy) : FluxParams(RealVector(3)) {
    component(0, copy.abs_perm);
    component(1, copy.sin_beta);
    component(2, copy.const_gravity);
    TD_ = copy.TD_;
    cout << "copy" << endl;
}

Thermodynamics_SuperCO2_WaterAdimensionalized * Flux2Comp2PhasesAdimensionalized_Params::get_thermodynamics(void) const {
    return TD_;
}

bool Flux2Comp2PhasesAdimensionalized_Params::has_gravity(void) const {
    return has_gravity_;
}

bool Flux2Comp2PhasesAdimensionalized_Params::has_horizontal(void) const {
    return has_horizontal_;
}

