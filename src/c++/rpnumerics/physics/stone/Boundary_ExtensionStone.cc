#include "Boundary_ExtensionStone.h"
//
//void Boundary_ExtensionStone::extension_curve(const FluxFunction *curve_flux, const AccumulationFunction *curve_accum,
//                                              // const FluxFunction *curve_reduced_flux, const AccumulationFunction *curve_reduced_accum,
//                                              int fixed_s, int number_of_steps,
//                                              int curve_family,
//                                              const RealVector &pmin, const RealVector &pmax, int *number_of_grid_points,         // For the domain.
//                                              int domain_family,
//                                              const FluxFunction *domain_ff, const AccumulationFunction *domain_aa, const Boundary * boundary,
//                                              // const FluxFunction *domain_reduced_ff, const AccumulationFunction *domain_reduced_aa,
//                                              int characteristic_where, int singular,
//                                              std::vector<RealVector> &curve_segments,
//                                              std::vector<RealVector> &domain_segments){
//
//    curve_segments.clear();
//    domain_segments.clear();
//
//    if (number_of_steps < 3) number_of_steps = 3; // Extremes must be eliminated, thus the minimum number of desired segments is 3: of which only one segment will remain.
//
//    double p[number_of_steps - 1][2];
//
//    double p_alpha[2], p_beta[2];
//
//    double delta = 1.0/(double)number_of_steps;
//
//    if (fixed_s == BOUNDARY_EXTENSIONSTONE_SO_CONST){
//        p_alpha[0] = 0.0; p_alpha[1] = 0.0;
//        p_beta[0]  = 1.0; p_beta[1]  = 0.0;
//    }
//    else if (fixed_s == BOUNDARY_EXTENSIONSTONE_SW_CONST){
//        p_alpha[0] = 0.0; p_alpha[1] = 0.0;
//        p_beta[0]  = 0.0; p_beta[1]  = 1.0;
//    }
//    else {
//        p_alpha[0] = 0.0; p_alpha[1] = 1.0;
//        p_beta[0]  = 1.0; p_beta[1]  = 0.0;
//    }
//
//    for (int i = 1; i < number_of_steps; i++){
//        double beta  = (double)i*delta;
//        double alpha = 1.0 - beta;
//        for (int j = 0; j < 2; j++) p[i - 1][j] = p_alpha[j]*alpha + p_beta[j]*beta;
//    }
//
//    // Boundary extension segments.
//    vector <RealVector> be_segments(2*(number_of_steps - 2));
//
//    for (int i = 0; i < number_of_steps - 2; i++){
//        be_segments[2*i].resize(2);
//        be_segments[2*i + 1].resize(2);
//
//        for (int j = 0; j < 2; j++){
//            be_segments[2*i].component(j)     = p[i][j];
//            be_segments[2*i + 1].component(j) = p[i + 1][j];
//        }
//
//    }
//
//    IF_DEBUG
//        for (int i = 0; i < be_segments.size(); i++) std::cout << "be_segments[" << i << "] = " << be_segments[i] << std::endl;
//    END_DEBUG
//
//    // Compute the extension curve for the rarefaction
//    Extension_Curve extension_curve(pmin, pmax, number_of_grid_points,
//                                    domain_ff, domain_aa,boundary);
//
//    extension_curve.compute_extension_curve(characteristic_where, singular,
//                                            be_segments, curve_family,
//                                            (FluxFunction*)curve_flux, (AccumulationFunction*)curve_accum,
//                                            domain_family,
//                                            curve_segments,
//                                            domain_segments);
//
//    return;
//}
//
