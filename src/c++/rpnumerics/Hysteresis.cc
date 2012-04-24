#include "Hysteresis.h"

void Hysteresis::curve(
        const FluxFunction *curve_flux, const AccumulationFunction *curve_accum, GridValues &gv,
        int characteristic_where, int curve_family,
        int domain_family,
        const FluxFunction *domain_ff, const AccumulationFunction *domain_aa,
        int singular,
        std::vector<RealVector> &curve_segments,
        std::vector<RealVector> &domain_segments) {

    //    // Inflection curve

    std::vector<RealVector> inflectionSegments;
    Inflection_Curve inflectionCurve;

    inflectionCurve.curve(curve_flux, curve_accum, gv, curve_family, inflectionSegments);


    //Compute the extension curve for the inflection curve.

    Extension_Curve extension_curve;
    //
    extension_curve.curve(curve_flux, curve_accum, gv, characteristic_where, singular, curve_family,
            inflectionSegments, curve_segments, domain_segments);

}

