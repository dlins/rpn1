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
#include "Hugoniot_Locus.h"

#include <vector>
#include <deque>

class Hugoniot_Curve : public Hugoniot_Locus {
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

        // For classification of segmented curves
        int classified_curve(const FluxFunction *f, const AccumulationFunction *a, 
                             GridValues &g, const RealVector &r, 
                             std::vector<HugoniotPolyLine> &hugoniot_curve);

        // For classification of continuous curves
//        int classified_curve(const FluxFunction *f, const AccumulationFunction *a, 
//                             GridValues &g, const RealVector &r, 
//                             std::vector<HugoniotPolyLine> &hugoniot_curve,
//                             std::vector<bool> &circular);
        
        
        int classified_curve(const FluxFunction *f, const AccumulationFunction *a,
                                     GridValues &g, const RealVector &r,
                                     std::vector<HugoniotPolyLine> &hugoniot_curve, std::vector<RealVector> &transitionList,
                                     std::vector<bool> &circular) ;
        
        

        int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, const RealVector &r,
                  std::vector<RealVector> &hugoniot_curve,
                  std::vector< std::deque <RealVector> > &curves, std::vector <bool> &is_circular,
                  const int method);
        
        int curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, const RealVector &r,
            std::vector<RealVector> &hugoniot_curve);
        

        void map(const RealVector &p, double &f, RealVector &map_Jacobian);

        bool improvable(void);
};

#endif // _HUGONIOT_CURVE_

