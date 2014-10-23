#ifndef _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_
#define _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"

#include "AccumulationSinglePhaseLiquidAdimensionalized_Params.h"
#include "Thermodynamics.h"

class AccumulationSinglePhaseLiquidAdimensionalized : public AccumulationFunction {
    private:
        double phi; // Porosity

        // Thermodynamics
        Thermodynamics *thermo;
    protected:
    public:
        AccumulationSinglePhaseLiquidAdimensionalized(const AccumulationSinglePhaseLiquidAdimensionalized &);
        AccumulationSinglePhaseLiquidAdimensionalized(const AccumulationSinglePhaseLiquidAdimensionalized_Params &, Thermodynamics *t);
        AccumulationSinglePhaseLiquidAdimensionalized * clone() const;

        ~AccumulationSinglePhaseLiquidAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _ACCUMULATIONSINGLEPHASELIQUIDADIMENSIONALIZED_

