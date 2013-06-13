/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) EllipticExtension.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "EllipticExtension.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



int EllipticExtension::curve(const FluxFunction *f, const AccumulationFunction *a, int where_is_characteristic, int family,
        GridValues &g, std::vector<RealVector> &elliptic_extension_on_curve, std::vector<RealVector> &elliptic_extension_on_domain) {


    Elliptic_Boundary ellipticBoundary;
    std::vector<RealVector> ellipiticCurve;
    ellipticBoundary.curve(f, a, g, ellipiticCurve);



    Extension_Curve extensionCurve;

    IF_DEBUG
        cout <<"Familia na extensao: "<<family<<endl;
    END_DEBUG

    extensionCurve.curve(f, a,
            g, where_is_characteristic,
            true, family,
            ellipiticCurve,
            elliptic_extension_on_curve,
            elliptic_extension_on_domain);





}


