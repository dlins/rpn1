#ifndef _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_
#define _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "ReducedFlux2Comp2PhasesAdimensionalized_Params.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

#include "FracFlow2PhasesHorizontalAdimensionalized.h"
#include "FracFlow2PhasesVerticalAdimensionalized.h"


class ReducedFlux2Comp2PhasesAdimensionalized : public FluxFunction {
    private:
        // Fluid dynamics
        double abs_perm, sin_beta, const_gravity, grav; // Prune this

        // Thermodynamics
        Thermodynamics_SuperCO2_WaterAdimensionalized *TD;

        // FracFlows
        FracFlow2PhasesHorizontalAdimensionalized *FH;

//        void type(int);
    protected:
    public:
//      ReducedFlux2Comp2PhasesAdimensionalized(const ReducedFlux2Comp2PhasesAdimensionalized &);
        ReducedFlux2Comp2PhasesAdimensionalized(const ReducedFlux2Comp2PhasesAdimensionalized_Params &);
        ReducedFlux2Comp2PhasesAdimensionalized * clone() const;

       virtual ~ReducedFlux2Comp2PhasesAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _REDUCEDFLUX2COMP2PHASESADIMENSIONALIZED_

