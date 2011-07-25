#ifndef _ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_
#define _ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_

#include <stdlib.h>

#include "AccumulationParams.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class Accum2Comp2PhasesAdimensionalized_Params : public AccumulationParams {
private:
    Thermodynamics_SuperCO2_WaterAdimensionalized *TD_;
protected:
public:
    Accum2Comp2PhasesAdimensionalized_Params(const Thermodynamics_SuperCO2_WaterAdimensionalized &, double);
    virtual ~Accum2Comp2PhasesAdimensionalized_Params();

    Thermodynamics_SuperCO2_WaterAdimensionalized * get_thermodynamics(void) const;
    double getPhi();
};

#endif //_ACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_

