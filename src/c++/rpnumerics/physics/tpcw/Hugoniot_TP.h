#ifndef _HUGONIOT_TP_
#define _HUGONIOT_TP_


#include "ImplicitFunction.h"

#include <vector>
#include "ColorCurve.h"
#include "Curve.h"
#include "ColorCurve.h"
#include "ContourMethod.h"


class Hugoniot_TP : public ImplicitFunction{
private:
    RealVector Fref, Gref;
    RealVector Uref;

    const FluxFunction *ff;
    const AccumulationFunction *aa;
    


public:

    
    Hugoniot_TP(const FluxFunction * flux, const AccumulationFunction * accum);
    
    ~Hugoniot_TP();

    double complete_points(const RealVector &Uplus);

    int function_on_square(double *foncub, int i, int j);


    void curve(GridValues &, ReferencePoint &,int ,std::vector<HugoniotPolyLine> &hugoniot_curve,std::vector<RealVector> & transitionList);

    void curve(GridValues &, RealVector &,int ,std::vector<HugoniotPolyLine> &hugoniot_curve,std::vector<RealVector> & transitionList);

    void map(const RealVector &p, double &f, RealVector &map_Jacobian);

    bool improvable(void);
};

#endif // _HUGONIOT_TP_

