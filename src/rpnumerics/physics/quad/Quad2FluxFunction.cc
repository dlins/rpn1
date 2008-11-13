
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2FluxFunction.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Quad2FluxFunction.h"
#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
#include <iostream.h>


Quad2FluxFunction::Quad2FluxFunction(const Quad2FluxParams & params) : FluxFunction(params) {
}


Quad2FluxFunction::Quad2FluxFunction(const Quad2FluxFunction & copy):FluxFunction(copy.fluxParams()) {
}


Quad2FluxFunction * Quad2FluxFunction::clone() const {
    return new Quad2FluxFunction(*this);
}


Quad2FluxFunction::~Quad2FluxFunction(void) {
}


int Quad2FluxFunction::jet(const WaveState & x, JetMatrix & y, int degree = 2) const {

    // Calculate F
    double a1, b1, c1, d1, e1, a2, b2, c2, d2, e2, out0, out1;
    FluxParams params = fluxParams();
    
    a1 = params.component(0);
    b1 = params.component(1);
    c1 = params.component(2);
    d1 = params.component(3);
    e1 = params.component(4);
    
    a2 = params.component(5);
    b2 = params.component(6);
    c2 = params.component(7);
    d2 = params.component(8);
    e2 = params.component(9);
    
    double u = x(0);
    double v = x(1);
    
    out0 =  0.5 * ( a1*pow(u, (double)2) + 2.0*b1*u*v + c1*pow(v, (double)2) ) + d1*u + e1*v;
    out1 =  0.5 * ( a2*pow(u, (double)2) + 2.0*b2*u*v + c2*pow(v, (double)2) ) + d2*u + e2*v;

    y(0, out0);
    y(1, out1);
    
    if ( degree > 0 ) {
    	// Calculate DF
    	double  out00, out01, out10, out11;
    
    	out00 = a1*u + b1*v + d1;
    	out01 = b1*u + c1*v + e1;
    	out10 = a2*u + b2*v + d2;
    	out11 = b2*u + c2*v + e2;
    
    	y(0, 0, out00);
    	y(0, 1, out01);
    	y(1, 0, out10);
    	y(1, 1, out11);

	if ( degree > 1 ) {
    	 	// Calculate D2F
    		y(0, 0, 0, a1);
    		y(1, 0, 0, b1);
    		y(0, 1, 0, b1);
    		y(1, 1, 0, c1);
    		y(0, 0, 1, a2);
    		y(1, 0, 1, b2);
    		y(0, 1, 1, b2);
    		y(1, 1, 1, c2);

		if (degree > 2 ) {
			return 0; //UNSUCCESSFUL_PROCEDURE;
		}
	}
    } 

    return 2;//SUCCESSFUL_PROCEDURE;
}


