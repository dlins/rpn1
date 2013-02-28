#ifndef _RAREFACTION_EXTENSIONTPCW_
#define _RAREFACTION_EXTENSIONTPCW_

#include "Boundary.h"
#include "Rarefaction.h"
#include "Extension_CurveTPCW.h"

class Rarefaction_ExtensionTPCW {
    private:
    protected:
    public:
        static void extension_curve(const Flux2Comp2PhasesAdimensionalized *curve_flux, const Accum2Comp2PhasesAdimensionalized *curve_accum,
                                    const RealVector &initial_point,
                                    double deltaxi,
                                    int curve_family,
                                    int increase,
                                    const Boundary *boundary,
                                    const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                    int domain_family,
                                    const Flux2Comp2PhasesAdimensionalized *domain_ff, const Accum2Comp2PhasesAdimensionalized *domain_aa,
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _RAREFACTION_EXTENSION_

