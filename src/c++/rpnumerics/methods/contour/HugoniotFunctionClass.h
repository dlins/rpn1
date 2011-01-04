#ifndef _HUGONIOTFUNCTIONCLASS_
#define _HUGONIOTFUNCTIONCLASS_

#include "RealVector.h"

class HugoniotFunctionClass {
    private:
    protected:
    public:
        virtual double HugoniotFunction(const RealVector &u) = 0;
//        virtual HugoniotFunctionClass * clone()=0;
};

#endif // _HUGONIOTFUNCTIONCLASS_

