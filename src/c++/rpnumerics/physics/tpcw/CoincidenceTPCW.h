#ifndef _COINCIDENCETPCW_
#define _COINCIDENCETPCW_

#include <stdio.h>
#include "HugoniotFunctionClass.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

class CoincidenceTPCW : public HugoniotFunctionClass {
private:
   const Thermodynamics_SuperCO2_WaterAdimensionalized *td;
//    const Flux2Comp2PhasesAdimensionalized *fluxFunction_;
    double phi;

    double lambdas_function(const RealVector &u);
    double lambdae_function(const RealVector &u);
protected:
public:
    CoincidenceTPCW(const Flux2Comp2PhasesAdimensionalized *,const Accum2Comp2PhasesAdimensionalized *);
    double HugoniotFunction(const RealVector &u);

    void completeCurve(std::vector<RealVector> &);
};

#endif // _COINCIDENCETPCW_

