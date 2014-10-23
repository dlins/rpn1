#ifndef _GASVOLATILEDEADACCUMULATIONFUNCTION_
#define _GASVOLATILEDEADACCUMULATIONFUNCTION_

#include "AccumulationFunction.h"
#include "GasVolatileDeadThermodynamics.h"

class GasVolatileDeadAccumulationFunction : public AccumulationFunction {
    private:
    protected:
        GasVolatileDeadThermodynamics *thermo;
        
        Parameter *phi_parameter;
    public:
        GasVolatileDeadAccumulationFunction(Parameter *phi, GasVolatileDeadThermodynamics *th);
        virtual ~GasVolatileDeadAccumulationFunction();

        int reduced_jet(const WaveState &state, JetMatrix &m, int degree) const;
        int reduced_jet(const RealVector &u, JetMatrix &m, int degree) const {
            WaveState w(u);

            int info = reduced_jet(w, m, degree);

            return info;
        }

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _GASVOLATILEDEADACCUMULATIONFUNCTION_

