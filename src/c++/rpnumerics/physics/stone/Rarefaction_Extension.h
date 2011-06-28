#ifndef _RAREFACTION_EXTENSION_
#define _RAREFACTION_EXTENSION_

#include "Boundary.h"
#include "Rarefaction.h"
#include "Extension_Curve.h"

class Rarefaction_Extension {
    private:
    protected:
    public:
        static void extension_curve(const FluxFunction *curve_ff, const AccumulationFunction *curve_aa,
                                    const RealVector &initial_point,
                                    double deltaxi,
                                    int curve_family,
                                    int increase,
                                    const Boundary *boundary, 
                                    const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                    int domain_family,
                                    const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &rarefaction_segments,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _RAREFACTION_EXTENSION_

