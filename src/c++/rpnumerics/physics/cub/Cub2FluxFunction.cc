
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Cub2FluxFunction.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Cub2FluxFunction.h"
#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

Cub2FluxFunction::Cub2FluxFunction(const Cub2FluxParams & params) : FluxFunction(params) {
}

Cub2FluxFunction::Cub2FluxFunction(const Cub2FluxFunction & copy) : FluxFunction(copy.fluxParams()) {
}

Cub2FluxFunction * Cub2FluxFunction::clone() const {
    return new Cub2FluxFunction(*this);
}

Cub2FluxFunction::~Cub2FluxFunction(void) {
}

int Cub2FluxFunction::jet(const WaveState & x, JetMatrix & y, int degree = 2) const {

    // Calculate F
    double a1, b1, c1, d1, e1,f1,g1,h1,k1, a2, b2, c2, d2, e2,f2,g2,h2,k2, out0, out1;
    const FluxParams params = fluxParams();

    a1 = params.component(0);
    b1 = params.component(1);
    c1 = params.component(2);
    d1 = params.component(3);
    e1 = params.component(4);
    f1 = params.component(5);
    g1 = params.component(6);
    h1 = params.component(7);
    k1 = params.component(8);

    a2 = params.component(9);
    b2 = params.component(10);
    c2 = params.component(11);
    d2 = params.component(12);
    e2 = params.component(13);
    f2 = params.component(14);
    g2 = params.component(15);
    h2 = params.component(16);
    k2 = params.component(17);

    double u = x(0);
    double v = x(1);

    out0 = 0.5 * ( (a1*u*u) + (2.0*b1*u*v) + (c1*v*v) ) + d1*u + e1*v + f1*u*u*u + g1*u*u*v + h1*u*v*v + k1*v*v*v;
    out1 = 0.5 * ( (a2*u*u) + (2.0*b2*u*v) + (c2*v*v) ) + d2*u + e2*v + f2*u*u*u + g2*u*u*v + h2*u*v*v + k2*v*v*v;

    y.get(0, out0);
    y.get(1, out1);

    if (degree > 0) {
        // Calculate DF
        double out00, out01, out10, out11;

        out00 = a1*u + b1*v + d1 + 3.0*f1*u*u + 2.0*g1*u*v + h1*v*v;
        out01 = b1*u + c1*v + e1 + g1*u*u + 2.0*h1*u*v + 3.0*k1*v*v;
        out10 = a2*u + b2*v + d2 + 3.0*f2*u*u + 2.0*g2*u*v + h2*v*v;
        out11 = b2*u + c2*v + e2 + g2*u*u + 2.0*h2*u*v + 3.0*k2*v*v;

        y.set(0, 0, out00);
        y.set(0, 1, out01);
        y.set(1, 0, out10);
        y.set(1, 1, out11);

        if (degree > 1) {
	double out000, out001, out010, out011, out100, out101, out110, out111;
            // Calculate D2F

	    out000 = a1 + 6.0*f1*u + 2.0*g1*v;
	    out001 = b1 + 2.0*g1*u + 2.0*h1*v;
	    out010 = out001;
	    out011 = c1 + 2.0*h1*u + 6.0*k1*v;
	    out100 = a2 + 6.0*f2*u + 2.0*g2*v;
	    out101 = b2 + 2.0*g2*u + 2.0*h2*v;
	    out110 = out101;
	    out111 = c2 + 2.0*h2*u + 6.0*k2*v;

            y.set(0, 0, 0, out000);
            y.set(0, 0, 1, out001);
            y.set(0, 1, 0, out010);
            y.set(0, 1, 1, out011);
            y.set(1, 0, 0, out100);
            y.set(1, 0, 1, out101);
            y.set(1, 1, 0, out110);
            y.set(1, 1, 1, out111);

            if (degree > 2) {
                return 0; //UNSUCCESSFUL_PROCEDURE;
            }
        }
    }

    return 2; //SUCCESSFUL_PROCEDURE;
}

