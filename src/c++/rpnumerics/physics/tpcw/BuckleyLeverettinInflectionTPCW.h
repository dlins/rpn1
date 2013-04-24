#ifndef _BUCKLEYLEVERETTINFLECTIONTPCW_
#define _BUCKLEYLEVERETTINFLECTIONTPCW_


#include "HugoniotFunctionClass.h"
#include "RectBoundary.h"
#include "Flux2Comp2PhasesAdimensionalized.h"

class BuckleyLeverettinInflectionTPCW : public HugoniotFunctionClass {
private:
    Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized *fh;
    RectBoundary * boundary_;
protected:
public:
    BuckleyLeverettinInflectionTPCW(Flux2Comp2PhasesAdimensionalized *f,RectBoundary *);
    double HugoniotFunction(const RealVector &u);
    void completeCurve(std::vector<RealVector> &);
};

#endif // _BUCKLEYLEVERETTINFLECTIONTPCW_

