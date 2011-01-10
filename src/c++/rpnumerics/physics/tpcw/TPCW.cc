/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCW.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TPCW.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


TPCW::TPCW(const FluxFunction & fluxFunction, const AccumulationFunction & accumulationFunction,const  Thermodynamics_SuperCO2_WaterAdimensionalized & thermo) : SubPhysics(fluxFunction, accumulationFunction, *new RectBoundary(RealVector(),RealVector()), *new Space("R3", 3), "TPCW",_GENERAL_ACCUMULATION_) {

    // Create Thermodynamics
    double Tref_rock = 273.15;
    double Tref_water = 274.3775;
    double pressure = 100.9;
    double Cr = 2.029e6;
    double Cw = 4297.;
    double rhoW_init = 998.2;
    double T_typical = 304.63;
    double Rho_typical = 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    double U_typical = 4.22e-6;
    double h_typical = Cw * (T_typical - Tref_water);


//     TD =thermo;
//     = new Thermodynamics_SuperCO2_WaterAdimensionalized(thermo);

    string rpnHome("/home/edsonlan/Java/rpn");

    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(rpnHome);
//    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(Tref_rock, Tref_water, pressure,
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmac_spline.txt",
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmaw_spline.txt",
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoac_spline.txt",
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoaw_spline.txt",
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoW_spline.txt",
//            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
//            rhoW_init,
//            Cr,
//            Cw,
//            T_typical,
//            Rho_typical,
//            h_typical,
//            U_typical);



//    int info = TD->status_after_init();



    cout <<"Calculo de U0[1] "<<TD->T2Theta(310.)<<endl;
    cout << "Calculo de U0[2] " << TD->u2U(4.22e-6) << endl;

//    printf("Thermodynamics  info = %d\n\n\n", info);

    // Create Horizontal & Vertical FracFlows
    double cnw = 0., cng = 0., expw = 2., expg = 2.;
    fh = new FracFlow2PhasesHorizontalAdimensionalized(cnw, cng, expw, expg, TD);
    fv = new FracFlow2PhasesVerticalAdimensionalized(cnw, cng, expw, expg, TD);

    // Create the Flux and its params
//    double abs_perm = 3e-12;
//    double sin_beta = 0.0;
//    double const_gravity = 9.8;
//    bool has_gravity = false, has_horizontal = true;
//
//    flux_params = new Flux2Comp2PhasesAdimensionalized_Params(abs_perm, sin_beta, const_gravity,
//            has_gravity, has_horizontal, TD, fh, fv);
//
//    flux = new Flux2Comp2PhasesAdimensionalized(*flux_params);

    // Create the Accum and its params
//    double phi = 0.38;
//    accum_params = new Accum2Comp2PhasesAdimensionalized_Params(TD, &phi);
//    accum = new Accum2Comp2PhasesAdimensionalized(*accum_params);
//    boundary_ = defaultBoundary();
//    ID_ = "TPCW";
//    space_ = new Space("R3", 3);
    boundary(*defaultBoundary());

}

