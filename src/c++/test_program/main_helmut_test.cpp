#include "Accum2Comp2PhasesAdimensionalized.h"


#include "JetTester.h"
#include "canvas.h"
#include "curve2d.h"
#include <FL/Fl_Double_Window.H>


int f2x1(void*, const RealVector &p, int degree, JetMatrix &jm){
    double x = p(0);
    double y = p(1);

    jm.resize(2, 1);

    jm.set(0, cos(x) + sin(y));

    jm.set(0, 0, -sin(x));
    jm.set(0, 1,  cos(y));

    jm.set(0, 0, 0, -cos(x));
    jm.set(0, 0, 1, 0.0);
    jm.set(0, 1, 0, 0.0);
    jm.set(0, 1, 1, -sin(y));

    return 1;
}

int main_accum(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
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

    jt.populate_F((void*)accum, &Accum2Comp2PhasesAdimensionalized::accumulation_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = cols = 3;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;

    return 0;
}

int main_thermo(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(1), pmax(1);

    pmin.component(0) = Theta_min;

    pmax.component(0) = Theta_max;

    std::vector<unsigned long int> subdivision;
    subdivision.push_back(200);

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)tc, &Thermodynamics::Rhoaw_jet_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = cols = 1;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
    std::cout << "max_abs_F = " << max_abs_F << std::endl;
    std::cout << "synthetic_deviation/max_abs_F = " << synthetic_deviation/max_abs_F << std::endl;

    return 0;
}

int main_thermo2d(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(2), pmax(2);

    pmin.component(0) = 0.0; // yw
    pmin.component(1) = Theta_min;

    pmax.component(0) = 1.0;
    pmax.component(1) = Theta_max;

    std::vector<unsigned long int> subdivision(2);
    subdivision[0] = 100;
    subdivision[1] = 100;

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)tc, &Thermodynamics::Rhoac_jet2_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

//    std::cout << F << std::endl;
//    std::cout << JF << std::endl;

    int rows, cols;
    rows = 1;
    cols = 2;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
    std::cout << "max_abs_F = " << max_abs_F << std::endl;
    std::cout << "numerical_analytic_abs_deviation_sup/max_abs_F = " << numerical_analytic_abs_deviation_sup*(1.0/max_abs_F) << std::endl;

    return 0;
}

int main_flash(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(1), pmax(1);

    pmin.component(0) = tc->Theta2T(Theta_min);

    pmax.component(0) = tc->Theta2T(Theta_max);

    std::vector<unsigned long int> subdivision;
    subdivision.push_back(200);

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)flash, &VLE_Flash_TPCW::molarfractions_xcj_jet_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = cols = 1;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
    std::cout << "max_abs_F = " << max_abs_F << std::endl;
    std::cout << "synthetic_deviation/max_abs_F = " << synthetic_deviation/max_abs_F << std::endl;

    return 0;
}

int main_molar(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(1), pmax(1);

    pmin.component(0) = tc->Theta2T(Theta_min);

    pmax.component(0) = tc->Theta2T(Theta_max);

    std::vector<unsigned long int> subdivision;
    subdivision.push_back(100);

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)(&mdv), &MolarDensity::tau21_jet_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = cols = 1;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
    std::cout << "max_abs_F = " << max_abs_F << std::endl;
    std::cout << "synthetic_deviation/max_abs_F = " << synthetic_deviation/max_abs_F << std::endl;

    return 0;
}

int main_molar2x1(){
    // Thermo.
    //
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    // Pmin, pmax.
    //
    double Theta_min = 0.099309;
    double Theta_max = 0.576511;

    RealVector pmin(2), pmax(2);

    pmin(0) = 0.0;
    pmin(1) = tc->Theta2T(Theta_min);

    pmax(0) = 1.0;
    pmax(1) = tc->Theta2T(Theta_max);

    std::vector<unsigned long int> subdivision(2);
    subdivision[0] = 200;
    subdivision[1] = 200;

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F((void*)(&mdv), &MolarDensity::gamma1_jet_test,
                  pmin, pmax,
                  subdivision,
                  F, JF);

    int rows, cols;
    rows = 1;
    cols = 2;

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

//    {
//        Fl_Double_Window *win = new Fl_Double_Window(10, 10, 800, 800);
//        {
//            Canvas *canvas = new Canvas(0, 0, win->w(), win->h());

//            std::vector<double> delta(2);
//            for (int i = 0; i < 2; i++) delta[i] = (pmax(i) - pmin(i))/(double)(subdivision[i] - 1);

//            std::vector<RealVector> vp;
//            for (int i = 0; i < subdivision[0]; i++){
//                RealVector p(2);
//                p(0) = pmin(0) + delta[0]*((double)i);
//                p(1) = pmin(1) + delta[1]*((double)1);

//                JetMatrix jm;
//                MolarDensity::gamma1_jet_test(&mdv, p, 1, jm);

//                p(1) = jm.function()(0);

//                vp.push_back(p);
//            }

//            Curve2D *c = new Curve2D(vp, 0.0, 0.0, 0.0);
//            canvas->add(c);
//        }
//        win->end();
//        win->resizable(win);

//        win->show();
//        return Fl::run();
//    }

    return 0;
}

int main_f2x1(){
    // Pmin, pmax.
    //
    RealVector pmin(2), pmax(2);

    pmin.component(0) = 0.0; // yw
    pmin.component(1) = 0.0;

    pmax.component(0) = 1.0;
    pmax.component(1) = 1.0;

    std::vector<unsigned long int> subdivision(2);
    subdivision[0] = 200;
    subdivision[1] = 200;

    // JetTester.
    //
    JetTester jt;
    
    MultiArray<RealVector> F;
    MultiArray<DoubleMatrix> JF;

    jt.populate_F(0, &f2x1,
                  pmin, pmax,
                  subdivision,
                  F, JF);

//    std::cout << F << std::endl;
//    std::cout << JF << std::endl;

    int rows, cols;
    rows = 1;
    cols = 2;

    DoubleMatrix numerical_analytic_abs_deviation_sup;
    double synthetic_deviation;
    double max_abs_F;

    jt.numerical_Jacobian(F, 
                          JF,
                          rows, cols, // rows and columns of the Jacobians, maybe there is a better way to do this.
                          1,
                          pmin, pmax,
                          subdivision,
                          numerical_analytic_abs_deviation_sup,
                          synthetic_deviation,
                          max_abs_F);

    std::cout << numerical_analytic_abs_deviation_sup << std::endl;
    std::cout << "synthetic_deviation = " << synthetic_deviation << std::endl;
    std::cout << "max_abs_F = " << max_abs_F << std::endl;
    std::cout << "numerical_analytic_abs_deviation_sup/max_abs_F = " << numerical_analytic_abs_deviation_sup*(1.0/max_abs_F) << std::endl;

    return 0;
}

int main(){
//    return main_thermo();
//    return main_thermo2d();

//    return main_flash();

//    return main_molar();
    return main_molar2x1();

//    return main_f2x1();
}

