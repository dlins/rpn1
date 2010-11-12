#ifndef _FLUX2COMP2PHASESADIMENSIONALIZED_
#define _FLUX2COMP2PHASESADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "Flux2Comp2PhasesAdimensionalized_Params.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

#include "FracFlow2PhasesHorizontalAdimensionalized.h"
#include "FracFlow2PhasesVerticalAdimensionalized.h"

//#define FLUX2COMP2PHASESADIMENSIONALIZED_PURE_GRAVITY 0
//#define FLUX2COMP2PHASESADIMENSIONALIZED_GRAVITY      1
//#define FLUX2COMP2PHASESADIMENSIONALIZED_HORIZONTAL   2

class Flux2Comp2PhasesAdimensionalized : public FluxFunction {
private:
    // Fluid dynamics
    double abs_perm, sin_beta, const_gravity, grav;

    bool has_gravity;
    bool has_horizontal;

    // Thermodynamics
    Thermodynamics_SuperCO2_WaterAdimensionalized *TD;

    // FracFlows
    FracFlow2PhasesHorizontalAdimensionalized *FH;
    FracFlow2PhasesVerticalAdimensionalized *FV;

    //        void type(int);
protected:
public:
    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &);
    Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &);
    Flux2Comp2PhasesAdimensionalized * clone() const;

    virtual ~Flux2Comp2PhasesAdimensionalized();

    int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _FLUX2COMP2PHASESADIMENSIONALIZED_

