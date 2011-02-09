#ifndef _BUCKLEYLEVERETTINFLECTIONTPCW_
#define _BUCKLEYLEVERETTINFLECTIONTPCW_

#include "FracFlow2PhasesHorizontalAdimensionalized.h"
#include "HugoniotFunctionClass.h"
#include "RpNumerics.h"

class BuckleyLeverettinInflectionTPCW : public HugoniotFunctionClass {
private:
    FracFlow2PhasesHorizontalAdimensionalized *fh;
protected:
public:
    BuckleyLeverettinInflectionTPCW(FracFlow2PhasesHorizontalAdimensionalized *f);
    double HugoniotFunction(const RealVector &u);
    void completeCurve(std::vector<RealVector> &);
};

#endif // _BUCKLEYLEVERETTINFLECTIONTPCW_

