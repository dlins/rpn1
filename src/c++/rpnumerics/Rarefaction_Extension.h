#ifndef _RAREFACTION_EXTENSIONTPCW_
#define _RAREFACTION_EXTENSIONTPCW_

#include "Boundary.h"
#include "Rarefaction.h"
#include "Extension_Curve.h"

class Rarefaction_Extension {
    private:
    protected:
    public:
        static void extension_curve(  FluxFunction *curve_flux,  AccumulationFunction *curve_accum,
                                     RealVector &initial_point,
                                    double deltaxi,
                                    int curve_family,
                                    int increase,
                                    const Boundary *boundary,
                                    const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                    int domain_family,
                                      FluxFunction *domain_ff,  AccumulationFunction *domain_aa,
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _RAREFACTION_EXTENSION_

