#ifndef _ACCUM2COMP2PHASESADIMENSIONALIZED_
#define _ACCUM2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"

#include "Accum2Comp2PhasesAdimensionalized_Params.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class Accum2Comp2PhasesAdimensionalized : public AccumulationFunction {
    private:
        // Fluid dynamics
        double phi; // = 0.38;

        // Thermodynamics
        Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
    protected:
    public:
        Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized &);
        Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized_Params &);
        Accum2Comp2PhasesAdimensionalized * clone() const;

        virtual ~Accum2Comp2PhasesAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _ACCUM2COMP2PHASESADIMENSIONALIZED_

