#ifndef _REDUCEDACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_
#define _REDUCEDACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_

#include <stdlib.h>

#include "AccumulationParams.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class ReducedAccum2Comp2PhasesAdimensionalized_Params : public AccumulationParams {
    private:

        Thermodynamics_SuperCO2_WaterAdimensionalized *TD_;
    protected:
    public:
        ReducedAccum2Comp2PhasesAdimensionalized_Params(Thermodynamics_SuperCO2_WaterAdimensionalized*, double);
        ~ReducedAccum2Comp2PhasesAdimensionalized_Params();

        Thermodynamics_SuperCO2_WaterAdimensionalized * get_thermodynamics(void) const;
};

#endif //_REDUCEDACCUM2COMP_PHASESADIMENSIONALIZED_PARAMS_

