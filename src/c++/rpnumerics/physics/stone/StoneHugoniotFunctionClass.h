#ifndef _STONEHUGONIOTFUNCTIONCLASS_
#define _STONEHUGONIOTFUNCTIONCLASS_

#include "StoneFluxFunction.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

class StoneHugoniotFunctionClass : public HugoniotFunctionClass {
    private:
//        StoneFluxFunction *stone;

        RealVector Uref;
        JetMatrix  UrefJetMatrix;

        std::vector<eigenpair> ve_uref;

        bool Uref_is_elliptic;
    protected:
    public:
        StoneHugoniotFunctionClass(const RealVector &U, const StoneFluxFunction &);
        void setReferenceVector(const RealVector & refVec);
        ~StoneHugoniotFunctionClass();

        double HugoniotFunction(const RealVector &u);
};

#endif // _STONEHUGONIOTFUNCTIONCLASS_

