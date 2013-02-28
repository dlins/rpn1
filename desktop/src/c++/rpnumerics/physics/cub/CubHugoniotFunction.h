#ifndef _CUBHUGONIOTFUNCTION_
#define _CUBHUGONIOTFUNCTION_

#include "Cub2FluxFunction.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

class CubHugoniotFunction : public HugoniotFunctionClass {
    private:

        RealVector Uref;
        JetMatrix  UrefJetMatrix;

        std::vector<eigenpair> ve_uref;

        bool Uref_is_elliptic;
    protected:
    public:
        CubHugoniotFunction(const RealVector &U, const Cub2FluxFunction &);
        void setReferenceVector(const RealVector & refVec);
        ~CubHugoniotFunction();

        double HugoniotFunction(const RealVector &u);
};

#endif // _CUBHUGONIOTFUNCTION_

