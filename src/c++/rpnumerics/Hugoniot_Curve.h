#ifndef _HUGONIOT_CURVE_
#define _HUGONIOT_CURVE_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "ColorCurve.h"
#include "GridValues.h"

class Hugoniot_Curve : public ImplicitFunction {
    private:
        Matrix<double> JFref, JGref;
        RealVector Fref, Gref;

        const FluxFunction *ff;
        const AccumulationFunction *aa;
    protected:
    public:
        Hugoniot_Curve(){gv = 0;}
        ~Hugoniot_Curve();

        int function_on_square(double *foncub, int i, int j);

        int classified_curve(const FluxFunction *f, const AccumulationFunction *a, 
                             GridValues &g, const RealVector &r, 
                             std::vector<HugoniotPolyLine> &hugoniot_curve);

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, const RealVector &r,
                  std::vector<RealVector> &hugoniot_curve);

        void map(const RealVector &p, double &f, RealVector &map_Jacobian);

        bool improvable(void);
};

#endif // _HUGONIOT_CURVE_

