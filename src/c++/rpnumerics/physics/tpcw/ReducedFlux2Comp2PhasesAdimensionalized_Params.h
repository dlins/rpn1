#ifndef _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_PARAMS_
#define _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_PARAMS_

#include "FluxParams.h"

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"
#include "FracFlow2PhasesHorizontalAdimensionalized.h"

class ReducedFlux2Comp2PhasesAdimensionalized_Params : public FluxParams {
    private:
        double abs_perm;      // = 3e-12

        Thermodynamics_SuperCO2_WaterAdimensionalized *TD_;
        FracFlow2PhasesHorizontalAdimensionalized     *FH_;
    protected:
    public:
        ReducedFlux2Comp2PhasesAdimensionalized_Params(double abs_perm,
                                Thermodynamics_SuperCO2_WaterAdimensionalized *TD,
                                FracFlow2PhasesHorizontalAdimensionalized *FH);
        virtual ~ReducedFlux2Comp2PhasesAdimensionalized_Params();

        Thermodynamics_SuperCO2_WaterAdimensionalized * get_thermodynamics(void) const;
        FracFlow2PhasesHorizontalAdimensionalized     * get_horizontal(void) const;
};

#endif // _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_PARAMS_

