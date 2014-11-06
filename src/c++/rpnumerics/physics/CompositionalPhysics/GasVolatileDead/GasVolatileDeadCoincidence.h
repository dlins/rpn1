#ifndef _GASVOLATILEDEADCOINCIDENCE_
#define _GASVOLATILEDEADCOINCIDENCE_

#include "Coincidence.h"
#include "GasVolatileDeadThermodynamics.h"
#include "GasVolatileDeadHydrodynamics.h"

class GasVolatileDeadCoincidence : public Coincidence {
    private:
    protected:
        GasVolatileDeadThermodynamics *thermo;
        GasVolatileDeadHydrodynamics  *hydro;

        Parameter *re_parameter, *rg_parameter, *phi_parameter;
    public:
        GasVolatileDeadCoincidence(GasVolatileDeadThermodynamics *th, 
                                   GasVolatileDeadHydrodynamics *hy, 
                                   Parameter *re, Parameter *rg, 
                                   Parameter *phi);

        virtual ~GasVolatileDeadCoincidence();

        void lambdas(const RealVector &p, double &lambda_s, double &lambda_e, double &lambda_diff) const;

        double lambda_s(const RealVector &p) const;
        double lambda_e(const RealVector &p) const;
        double lambda_diff(const RealVector &p) const;

        // For the Extension Map:
        //
        bool extension_basis(const RealVector &u, double &fe, double &se) const;

        // To be used by GasVolatileDeadEvaporationExtension.
        //
        GasVolatileDeadThermodynamics* thermodynamics(){return thermo;}
};

#endif // _GASVOLATILEDEADCOINCIDENCE_

