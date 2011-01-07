#ifndef _HUGONIOTFUNCTIONCLASS_
#define _HUGONIOTFUNCTIONCLASS_

#include "RealVector.h"
#include <vector>
class HugoniotFunctionClass {
private:
protected:
public:
    virtual double HugoniotFunction(const RealVector &u) = 0;
    virtual void completeCurve(std::vector<RealVector> &);
};

#endif // _HUGONIOTFUNCTIONCLASS_