TPCW::TPCW(const TPCW & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),*new Space("R3",3),"TPCW",_GENERAL_ACCUMULATION_) {

    // Create Thermodynamics
    double Tref_rock = 273.15;
    double Tref_water = 274.3775;
    double pressure = 100.9;
    double Cr = 2.029e6;
    double Cw = 4297.;
    double rhoW_init = 998.2;
    double T_typical = 304.63;
    double Rho_typical = 998.2; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    double U_typical = 4.22e-6;
    double h_typical = Cw * (T_typical - Tref_water);

    TD = new Thermodynamics_SuperCO2_WaterAdimensionalized(Tref_rock, Tref_water, pressure,
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmac_spline.txt",
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhosigmaw_spline.txt",
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoac_spline.txt",
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoaw_spline.txt",
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/rhoW_spline.txt",
            "/impa/home/f/edsonlan/Java/rpn/src/c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt",
            rhoW_init,
            Cr,
            Cw,
            T_typical,
            Rho_typical,
            h_typical,
            U_typical);

//    int info = TD->status_after_init();
//
//    printf("Thermodynamics  info = %d\n\n\n", info);

    // Create Horizontal & Vertical FracFlows
    double cnw = 0., cng = 0., expw = 2., expg = 2.;
    fh = new FracFlow2PhasesHorizontalAdimensionalized(cnw, cng, expw, expg, TD);
    fv = new FracFlow2PhasesVerticalAdimensionalized(cnw, cng, expw, expg, TD);

    // Create the Flux and its params
//    double abs_perm = 3e-12;
//    double sin_beta = 0.0;
//    double const_gravity = 9.8;
//    bool has_gravity = false, has_horizontal = true;

//    flux_params = new Flux2Comp2PhasesAdimensionalized_Params(abs_perm, sin_beta, const_gravity,
//            has_gravity, has_horizontal, TD, fh, fv);
//
//    flux = new Flux2Comp2PhasesAdimensionalized(*flux_params);

    // Create the Accum and its params
//    double phi = 0.38;
//    accum_params = new Accum2Comp2PhasesAdimensionalized_Params(TD, &phi);
//    accum = new Accum2Comp2PhasesAdimensionalized(*accum_params);
//    boundary_ = defaultBoundary();
//    ID_ = "TPCW";
//    space_ = new Space("R3", 3);
//    boundary(*defaultBoundary());
}

SubPhysics * TPCW::clone() const {

    return new TPCW(*this);
}

double TPCW::T2Theta(double T)const {
    return TD->T2Theta(T);
}

double TPCW::Theta2T(double theta)const {

    return TD->Theta2T(theta);
}

Boundary * TPCW::defaultBoundary()const {

    RealVector min(3);

    min.component(0) = 0;
    min.component(1) = T2Theta(304.63);

//    min.component(1) = 0;
    min.component(2) = TD->u2U(0);

//    min.component(2) = 0;

//    cout <<min.component(0)<<"<--------MIN 0"<<endl;
//    cout << min.component(1) << "<--------MIN 1" << endl;
//    cout << min.component(2) << "<--------MIN 2" << endl;



    RealVector max(3);

    max.component(0) = 1.0;
    max.component(1) = T2Theta(450);

//    max.component(1) = 1;
    max.component(2) = TD->u2U(2*4.22e-5);


//    max.component(2) = 1;
//cout <<"res "<<TD->u2U(4.22e-6)<<endl;

//    cout <<max.component(0)<<"<----------MAX 0"<<endl;
//    cout << max.component(1) << "<-------MAX 1" << endl;
//    cout << max.component(2) << "<------MAX 2" << endl;


    return new RectBoundary(min, max);

}

TPCW::~TPCW() {
    delete TD;
    delete fv;
    delete fh;
////    delete flux;
//    delete accum_params;
//    delete accum;
//    delete flux_params;
//    delete space_;
//    delete boundary_;
}

//const AccumulationFunction & TPCW::accumulation() const {
//
//    return *accum;
//}
//
//const Boundary & TPCW::boundary() const {
//
//    return * boundary_;
//
//
//}
//
//void TPCW::boundary(const Boundary & b) {
//    delete boundary_;
//    boundary_ = b.clone();
//}
//
//const FluxFunction & TPCW::fluxFunction() const {
//    return *flux;
//
//}
//
//void TPCW::fluxParams(const FluxParams & newParams) {
//    double abs_perm = 3e-12;
//    double sin_beta = 0.0;
//    double const_gravity = 9.8;
//    bool has_gravity = false, has_horizontal = true;
//
//    flux_params = new Flux2Comp2PhasesAdimensionalized_Params(abs_perm, sin_beta, const_gravity,
//            has_gravity, has_horizontal, TD, fh, fv);
//}
//
//void TPCW::accumulationParams(const AccumulationParams & newParams) {
//    double phi = 0.38;
//    accum_params = new Accum2Comp2PhasesAdimensionalized_Params(TD, &phi);
//}
//
//const Space & TPCW::domain() const {
//    return *space_;
//}
//
//const char * TPCW::ID() const {
//    return ID_;
//}

