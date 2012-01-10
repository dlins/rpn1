#ifndef _HUGONIOT_CURVE_
#define _HUGONIOT_CURVE_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "ColorCurve.h"

class Hugoniot_Curve : public ImplicitFunction {
private:
    const FluxFunction *ff;
    const AccumulationFunction *aa;

    // For the grid proper.
    RealVector pmin, pmax, Uref;
    double *Fref, *Gref;
    double *JFref, *JGref;

    int *number_of_cells;

    // Values on the grid.
    Matrix<RealVector> grid;
    Matrix<RealVector> F_on_grid;
    Matrix<RealVector> G_on_grid;

    void hc_fill_values_on_grid(void);

    void fill_with_jet(const RpFunction *flux_object, int n, double *in, int degree, double *F, double *J, double *H);
protected:
public:
    Hugoniot_Curve(const FluxFunction *f, const AccumulationFunction *a,
            const RealVector &min, const RealVector &max,
            const int *cells,
            const RealVector &ref);
    ~Hugoniot_Curve();

    void set_reference_point(const RealVector &ref);

    int function_on_square(double *foncub, int i, int j, int is_square);

    int curve(std::vector<RealVector> &hugoniot_curve);

    int classified_curve(std::vector<HugoniotPolyLine> &hugoniot_curve);


    void map(const RealVector &p, double &f, RealVector &map_Jacobian);

    bool improvable(void);
};

#endif // _HUGONIOT_CURVE_

