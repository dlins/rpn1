#ifndef _JDFLUXFUNCTION_
#define _JDFLUXFUNCTION_

#include "FluxFunction.h"

class JDFluxFunction : public FluxFunction {
    private:
    protected:
        double epsilon;
    public:
        JDFluxFunction();
        JDFluxFunction(double e);

        virtual ~JDFluxFunction();

        int jet(const WaveState &w, JetMatrix &f, int degree) const;
        JDFluxFunction * clone() const;

        // For the coincidence:
        //
        double alpha_dot(const RealVector &p) const;

        // Temporal.
        //
        void set_epsilon(double e);
};

#endif // _JDFLUXFUNCTION_

