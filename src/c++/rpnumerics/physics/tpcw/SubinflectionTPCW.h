#ifndef _SUBINFLECTIONTPCW_
#define _SUBINFLECTIONTPCW_

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"
#include "FracFlow2PhasesHorizontalAdimensionalized.h"
#include "HugoniotFunctionClass.h"

class SubinflectionTPCW : public HugoniotFunctionClass {
    private:
        Thermodynamics_SuperCO2_WaterAdimensionalized *td;
        FracFlow2PhasesHorizontalAdimensionalized     *fh;
        double                                        phi;

        void subinflection_function(double  & reduced_lambdae, double & numeratorchiu, double & denominatorchiu, const RealVector &u);
    protected:
    public:
        SubinflectionTPCW(Thermodynamics_SuperCO2_WaterAdimensionalized *t, FracFlow2PhasesHorizontalAdimensionalized *f, double phi_);
        double HugoniotFunction(const RealVector &u);
};

#endif // _SUBINFLECTIONTPCW_

