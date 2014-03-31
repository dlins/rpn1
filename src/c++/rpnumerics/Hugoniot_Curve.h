#ifndef _HUGONIOT_CURVE_
#define _HUGONIOT_CURVE_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "DoubleMatrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "ColorCurve.h"
#include "GridValues.h"
#include "Hugoniot_Locus.h"

#include "Newton_Improvement.h"

#include <vector>
#include <deque>

class Hugoniot_Curve : public Hugoniot_Locus {
    private:
        DoubleMatrix JFref, JGref;
        RealVector Fref, Gref;



    protected:
        
        const FluxFunction *ff;
        const AccumulationFunction *aa;
        
        
    public:
        Hugoniot_Curve(const FluxFunction *, const AccumulationFunction *);
        ~Hugoniot_Curve();

        int function_on_square(double *foncub, int i, int j);

        // For classification of segmented curves
        int classified_curve(GridValues &, ReferencePoint &,std::vector<HugoniotPolyLine> & ,std::vector<RealVector> &);

        int complete(const RealVector &p0, const RealVector &p1, const RealVector &p_init, RealVector &p_completed);

        void map(const RealVector &p, double &f, RealVector &map_Jacobian);

        bool improvable(void);
};

#endif // _HUGONIOT_CURVE_

