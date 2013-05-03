#ifndef _SUBINFLECTIONTPCW_EXTENSION_
#define _SUBINFLECTIONTPCW_EXTENSION_

#include "Extension_CurveTPCW.h"
#include "RectBoundary.h"
#include "SubinflectionTPCW.h"
#include "ContourMethod.h"

class SubinflectionTPCW_Extension {
private:
protected:
public:
    static void extension_curve(SubinflectionTPCW *Subinflectiontpcw,
            RealVector &pmin, RealVector &pmax, int *number_of_grid_points, // For the domain.
            const Flux2Comp2PhasesAdimensionalized *, const Accum2Comp2PhasesAdimensionalized *, // For the domain.
            int domain_family, // For the domain.
            const Flux2Comp2PhasesAdimensionalized *, const Accum2Comp2PhasesAdimensionalized *, // For the curve.
            int curve_family, // For the curve.
            int characteristic_where, int singular,
            std::vector<RealVector> &curve_segments,
            std::vector<RealVector> &domain_segments);
};

#endif // _SUBINFLECTIONTPCW_EXTENSION_

