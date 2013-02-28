#ifndef _TRIVIALACCUMULATION_
#define _TRIVIALACCUMULATION_

#include <stdio.h>
#include <stdlib.h>
#include "AccumulationFunction.h"

#include "TrivialAccumulation_Params.h"
#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"

class TrivialAccumulation : public AccumulationFunction {
    private:
        // Fluid dynamics
        double phi; // = 0.38;

        // Thermodynamics
        Thermodynamics_SuperCO2_WaterAdimensionalized *TD;
    protected:
    public:
        TrivialAccumulation(const TrivialAccumulation &);
        TrivialAccumulation(const TrivialAccumulation_Params &);
        TrivialAccumulation * clone() const;

        ~TrivialAccumulation();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _TRIVIALACCUMULATION_

