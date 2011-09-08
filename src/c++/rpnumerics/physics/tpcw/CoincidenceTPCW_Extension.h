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
                                    const Flux2Comp2PhasesAdimensionalized *dff, const Accum2Comp2PhasesAdimensionalized *daa,                           // For the domain.
//                                    FluxFunction *drff, AccumulationFunction *draa,                         // For the domain.
                                    int domain_family,                                                                  // For the domain.
                                    const Flux2Comp2PhasesAdimensionalized *curve_ff, const Accum2Comp2PhasesAdimensionalized *curve_aa,                 // For the curve.
//                                    FluxFunction *curve_reduced_ff, AccumulationFunction *curve_reduced_aa, // For the curve.
                                    int curve_family,                                                                   // For the curve.
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _COINCIDENCETPCW_EXTENSION_
 
