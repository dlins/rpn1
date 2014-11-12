#include "ICDOWChemistry.h"

ICDOWChemistry::ICDOWChemistry(){
}

ICDOWChemistry::~ICDOWChemistry(){
}

// x = Hydrogen contents.
//
void ICDOWChemistry::aqueous_carbon_excess_oxygen(double x, int degree, JetMatrix &rho_CO_jet){
    rho_CO_jet.resize(1);

    if (degree >= 0){
        rho_CO_jet.set(0, exp(tanh(x+1.686246E1)*1.119958E1+1.98204));

        if (degree >= 1){
            rho_CO_jet.set(0, 0, -exp(tanh(x+1.686246E1)*1.119958E1+1.98204)*(pow(tanh(x+1.686246E1),2.0)*1.119958E1-1.119958E1));

            if (degree >= 2){
                rho_CO_jet.set(0, 0, 0, exp(tanh(x+1.686246E1)*1.119958E1+1.98204)*
                                        pow(pow(tanh(x+1.686246E1),2.0)*1.119958E1-1.119958E1,2.0)+
                                        exp(tanh(x+1.686246E1)*1.119958E1+1.98204)*tanh(x+1.686246E1)*
                                        (pow(tanh(x+1.686246E1),2.0)*1.0-1.0)*2.239916E1
                              );
            }
        }
    }

    return;
}

// x = Hydrogen contents.
//
void ICDOWChemistry::aqueous_hydrogen(double x, int degree, JetMatrix &rho_H_jet){
    rho_H_jet.resize(1);

    if (degree >= 0){
        rho_H_jet.set(0, exp(1.0751E1/(sinh(x*1.50509958419512)+cosh(x)*3.8642421361418E1-6.4299174481606E3)-1.0/1.0E2));

        if (degree >= 1){
            rho_H_jet.set(0, 0, exp(1.0751E1/(sinh(x*1.50509958419512)+cosh(x)*3.8642421361418E1-6.4299174481606E3)-1.0E-2)*
                                (cosh(x*1.50509958419512)*1.50509958419512+sinh(x)*3.8642421361418E1)*1.0/pow(sinh(x*1.50509958419512)+
                                cosh(x)*3.8642421361418E1-6.4299174481606E3,2.0)*(-1.0751E1)
                         );

            if (degree >= 2){
                rho_H_jet.set(0, 0, 0, exp(1.0751E1/(sinh(x*1.50509958419512)+cosh(x)*3.8642421361418E1-6.4299174481606E3)-1.0E-2)*
                                       pow(cosh(x*1.50509958419512)*1.50509958419512+sinh(x)*3.8642421361418E1,2.0)*1.0/pow(sinh(x*1.50509958419512)+
                                       cosh(x)*3.8642421361418E1-6.4299174481606E3,3.0)*2.1502E1+exp(1.0751E1/(sinh(x*1.50509958419512)+
                                       cosh(x)*3.8642421361418E1-6.4299174481606E3)-1.0E-2)*pow(cosh(x*1.50509958419512)*1.50509958419512+sinh(x)*
                                       3.8642421361418E1,2.0)*1.0/pow(sinh(x*1.50509958419512)+cosh(x)*3.8642421361418E1-6.4299174481606E3,4.0)*
                                       1.15584001E2-exp(1.0751E1/(sinh(x*1.50509958419512)+cosh(x)*3.8642421361418E1-6.4299174481606E3)-1.0E-2)*
                                       (sinh(x*1.50509958419512)*2.265324758344323+cosh(x)*3.8642421361418E1)*1.0/pow(sinh(x*1.50509958419512)+
                                       cosh(x)*3.8642421361418E1-6.4299174481606E3,2.0)*1.0751E1
                             );
            }
        }
    }

    return;
}

// x = Hydrogen contents.
//
void ICDOWChemistry::carbon_in_oil(double x, int degree, JetMatrix &rho_C_oil_jet){
    rho_C_oil_jet.resize(1);

    if (degree >= 0){
        rho_C_oil_jet.set(0, exp(tanh(sinh(x)*9.4728E-4)*(-3.29E2/5.0E1)));

        if (degree >= 1){
            rho_C_oil_jet.set(0, 0, exp(tanh(sinh(x)*9.4728E-4)*-6.58)*cosh(x)*
                                    (pow(tanh(sinh(x)*9.4728E-4),2.0)-1.0)*6.2331024E-3
                             );

            if (degree >= 2){
                rho_C_oil_jet.set(0, 0, 0, exp(tanh(sinh(x)*9.4728E-4)*-6.58)*pow(cosh(x),2.0)*
                                           pow(pow(tanh(sinh(x)*9.4728E-4),2.0)-1.0,2.0)*3.885156552888576E-5+
                                           exp(tanh(sinh(x)*9.4728E-4)*-6.58)*sinh(x)*(pow(tanh(sinh(x)*9.4728E-4),2.0)-1.0)*6.2331024E-3-
                                           tanh(sinh(x)*9.4728E-4)*exp(tanh(sinh(x)*9.4728E-4)*-6.58)*pow(cosh(x),2.0)*
                                           (pow(tanh(sinh(x)*9.4728E-4),2.0)-1.0)*1.1808986482944E-5
                                 );
            }
        }
    }

    return;
}

