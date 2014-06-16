#ifndef _COINCIDENCE_TPCW_
#define _COINCIDENCE_TPCW_

#include "Coincidence.h"

#include "Thermodynamics.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

class Coincidence_TPCW : public Coincidence {
    private:
    protected:
        const Thermodynamics *td;
        const Flux2Comp2PhasesAdimensionalized *fluxFunction_;
        const Accum2Comp2PhasesAdimensionalized *accumFunction_;

        void engine(const RealVector &u, double &f, double &M, double &N1, double &s, double &N2, double &df0_du0) const;
    public:
        Coincidence_TPCW(const Flux2Comp2PhasesAdimensionalized *f, const Accum2Comp2PhasesAdimensionalized *a);
        ~Coincidence_TPCW();

        void lambdas(const RealVector &u, double &lambda_s, double &lambda_e, double &lambda_diff) const;

        double lambda_s(const RealVector &p) const;
        double lambda_e(const RealVector &p) const;
        double lambda_diff(const RealVector &p) const;

        bool extension_basis(const RealVector &u, double &fe, double &se) const;
};

#endif // _COINCIDENCE_TPCW_

