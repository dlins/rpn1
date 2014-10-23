#ifndef _FLUXSINGLEPHASELIQUIDADIMENSIONALIZED_
#define _FLUXSINGLEPHASELIQUIDADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "FluxFunction.h"

#include "FluxSinglePhaseLiquidAdimensionalized_Params.h"
#include "Thermodynamics.h"

class FluxSinglePhaseLiquidAdimensionalized : public FluxFunction {
    private:
        // Thermodynamics
        Thermodynamics *thermo;
    protected:
    public:
        FluxSinglePhaseLiquidAdimensionalized(const FluxSinglePhaseLiquidAdimensionalized &);
        FluxSinglePhaseLiquidAdimensionalized(const FluxSinglePhaseLiquidAdimensionalized_Params &, Thermodynamics *t);
        FluxSinglePhaseLiquidAdimensionalized * clone() const;

        ~FluxSinglePhaseLiquidAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _FLUXSINGLEPHASELIQUIDADIMENSIONALIZED_

