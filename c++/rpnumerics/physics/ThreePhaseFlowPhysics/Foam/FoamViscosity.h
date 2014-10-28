#ifndef _FOAMVISCOSITY_
#define _FOAMVISCOSITY_

#define VISCOSITY_OK    0
#define VISCOSITY_ERROR 1

#include "Parameter.h"
#include "WaveState.h"
#include "JetMatrix.h"

class FoamViscosity {
    private:
    protected:
        Parameter *mug_parameter; // mug0
    public:
        FoamViscosity(Parameter *mug);
        ~FoamViscosity();

        int gas_viscosity_jet(const WaveState &w, int degree, JetMatrix &mug_jet);
};

#endif // _FOAMVISCOSITY_

