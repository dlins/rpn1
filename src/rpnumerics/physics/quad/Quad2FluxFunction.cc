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

Quad2FluxFunction::Quad2FluxFunction(const Quad2FluxParams & params) : FluxFunction(params)

{
}

Quad2FluxFunction * Quad2FluxFunction::clone() const {
    return new Quad2FluxFunction(*this);
}

Quad2FluxFunction::~Quad2FluxFunction(void) {
    
}


//
//TODO  set degree = 2 as default value
//
int Quad2FluxFunction::jet(const WaveState & x, JetMatrix & y, int degree = 2)const {
    

    
    double u = x(0);
    double v= x(1);
    
    double a1, b1, c1, d1, e1, a2, b2, c2, d2, e2;
    
    a1 = -1.0;
    b1 = 0.0;
    c1 = 1.0;
    d1 = 0.0;
    e1 = 0.0;
    
    a2 = 1.0;
    b2 = 1.0;
    c2 = 0.0;
    d2 = 0.0;
    e2 = 0.0;
    
    switch (degree){
        
        case 0:
            
            double out;
            
            out =  0.5 * ( a1*pow(u, (double)2) + 2.0*b1*u*v + c1*pow(v, (double)2) ) + d1*u + e1*v;
            y(0, out);
            out =  0.5 * ( a2*pow(u, (double)2) + 2.0*b2*u*v + c2*pow(v, (double)2) ) + d2*u + e2*v;
            
            y(1, out);
                        
            break;
            
        case 1:
            
            double  out00, out01, out10, out11;
            
            out00 = a1*u + b1*v + d1;
            out01 = b1*u + c1*v + e1;
            out10 = a2*u + b2*v + d2;
            out11 = b2*u + c2*v + e2;
            
            y(0, 0, out00);
            y(0, 1, out01);
            y(1, 0, out10);
            y(1, 1, out11);
            
            break;
            
        case 2:
            
            y(0, 0, 0, a1);
            y(1, 0, 0, b1);
            y(0, 1, 0, b1);
            y(1, 1, 0, c1);
            y(0, 0, 1, a2);
            y(1, 0, 1, b2);
            y(0, 1, 1, b2);
            y(1, 1, 1, c2);
            
            break;
            
    }
    
    return 2;//SUCCESSFUL_PROCEDURE;
}


