#include <iostream>
#include "MolarDensity.h"

int main(){
    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    double x = 0.012599, T = 339.88;
    int degree = 1;

    JetMatrix tau21j(1);
    mdl.tau21_jet(T, degree, tau21j); std::cout << "tau21_jet" << std::endl;

    JetMatrix  G21j(1);
    mdl.G21_jet(T, degree, G21j); std::cout << "G21_jet" << std::endl;

    JetMatrix Gamma1j(2);
    mdl.Gamma1_jet(x, T, degree, Gamma1j); std::cout << "Gamma1_jet" << std::endl;

    JetMatrix gej(2);
    mdl.GE_jet(x,T, degree, gej);   std::cout << "liquid_L_jet after GE_jet" << std::endl;

    JetMatrix Lj(2);
    mdl.L_jet(x, T, degree, Lj);

    std::cout << "Main: After mdl.L_jet" << std::endl;

    return 0;
}

