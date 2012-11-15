#ifndef _BL_INFLECTION_TPCW_
#define _BL_INFLECTION_TPCW_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "GridValues.h"

#include "Flux2Comp2PhasesAdimensionalized.h"

class BLInflectionTP : public ImplicitFunction {
private:
    Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized *fh;
protected:
public:
//    BLInflectionTP(Flux2Comp2PhasesAdimensionalized *f,RectBoundary *);
    BLInflectionTP(){gv = 0;}
    ~BLInflectionTP();

    int function_on_square(double *foncub, int i, int j);

    int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, std::vector<RealVector> &BLinflection_curve);
};

#endif // _BL_INFLECTION_TPCW_

