#ifndef _HYSTERESIS_
#define _HYSTERESIS_

#include "Extension_Curve.h"
#include "Inflection_Curve.h"
#include "Boundary.h"

// This class implements the Hysteresis, which is the extension 
// of the inflection curve.
//
class Hysteresis {
    private:
    protected:
    public:
        static int curve(Boundary *boundary, 
                         const FluxFunction *curve_flux, const AccumulationFunction *curve_accum,
                         int curve_family,
                         const RealVector &pmin, const RealVector &pmax, int *number_of_cells,         // For the domain.
                         int domain_family,
                         const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
                         int characteristic_where, int singular,
                         std::vector<RealVector> &curve_segments,
                         std::vector<RealVector> &domain_segments);
};

#endif // _HYSTERESIS_

