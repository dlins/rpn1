#ifndef _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_
#define _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"

#include "AccumulationSinglePhaseVaporAdimensionalized_Params.h"
#include "Thermodynamics.h"

class AccumulationSinglePhaseVaporAdimensionalized : public AccumulationFunction {
    private:
        double phi; // Porosity

        // Thermodynamics
        Thermodynamics *thermo;
    protected:
    public:
        AccumulationSinglePhaseVaporAdimensionalized(const AccumulationSinglePhaseVaporAdimensionalized &);
        AccumulationSinglePhaseVaporAdimensionalized(const AccumulationSinglePhaseVaporAdimensionalized_Params &, Thermodynamics *t);
        AccumulationSinglePhaseVaporAdimensionalized * clone() const;

        ~AccumulationSinglePhaseVaporAdimensionalized();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _ACCUMULATIONSINGLEPHASEVAPORADIMENSIONALIZED_

