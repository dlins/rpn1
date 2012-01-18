#include "Hysteresis.h"

int Hysteresis::curve(Boundary *boundary, 
                      const FluxFunction *curve_flux, const AccumulationFunction *curve_accum,
                      int curve_family,
                      const RealVector &pmin, const RealVector &pmax, int *number_of_cells,         // For the domain.
                      int domain_family,
                      const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
                      int characteristic_where, int singular,
                      std::vector<RealVector> &curve_segments,
                      std::vector<RealVector> &domain_segments){

    // Inflection curve
    Inflection_Curve ic((FluxFunction*)curve_flux, (AccumulationFunction*)curve_accum, boundary, 
                         pmin, pmax, number_of_cells);
    std::vector<RealVector> vic;
    int info = ic.curve(curve_family, vic);

    // Compute the extension curve for the inflection curve.
    //
    Extension_Curve extension_curve(pmin, pmax, number_of_cells, domain_ff, domain_aa);

    extension_curve.compute_extension_curve(characteristic_where, singular,
                                            vic, curve_family,
                                            (FluxFunction*)curve_flux, (AccumulationFunction*)curve_accum,
                                            domain_family, 
                                            curve_segments,
                                            domain_segments);

    return info;
}

