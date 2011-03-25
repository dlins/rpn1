#ifndef _COINCIDENCETPCW_EXTENSION_
#define _COINCIDENCETPCW_EXTENSION_

#include "Extension_CurveTPCW.h"
#include "CoincidenceTPCW.h"
#include "ContourMethod.h"
#include "RectBoundary.h"

class CoincidenceTPCW_Extension {
    private:
    protected:
    public:
        static void extension_curve(CoincidenceTPCW *coincidencetpcw, 
                                    RealVector &pmin, RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                    FluxFunction *dff, AccumulationFunction *daa,                           // For the domain.
                                    FluxFunction *drff, AccumulationFunction *draa,                         // For the domain.
                                    int domain_family,                                                                  // For the domain.
                                    FluxFunction *curve_ff, AccumulationFunction *curve_aa,                 // For the curve.
                                    FluxFunction *curve_reduced_ff, AccumulationFunction *curve_reduced_aa, // For the curve.
                                    int curve_family,                                                                   // For the curve.
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _COINCIDENCETPCW_EXTENSION_
 
