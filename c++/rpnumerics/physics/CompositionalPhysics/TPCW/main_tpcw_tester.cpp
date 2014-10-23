#include "JetTester.h"
#include "JetTester2D.h"
#include "JetTester1D.h"

#include "Accum2Comp2PhasesAdimensionalized.h"

int main(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./hsigmaC_spline.txt");
    tc->set_flash(flash);

    double phi = 0.38;

    Accum2Comp2PhasesAdimensionalized_Params ap(tc, phi);
    Accum2Comp2PhasesAdimensionalized *accum = new Accum2Comp2PhasesAdimensionalized(ap);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(3), pmax(3);

    pmin.component(0) = 0.0;
    pmin.component(1) = Theta_min;
    pmin.component(2) = 0.0;

    pmax.component(0) = 1.0;
    pmax.component(1) = Theta_max;
    pmax.component(2) = 2.0;

    std::vector<unsigned long int> subdivision;
    subdivision.push_back(100);
    subdivision.push_back(100);
    subdivision.push_back(100);

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)(accum), &Accum2Comp2PhasesAdimensionalized::accumulation_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = 3;
    cols = 3;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    // Integer delta.
    //
    for (int k = 0; k < 5; k++){
        unsigned long int intdelta = 1;
        for (int i = 0; i < k; i++) intdelta *= 2;

        jt.numerical_Jacobian(F, 
                              JF,
                              rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                              intdelta, 
                              pmin, pmax,
                              subdivision,
                              numerical_analytic_abs_deviation_sup,
                              synthetic_deviation,
                              max_abs_F);

        std::cout << "==========================================================" << std::endl;
        std::cout << "k = " << k << ", intdelta = " << intdelta << std::endl;
        std::cout << numerical_analytic_abs_deviation_sup << std::endl;
        std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
        std::cout << "max_abs_F = " << max_abs_F << std::endl;
        std::cout << "synthetic_deviation/max_abs_F = " << numerical_analytic_abs_deviation_sup*(1.0/max_abs_F) << std::endl;
    }

    return 0;
}

