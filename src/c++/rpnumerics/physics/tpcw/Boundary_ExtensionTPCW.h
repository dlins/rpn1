#ifndef _BOUNDARY_EXTENSIONTPCW_
#define _BOUNDARY_EXTENSIONTPCW_

#include "Extension_CurveTPCW.h"

#define BOUNDARY_EXTENSIONTPCW_S_ZERO 0
#define BOUNDARY_EXTENSIONTPCW_S_ONE  1

class Boundary_ExtensionTPCW{
    private:
    protected:
    public:
        static void extension_curve(const FluxFunction *curve_flux, const AccumulationFunction *curve_accum,
                                    const FluxFunction *curve_reduced_flux, const AccumulationFunction *curve_reduced_accum,
                                    int fixed_s, int number_of_temperature_steps,
                                    int curve_family,
                                    const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                    int domain_family,
                                    const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
                                    const FluxFunction *domain_reduced_ff, const AccumulationFunction *domain_reduced_aa,
                                    int characteristic_where, int singular,
                                    std::vector<RealVector> &curve_segments,
                                    std::vector<RealVector> &domain_segments);
};

#endif // _BOUNDARY_EXTENSIONTPCW_

