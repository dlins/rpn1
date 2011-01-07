#ifndef _COINCIDENCETPCW_
#define _COINCIDENCETPCW_

#include <stdio.h>
#include "HugoniotFunctionClass.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"
#include "FracFlow2PhasesHorizontalAdimensionalized.h"

class CoincidenceTPCW : public HugoniotFunctionClass {
    private:
        Thermodynamics_SuperCO2_WaterAdimensionalized *td;
        FracFlow2PhasesHorizontalAdimensionalized     *fh;
        double                                        phi;

        double lambdas_function(const RealVector &u);
        double lambdae_function(const RealVector &u);
    protected:
    public:
        CoincidenceTPCW(Thermodynamics_SuperCO2_WaterAdimensionalized *t, FracFlow2PhasesHorizontalAdimensionalized *f, double phi_);
        double HugoniotFunction(const RealVector &u);
};

#endif // _COINCIDENCETPCW_

