
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
#include "Quad4FluxFunction.h"
#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

Quad4FluxFunction::Quad4FluxFunction(const Quad4FluxParams & params) : FluxFunction(params) {
}

Quad4FluxFunction::Quad4FluxFunction(const Quad4FluxFunction & copy) : FluxFunction(copy.fluxParams()) {
}

Quad4FluxFunction * Quad4FluxFunction::clone() const {
    return new Quad4FluxFunction(*this);
}

Quad4FluxFunction::~Quad4FluxFunction(void) {
}

int Quad4FluxFunction::jet(const WaveState & x, JetMatrix & output, int degree = 2) const {

    // Calculate F
    // F = A + B x + (1/2) x^T C x

    int spaceDim = 2;
    double q [spaceDim];
    double res [spaceDim];
    for (int i = 0; i < spaceDim; i++)
        q[i] = x(i);
    for (int i = 0; i < spaceDim; i++) {
        res[i] = Quad4FluxParams::DEFAULT_A[i];
        for (int j = 0; j < spaceDim; j++) {
            res[i] = res[i] + Quad4FluxParams::DEFAULT_B[i] [j] * q[j];
            for (int k = 0; k < spaceDim; k++)
                res[i] = res[i] + 0.5 * Quad4FluxParams::DEFAULT_C[i][j][k] * q[j] * q[k];
        }
    }
    for (int i = 0; i < spaceDim; i++)
        output(i, res[i]);

    if (degree > 0) {
        // Calculate DF
        // DF = B + C x

        double q [spaceDim];
        double res [spaceDim][spaceDim];
        for (int i = 0; i < spaceDim; i++)
            q[i] = x(i);
        for (int i = 0; i < spaceDim; i++)
            for (int j = 0; j < spaceDim; j++) {
                res[i] [j] = Quad4FluxParams::DEFAULT_B[i] [j];
                for (int k = 0; k < spaceDim; k++)
                    res[i] [j] = res[i] [j] + Quad4FluxParams::DEFAULT_C[i][j][k] * q[k];
            }
        for (int i = 0; i < spaceDim; i++)
            for (int j = 0; j < spaceDim; j++)
                output(i, j, res[i] [j]);


        if (degree > 1) {

            // Calculate D2F
            // DFF = C
            for (int i = 0; i < spaceDim; i++)
                for (int j = 0; j < spaceDim; j++)
                    for (int k = 0; k < spaceDim; k++)
                        output(i, j, k, Quad4FluxParams::DEFAULT_C[i][j][k]);


            if (degree > 2) {
                return 0; //UNSUCCESSFUL_PROCEDURE;

            }

        }
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}


//int Quad4FluxFunction::jet(const WaveState & x, JetMatrix & y, int degree = 2) const {
//
//    // Calculate F
//    double a1, b1, c1, d1, e1, a2, b2, c2, d2, e2, out0, out1;
//
//
//    const FluxParams params = fluxParams();
//
//    RealVector parVector = params.params();
//    
//
//    cout << parVector << "\n";
//
//
//    a1 = params.component(0);
//    b1 = params.component(1);
//    c1 = params.component(2);
//    d1 = params.component(3);
//    e1 = params.component(4);
//
//
//    cout << "a1 0: " << a1 << "\n";
//    cout << "b1 1: " << b1 << "\n";
//    cout << "c1 2: " << c1 << "\n";
//    cout << "d1 3: " << d1 << "\n";
//    cout << "e1 4: " << e1 << "\n";
//
//
//
//    a2 = params.component(5);
//    b2 = params.component(6);
//    c2 = params.component(7);
//    d2 = params.component(8);
//    e2 = params.component(9);
//    
//
//    cout << "a2 5: " << a2 << "\n";
//    cout << "b2 6: " << b2 << "\n";
//    cout << "c2 7: " << c2 << "\n";
//    cout << "d2 8: " << d2 << "\n";
//    cout << "e2 9: " << e2 << "\n";
//
//    
//    
//
////    double u = x(0);
////    double v = x(1);
//
//    double u = 0.14;
//    double v = 0.7;
//
//    
//    
//    out0 = 0.5 * (a1 * pow(u, (double) 2) + 2.0 * b1 * u * v + c1 * pow(v, (double) 2)) + d1 * u + e1*v;
//    
//    
//    
//    out1 = 0.5 * (a2 * pow(u, (double) 2) + 2.0 * b2 * u * v + c2 * pow(v, (double) 2)) + d2 * u + e2*v;
//
//    y(0, out0);
//    y(1, out1);
//
//
//    cout << "f (C++): " << y(0) << " "<< y(1)<<"\n";
//
//
//    
//    
//
//    if (degree > 0) {
//        // Calculate DF
//        double out00, out01, out10, out11;
//
//        out00 = a1 * u + b1 * v + d1;
//        out01 = b1 * u + c1 * v + e1;
//        out10 = a2 * u + b2 * v + d2;
//        out11 = b2 * u + c2 * v + e2;
//
//        y(0, 0, out00);
//        y(0, 1, out01);
//        y(1, 0, out10);
//        y(1, 1, out11);
//
//        if (degree > 1) {
//            // Calculate D2F
//            y(0, 0, 0, a1);
//            y(1, 0, 0, b1);
//            y(0, 1, 0, b1);
//            y(1, 1, 0, c1);
//            y(0, 0, 1, a2);
//            y(1, 0, 1, b2);
//            y(0, 1, 1, b2);
//            y(1, 1, 1, c2);
//
//            if (degree > 2) {
//                return 0; //UNSUCCESSFUL_PROCEDURE;
//
//            }
//        }
//    }
//
//    return 2; //SUCCESSFUL_PROCEDURE;
//}

























