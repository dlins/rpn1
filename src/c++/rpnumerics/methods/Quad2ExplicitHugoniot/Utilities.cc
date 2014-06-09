#include "Utilities.h"
#include "Debug.h"

// Quadratic equation. Translated from finder.F::quaeqt.
//
//     this routine calculates the real roots of a quadratic equation
//     of the form
//
//            y = x**2 + a*x + b
//
//     it returns  quaeqt = 0  if the roots are complex
//     it returns  quaeqt = 2  if the roots had been found
//
int Utilities::quadratic_equation(double a, double b, double &x1, double &x2){
    double disc = a*a - 4*b;

    if (disc >= 0.0){
        double discsqrt = sqrt(disc);
        x1 = .5*(-a + discsqrt);
        x2 = .5*(-a - discsqrt);

        return REAL_ROOTS;
    }
    else return COMPLEX_ROOTS;
}

// Cubic equation.  Translated from finder.F::cubert.
//     this function solves the cubic equation
//
//        fcub(x) = x**3 + a*x**2 + b*x + c
//
//     it returns  cubert = 1  if there is only one real root of f
//     it returns  cubert = 3  if there are three real roots of f
//
int Utilities::cubic_equation(std::vector<double> &x, double a, double b, double c){
    x.clear();

    double tol  = 1.e-5;
    double width_default = 1.0;

    double width = width_default;

    double aa = a, bb = b, cc = c;

    double x1;

    // Find a zero of the cubic equation
    //
    int root;

    for (int i = 1; i < 20; i++){
        root = solve1(x1, -width, width, aa, bb, cc, tol); // solve1 ( x1, -eps, eps, fcub, zero, tol )

//        if ( Debug::get_debug_level() == 5 ) {
//            std:://cout << "for i = " << i << "...info = " << root << std::endl;
//        }

        if (root == SOLVE1_OK) break;
        else width *= 2.0;
    }

    if (root == SOLVE1_OK){
        // Add the root that was found so far:
        x.push_back(x1);

        // Calculate the coefficients of the quadratic equation
        //
        double q = a + x1;
        double r = x1*(a + x1) + b;

        // Verify if the roots of the quadratic equation are real
        //
        double x2, x3;
        if (quadratic_equation(q, r, x2, x3) == REAL_ROOTS){
            x.push_back(x2);
            x.push_back(x3);
        }
    }
    else {
//        if ( Debug::get_debug_level() == 5 ) {
//            std:://cout << "Utilities::cubic_equation(): No root was found!" << std::endl;
//        }
    }

    // If no root was found it will return 0, otherwise it will return 1 or 3.
    return x.size();
}

//    This is a temporary version using bisection of a routine
//    which finds the point  xn  on the segment  xa,  xb  where
//    the function  f  has the value  v, whith an accuracy of
//    tol.
//
// It uses cubic_polynomial, hardwired.
//
int Utilities::solve1(double &xn, double xa, double xb,
                      double aa, double bb, double cc,
                      double tol){

    double x, xl, xr, fx, fl, fr;

    fr = cubic_polynomial(xb, aa, bb, cc);
    fl = cubic_polynomial(xa, aa, bb, cc);

    if (fl*fr >= 0.0){
//        if ( Debug::get_debug_level() == 5 ) {
//            std:://cout << "Utilities::solve1(): fl*fr >= 0.0 (No change in the sign.)" << std::endl;
//        }
        return SOLVE1_NO_ZERO;
    }

    // This was not like this in Fortran. The original code was:
    //     xl = 0.0
    //     xr = 1.0
    //
    xl = 0.0; 
    xr = 1.0;

    bool found = false;

    for (int i = 1; i < 60; i++){
        if (xr - xl <= tol){
            found = true;
            continue;
        }
        
        x = .5*(xl + xr);
        fx = cubic_polynomial(xa+x*(xb-xa), aa, bb, cc);

        if (fx*fl > 0.0){
            xl = x;
            fl = fx;
        }
        else {
            xr = x;
            fr = fx;
        }
    }

    if (found){
        xn = xa + x * ( xb - xa );
        return SOLVE1_OK;
    }
    else return SOLVE1_NO_CONVERGENCE;
}

// Cubic polynomial. Translated from finder.F::fcub.
double Utilities::cubic_polynomial(double m, double aa, double bb, double cc){
    return m*(m*(m + aa) + bb) + cc;
}

