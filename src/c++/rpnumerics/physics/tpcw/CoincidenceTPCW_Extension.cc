#include "CoincidenceTPCW_Extension.h"

void CoincidenceTPCW_Extension::extension_curve(CoincidenceTPCW *coincidencetpcw, 
                                                RealVector &pmin, RealVector &pmax, int *number_of_grid_points,         // For the domain.
                                                FluxFunction *dff, AccumulationFunction *daa,                           // For the domain.
                                                FluxFunction *drff, AccumulationFunction *draa,                         // For the domain.
                                                int domain_family,                                                                  // For the domain.
                                                FluxFunction *curve_ff, AccumulationFunction *curve_aa,                 // For the curve.
                                                FluxFunction *curve_reduced_ff, AccumulationFunction *curve_reduced_aa, // For the curve.
                                                int curve_family,                                                                   // For the curve.
                                                int characteristic_where, int singular,
                                                std::vector<RealVector> &curve_segments,
                                                std::vector<RealVector> &domain_segments){

    // Compute the contour

      RectBoundary rectBoundary(pmin, pmax);
    ContourMethod contourmethod(3, *dff, *daa, rectBoundary, coincidencetpcw);
//    ContourMethod contourmethod(coincidencetpcw);

    int isfirst = 1; // So that the contour computes some combinatorial stuff.

    double rect[4];
    rect[0] = pmin.component(0);
    rect[1] = pmax.component(0);
    rect[2] = pmin.component(1);
    rect[3] = pmax.component(1);

    int maxnum = 1000; // TODO: Modify curve2d so this is not necessary anymore.
    int sn     = 0;     // TODO: Idem.

    double fdummy = 0.0;

    std::vector<RealVector> contour_segments;

    contourmethod.curv2d(sn, maxnum, fdummy, rect, number_of_grid_points, isfirst, contour_segments);

    // Compute the extension curve for the contour
    Extension_CurveTPCW extension_curvetpcw(pmin, pmax, number_of_grid_points,
                                            dff, daa, drff, draa);

    extension_curvetpcw.compute_extension_curve(characteristic_where, singular,
                                                contour_segments, curve_family,
                                                curve_ff, curve_aa,
                                                curve_reduced_ff, curve_reduced_aa,
                                                domain_family, 
                                                curve_segments,
                                                domain_segments);

    return;
}

