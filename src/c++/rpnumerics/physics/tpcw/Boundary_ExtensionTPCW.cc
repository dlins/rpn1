#include "Boundary_ExtensionTPCW.h"

void Boundary_ExtensionTPCW::extension_curve(const Flux2Comp2PhasesAdimensionalized *curve_flux, const Accum2Comp2PhasesAdimensionalized *curve_accum,
//                                             const FluxFunction *curve_reduced_flux, const AccumulationFunction *curve_reduced_accum,
                                             int fixed_s, int number_of_temperature_steps,
                                             int curve_family,
                                             const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                             int domain_family,
                                             const Flux2Comp2PhasesAdimensionalized *domain_ff, const Accum2Comp2PhasesAdimensionalized *domain_aa,
//                                             const FluxFunction *domain_reduced_ff, const AccumulationFunction *domain_reduced_aa,
                                             int characteristic_where, int singular,
                                             std::vector<RealVector> &curve_segments,
                                             std::vector<RealVector> &domain_segments){

    curve_segments.clear();
    domain_segments.clear();

    if (number_of_temperature_steps < 1) number_of_temperature_steps = 1;
    double delta_T = (pmax.component(1) - pmin.component(1))/(double)number_of_temperature_steps;
    double T[number_of_temperature_steps + 1];
    for (int i = 0; i <= number_of_temperature_steps; i++) T[i] = pmin.component(1) + (double)i*delta_T;

    int n = pmin.size();

    // Boundary extension segments.
    vector <RealVector> be_segments(2*number_of_temperature_steps);

    double s = ((fixed_s == BOUNDARY_EXTENSIONTPCW_S_ZERO) ? 0.0 : 1.0);

    for (int i = 0; i < number_of_temperature_steps; i++){
        IF_DEBUG
            printf("i = %d\n", i);
        END_DEBUG

        be_segments[2*i].resize(n);
        be_segments[2*i + 1].resize(n);

        // s
        be_segments[2*i].component(0) = be_segments[2*i + 1].component(0) = s;

        // T
        be_segments[2*i].component(1)     = T[i];
        be_segments[2*i + 1].component(1) = T[i + 1];

        // u
//        be_segments[2*i].component(2) = be_segments[2*i + 1].component(2) = 1.0;
        IF_DEBUG
            printf(" ---\n");
        END_DEBUG
    }

    // Compute the extension curve for the rarefaction
    Extension_CurveTPCW extension_curvetpcw(pmin, pmax, number_of_grid_points,
                                            domain_ff, domain_aa);
//                                            domain_reduced_ff, domain_reduced_aa);

    extension_curvetpcw.compute_extension_curve(characteristic_where, singular,
                                                be_segments, curve_family,
                                                curve_flux,curve_accum,
//                                                (FluxFunction*)curve_reduced_flux, (AccumulationFunction*)curve_reduced_accum,
                                                domain_family, 
                                                curve_segments,
                                                domain_segments);

    return;
}

