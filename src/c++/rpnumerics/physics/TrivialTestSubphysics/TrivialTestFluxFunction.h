#ifndef _TRIVIALTESTFLUXFUNCTION_
#define _TRIVIALTESTFLUXFUNCTION_

#include "FluxFunction.h"

class TrivialTestFluxFunction : public FluxFunction {
    private:
    protected:
    public:
        TrivialTestFluxFunction();
        virtual ~TrivialTestFluxFunction();
        
        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _TRIVIALTESTFLUXFUNCTION_
