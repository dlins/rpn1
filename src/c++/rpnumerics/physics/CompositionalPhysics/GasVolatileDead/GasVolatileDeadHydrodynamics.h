#ifndef _GASVOLATILEDEADHYDRODYNAMICS_
#define _GASVOLATILEDEADHYDRODYNAMICS_

#include "AuxiliaryFunction.h"
#include "GasVolatileDeadThermodynamics.h"

class GasVolatileDeadHydrodynamics : public AuxiliaryFunction {
    private:
    protected:
        GasVolatileDeadThermodynamics *thermo;
    public:
        GasVolatileDeadHydrodynamics(GasVolatileDeadThermodynamics *t);
        virtual ~GasVolatileDeadHydrodynamics();

        void oil_fractional_flow(int degree, double s, double r, JetMatrix &fo);
        void fractional_flow(int degree, double s, double y, JetMatrix &f_jet);
};

#endif // _GASVOLATILEDEADHYDRODYNAMICS_

