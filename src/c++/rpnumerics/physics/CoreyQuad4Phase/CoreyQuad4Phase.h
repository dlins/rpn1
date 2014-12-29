#ifndef _COREY_QUAD_4PHASE_
#define _COREY_QUAD_4PHASE_

#include "FluxFunction.h"
#include "CoreyQuad4Phase_Params.h"

class CoreyQuad4Phase : public FluxFunction {
    private:
    protected:
    public:
        CoreyQuad4Phase(const CoreyQuad4Phase_Params &);
        CoreyQuad4Phase(const CoreyQuad4Phase &);
        CoreyQuad4Phase * clone() const;

        ~CoreyQuad4Phase();

        int jet(const WaveState &u, JetMatrix &m, int degree) const;
};

#endif // _COREY_QUAD_4PHASE_
