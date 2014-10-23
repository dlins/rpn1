#include "JetTester.h"
#include <iostream>

int f(void*, const RealVector &state, int degree, JetMatrix &jm){
    int n = state.size();
    jm.resize(n);

    double x = state(0);
    double y = state(1);
    double z = state(2);

    if (degree >= 0){
        jm.set(0, sin(x));
        jm.set(1, y*y);
        jm.set(2, z*z);

        if (degree >= 1){
            jm.set(0, 0, cos(x));
            jm.set(0, 1,  0.0);
            jm.set(0, 2,  0.0);

            jm.set(1, 0,  0.0);
            jm.set(1, 1, 2.*y);
            jm.set(1, 2,  0.0);

            jm.set(2, 0,  0.0);
            jm.set(2, 1,  0.0);
            jm.set(2, 2, 2.*z);

            if (degree >= 2){
                jm.set(0, 0, 0, -sin(x));
                jm.set(0, 0, 1, 0.0);
                jm.set(0, 0, 2, 0.0);

                jm.set(0, 1, 0, 0.0);
                jm.set(0, 1, 1, 0.0);
                jm.set(0, 1, 2, 0.0);

                jm.set(0, 2, 0, 0.0);
                jm.set(0, 2, 1, 0.0);
                jm.set(0, 2, 2, 0.0);

                /////////////////////

                jm.set(1, 0, 0, 0.0);
                jm.set(1, 0, 1, 0.0);
                jm.set(1, 0, 2, 0.0);

                jm.set(1, 1, 0, 0.0);
                jm.set(1, 1, 1, 2.0);
                jm.set(1, 1, 2, 0.0);

                jm.set(1, 2, 0, 0.0);
                jm.set(1, 2, 1, 0.0);
                jm.set(1, 2, 2, 0.0);

                /////////////////////

                jm.set(2, 0, 0, 0.0);
                jm.set(2, 0, 1, 0.0);
                jm.set(2, 0, 2, 0.0);

                jm.set(2, 1, 0, 0.0);
                jm.set(2, 1, 1, 0.0);
                jm.set(2, 1, 2, 0.0);

                jm.set(2, 2, 0, 0.0);
                jm.set(2, 2, 1, 0.0);
                jm.set(2, 2, 2, 2.0);
            }
        }
    }
    
    return 0;
}

int main(){
    JetTester jt;

    int n = 3;
    RealVector pmin(n), pmax(n);
    std::vector<unsigned long int> subdivision(n);

    for (int i = 0; i < n; i++) {
        pmin(i) = 0.0;
        pmax(i) = 1.0;

        subdivision[i] = 40;
    }
//    subdivision[1] = 4;

//    MultiArray<RealVector>    F;
//    MultiArray<DoubleMatrix> JF;

//    jt.populate_F(0, &f,
//                  pmin, pmax,
//                  subdivision,
//                  F, JF);

//    std::cout << F << std::endl;
//    std::cout << "\n" << JF << std::endl;

    int rows, cols;
    rows = cols = n;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;

//    jt.numerical_Jacobian(F, 
//                          JF,
//                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
//                          pmin, pmax,
//                          subdivision,
//                          numerical_analytic_abs_deviation_sup,
//                          synthetic_deviation);
//    std::cout << "numerical_analytic_abs_deviation_sup =\n" << numerical_analytic_abs_deviation_sup << std::endl;
//    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;

    MultiArray<DoubleMatrix> JF;
    MultiArray<std::vector<DoubleMatrix> > HF;

    jt.populate_JF(0, &f,
                   pmin, pmax,
                   subdivision,
                   JF, HF);

//    std::cout << JF << std::endl;

//    std::vector<unsigned long int> range = HF.range();
//    for (int i = 0; i < range.size(); i++){
//        for (int j = 0; j < range[i]; j++){
//            std::cout << "i = " << i << ", j = " << j << std::endl;

//            for (int k = 0; k < HF(i, j).size(); k++){
//                std::cout << HF(i, j)[k] << std::endl;
//            }
//        }
//    } 

    std::vector<DoubleMatrix> matrix_numerical_analytic_abs_deviation_sup;
    RealVector max_vec;

    jt.numerical_Hessian(JF, 
                         HF,
                         rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                         pmin, pmax,
                         subdivision,
                         matrix_numerical_analytic_abs_deviation_sup,
                         max_vec,
                         synthetic_deviation);    

    for (int i = 0; i < matrix_numerical_analytic_abs_deviation_sup.size(); i++){
        std::cout << "component = " << i << ", error = " << std::endl << matrix_numerical_analytic_abs_deviation_sup[i] << std::endl;
    }

    std::cout << "max_vec = " << max_vec << std::endl;

    return 0;
}

