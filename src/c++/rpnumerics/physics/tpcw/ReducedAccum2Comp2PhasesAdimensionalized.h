#ifndef _REDUCEDACCUM2COMP2PHASESADIMENSIONALIZED_
#define _REDUCEDACCUM2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"

#include "ReducedAccum2Comp2PhasesAdimensionalized_Params.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class ReducedAccum2Comp2PhasesAdimensionalized : public AccumulationFunction {
    private:
        // Fluid dynamics
        double phi; // = 0.38;

        // Thermodynamics
        Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
    protected:
    public:
        ReducedAccum2Comp2PhasesAdimensionalized(const ReducedAccum2Comp2PhasesAdimensionalized &);
        ReducedAccum2Comp2PhasesAdimensionalized(const ReducedAccum2Comp2PhasesAdimensionalized_Params &);
        ReducedAccum2Comp2PhasesAdimensionalized * clone() const;

        ~ReducedAccum2Comp2PhasesAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const; // ONLY DEGREE 0
};

#endif // _REDUCEDACCUM2COMP2PHASESADIMENSIONALIZED_

