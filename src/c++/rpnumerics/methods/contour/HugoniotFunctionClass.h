#ifndef _HUGONIOTFUNCTIONCLASS_
#define _HUGONIOTFUNCTIONCLASS_

#include "RealVector.h"
#include <vector>
class HugoniotFunctionClass {
private:
    RealVector * uRef_;
protected:
public:
    virtual double HugoniotFunction(const RealVector &u) = 0;
    virtual void completeCurve(std::vector<RealVector> &);
    RealVector & getReferenceVector();
    virtual void setReferenceVector(const RealVector &);
//    virtual HugoniotFunctionClass * clone()const = 0;
};

#endif // _HUGONIOTFUNCTIONCLASS_

