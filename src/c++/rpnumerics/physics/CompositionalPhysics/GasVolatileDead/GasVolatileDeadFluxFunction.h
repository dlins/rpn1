#ifndef _GASVOLATILEDEADFLUXFUNCTION_
#define _GASVOLATILEDEADFLUXFUNCTION_

#include "FluxFunction.h"
#include "GasVolatileDeadThermodynamics.h"
#include "GasVolatileDeadHydrodynamics.h"

class GasVolatileDeadFluxFunction : public FluxFunction {
    private:
    protected:
        GasVolatileDeadThermodynamics *thermo;
        GasVolatileDeadHydrodynamics  *hydro;
    public:
        GasVolatileDeadFluxFunction(GasVolatileDeadThermodynamics *th, GasVolatileDeadHydrodynamics *hy);
        virtual ~GasVolatileDeadFluxFunction();

        int reduced_jet(const WaveState &u, JetMatrix &m, int degree) const;
        int reduced_jet(const RealVector &u, JetMatrix &m, int degree) const {
            WaveState w(u);

            int info = reduced_jet(w, m, degree);

            return info;
        }

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _GASVOLATILEDEADFLUXFUNCTION_

