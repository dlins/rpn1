#include <math.h>
#include <vector>

#include "Thermodynamics_SuperCO2_WaterAdimensionalized.h"
using namespace std;

// Generate a spline

//int Thermodynamics_SuperCO2_WaterAdimensionalized::create_spline(const char *name, const char *verify, double P, spline1dinterpolant &spline) {

std::string Thermodynamics_SuperCO2_WaterAdimensionalized::dataPath_ = std::string("/src/c++/rpnumerics/physics/tpcw/");

int Thermodynamics_SuperCO2_WaterAdimensionalized::create_spline(const std::string& name, const char *verify, double P, spline1dinterpolant &spline) {
    //    printf("Dentro de create spline\n");
    std::string temp(rpnHomePath_);
    //temp.append(dataPath_);
    //    std::cout << "Valor de temp: " << temp << "\n";

    // Open the file that contains the data needed for the creation of the spline
    FILE *fid;
    //    fid = fopen(rpnHome.append(name).c_str(), "r");

    fid = fopen(temp.append(name).c_str(), "r");

    if (fid == NULL) return SPLINE_ERROR;

    // Read the name of the variable and the pressure and verify them.
    char name_variable[100];
    fscanf(fid, "%s", name_variable);

    double Ptest;
    fscanf(fid, "%lf", &Ptest);

    if (strcmp(name_variable, verify) != 0 || Ptest != P) {
        printf("For %s: P = %f, should be %f\n", temp.c_str(), P, Ptest);
        fclose(fid);
        return SPLINE_ERROR;
    }

    // Read the number of base functions
    int n_base_func;
    fscanf(fid, "%d", &n_base_func);

    // Read the rest of the data
    int n;
    fscanf(fid, "%d", &n);

    ap::real_1d_array x, y;
    x.setlength(n);
    y.setlength(n);

    double xtemp, ytemp;

    for (int i = 0; i < n; i++) {
        fscanf(fid, "%lf %lf", &xtemp, &ytemp);
        x(i) = xtemp;
        y(i) = ytemp;
    }

    // Close the file with the data
    fclose(fid);

    // Generate the spline
    int spline_info;
    spline1dfitreport report;
    spline1dfitcubic(x, y, n, n_base_func,
            spline_info,
            spline,
            report);

    if (spline_info > 0) return SPLINE_OK;
    else return SPLINE_ERROR;
}

Thermodynamics_SuperCO2_WaterAdimensionalized::Thermodynamics_SuperCO2_WaterAdimensionalized(const Thermodynamics_SuperCO2_WaterAdimensionalized & copy) :
a0(copy.a0),
a1(copy.a1),
a2(copy.a2),
a3(copy.a3),
b0(copy.b0),
b1(copy.b1),
b2(copy.b2),
b3(copy.b3),
b4(copy.b4),
b5(copy.b5),
Tref_rock(copy.Tref_rock),
Tref_water(copy.Tref_water),
P(copy.P),
rhoW_const(copy.rhoW_const),
Rock_Cr(copy.Rock_Cr),
Water_Cw_specific(copy.Water_Cw_specific),
T_typical_(copy.T_typical_),
Rho_typical_(copy.Rho_typical_),
U_typical_(copy.U_typical_),
h_typical_(copy.h_typical_),
rpnHomePath_(copy.rpnHomePath_) {

    cout << "construtor de copia da termodinamica" << endl;
    
    cout<<"No ctr de copia da thermo:" <<T_typical_<<" "<<U_typical_<<" "<<Rho_typical_<<endl;
    
    
    // Generate the splines

    info_rhosigmac = create_spline("rhosigmac_spline.txt", "rhosigmac", P, rhosigmac_);
    info_rhosigmaw = create_spline("rhosigmaw_spline.txt", "rhosigmaw", P, rhosigmaw_);
    info_rhoac = create_spline("rhoac_spline.txt", "rhoac", P, rhoac_);
    info_rhoaw = create_spline("rhoaw_spline.txt", "rhoaw", P, rhoaw_);
    info_rhoW = create_spline("rhoW_spline.txt", "rhoW", P, rhoW_);
    info_hsigmaC = create_spline("hsigmaC_spline.txt", "hsigmaC", P, hsigmaC_);

}


// Convert from Theta to T

double Thermodynamics_SuperCO2_WaterAdimensionalized::Theta2T(double Theta) const{
    return Theta * T_typical_ + Tref_water;
}



// Convert from T to Theta

double Thermodynamics_SuperCO2_WaterAdimensionalized::T2Theta(double T)const {
    return (T - Tref_water) / T_typical_;
}


// Convert from U to u

double Thermodynamics_SuperCO2_WaterAdimensionalized::U2u(double U) const{
    return U*U_typical_;
}
// Convert from u to U

double Thermodynamics_SuperCO2_WaterAdimensionalized::u2U(double u) const{
    return u / U_typical_;
}

Thermodynamics_SuperCO2_WaterAdimensionalized::Thermodynamics_SuperCO2_WaterAdimensionalized(const std::string & rpnHomePath, double T_Typical, double Rho_Typical, double U_Typical) :
a0(-1.94760101098783e-6),
a1(0.013524080086578),
a2(-9.043578102452411),
a3(1.612763701298628e3),
b0(-0.0123274),
b1(27.1038),
b2(-23527.5),
b3(1.01425e7),
b4(-2.17342e9),
b5(1.86935e11),
Tref_rock(273.15),
Tref_water(274.3775),
P(100.9e5),
rhoW_const(998.2),
Rock_Cr(2.029e6),
Water_Cw_specific(4297.),
T_typical_(T_Typical),
Rho_typical_(Rho_Typical),
U_typical_(U_Typical),
rpnHomePath_(rpnHomePath) {

    h_typical_ = Water_Cw_specific * (T_typical_ - Tref_water);

    // Generate the splines

    info_rhosigmac = create_spline("rhosigmac_spline.txt", "rhosigmac", P, rhosigmac_);
    info_rhosigmaw = create_spline("rhosigmaw_spline.txt", "rhosigmaw", P, rhosigmaw_);
    info_rhoac = create_spline("rhoac_spline.txt", "rhoac", P, rhoac_);
    info_rhoaw = create_spline("rhoaw_spline.txt", "rhoaw", P, rhoaw_);
    info_rhoW = create_spline("rhoW_spline.txt", "rhoW", P, rhoW_);
    info_hsigmaC = create_spline("hsigmaC_spline.txt", "hsigmaC", P, hsigmaC_);


    std::cout << "Status: " << status_after_init() << std::endl;

}
// DTOR (Destructor)

Thermodynamics_SuperCO2_WaterAdimensionalized::~Thermodynamics_SuperCO2_WaterAdimensionalized() {
}

// Verify if the splines were created correctly. This function
// should be called immediatly after the constructor.

int Thermodynamics_SuperCO2_WaterAdimensionalized::status_after_init(void) {
    if (info_rhosigmac != SPLINE_OK) return 1;
    else if (info_rhosigmaw != SPLINE_OK) return 2;
    else if (info_rhoac != SPLINE_OK) return 3;
    else if (info_rhoaw != SPLINE_OK) return 4;
    else if (info_rhoW != SPLINE_OK) return 5;
    else if (info_hsigmaC != SPLINE_OK) return 6;
    else return SPLINE_OK;
}

// RockEnthalpyVol & Diff_RockEnthalpyVol

double Thermodynamics_SuperCO2_WaterAdimensionalized::RockEnthalpyVol(double Theta) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    return Rock_Cr * (T - Tref_rock) / (Rho_typical_ * h_typical_); // Hr_D = Hr/(Rho_typical_*h_typical_).
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_RockEnthalpyVol(double Theta, double &Hr, double &d_Hr, double &d2_Hr) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    Hr = Rock_Cr * (T - Tref_rock) / (Rho_typical_ * h_typical_); // Hr_D = Hr/(Rho_typical_*h_typical_).
    d_Hr = Rock_Cr * T_typical_ / (Rho_typical_ * h_typical_); // d_Hr_D = (d_Hr/(Rho_typical_*h_typical_) )* T_ref
    d2_Hr = 0; // d2_Hr_D = (d2_Hr/(Rho_typical_*h_typical_) )* T_ref^2
    return;
}
// AqueousEnthalpyVol & Diff_AqueousEnthalpyVol Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::AqueousEnthalpyVol(double Theta) {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    return (Water_Cw_specific * (T - Tref_water) / h_typical_)*Rhoaw(Theta); // Ha_D(Theta) = rhoaw_D(Theta) * h_W_D(Theta)
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_AqueousEnthalpyVol(double Theta, double &Ha, double &d_Ha, double &d2_Ha)const {
    double rho, d_rho, d2_rho;
    Diff_Rhoaw(Theta, rho, d_rho, d2_rho);

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    Ha = rho * Water_Cw_specific * (T - Tref_water) / h_typical_; // rhoaw_D(Theta)*h_W_D(Theta)
    d_Ha = d_rho * Water_Cw_specific * (T - Tref_water) / h_typical_ + rho * Water_Cw_specific * T_typical_ / h_typical_; // d_Ha_D  = d_rhoaw_D*h_W_D + rhoaw_D*d_h_W_D
    d2_Ha = d2_rho * Water_Cw_specific * (T - Tref_water) / h_typical_ + 2 * d_rho * Water_Cw_specific * T_typical_ / h_typical_; // d2_rhoaw_D*h_W_D +2*d_rhoaw_D*d_h_W_D + ( rhoaw_D*d2h_W_D...=0 )
    return;
}


// SuperCriticEnthalpyVol & Diff_SuperCriticEnthalpyVol Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::SuperCriticEnthalpyVol(double Theta)const {

    return Rhosic(Theta) * hsigmaC(Theta); // Hsi_D = Hsigma_D
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_SuperCriticEnthalpyVol(double Theta, double &Hsi, double &d_Hsi, double &d2_Hsi) const{
    double rho, d_rho, d2_rho;
    Diff_Rhosic(Theta, rho, d_rho, d2_rho);

    double h, d_h, d2_h;
    Diff_hsigmaC(Theta, h, d_h, d2_h);

    Hsi = rho*h; //  H_sigma_D=rho_sigmac_D*h_sigmaC_D
    d_Hsi = d_rho * h + rho*d_h; //  d_H_sigma_D=d_rho_sigmac_D*h_sigmaC_D + rho_sigmac_D*d_h_sigmaC_D
    d2_Hsi = d2_rho * h + 2 * d_rho * d_h + rho*d2_h; //  d2_rho_sigmac_D*h_sigmaC_D + 2*d_rho_sigmac_D*d_h_sigmaC_D + rho_sigmac_D*d2_h_sigmaC_D

    return;
}

// Rhosic & Diff_Rhosic Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::Rhosic(double Theta) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    return spline1dcalc(rhosigmac_, T) / Rho_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_Rhosic(double Theta, double &rho, double &d_rho, double &d2_rho)const {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    spline1ddiff(rhosigmac_, T, rho, d_rho, d2_rho);
    rho *= (1. / Rho_typical_); // Rho_D(Theta)   = Rho(T(Theta))/Rho_typical_ .
    d_rho *= (T_typical_ / Rho_typical_); // d_Rho_D(Theta) = ( d_Rho(T(Theta)  )/Rho_typical_ )*T_typical_
    d2_rho *= (T_typical_ * T_typical_ / Rho_typical_); // Rho_D(Theta)   = ( d2_Rho(T(Theta) )/Rho_typical_ )*(T_typical_^2) .

    return;
}

// Rhosiw & Diff_Rhosiw Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::Rhosiw(double Theta)const {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    return spline1dcalc(rhosigmaw_, T) / Rho_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_Rhosiw(double Theta, double &rho, double &d_rho, double &d2_rho)const {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    spline1ddiff(rhosigmaw_, T, rho, d_rho, d2_rho);
    rho *= (1. / Rho_typical_);
    d_rho *= (T_typical_ / Rho_typical_);
    d2_rho *= (T_typical_ * T_typical_ / Rho_typical_);

    return;
}

// Rhoac & Diff_Rhoac Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::Rhoac(double Theta) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    return spline1dcalc(rhoac_, T) / Rho_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_Rhoac(double Theta, double &rho, double &d_rho, double &d2_rho)const {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    spline1ddiff(rhoac_, T, rho, d_rho, d2_rho);
    rho *= (1. / Rho_typical_);
    d_rho *= (T_typical_ / Rho_typical_);
    d2_rho *= (T_typical_ * T_typical_ / Rho_typical_);

    return;
}

// Rhoaw & Diff_Rhoaw Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::Rhoaw(double Theta) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    return spline1dcalc(rhoaw_, T) / Rho_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_Rhoaw(double Theta, double &rho, double &d_rho, double &d2_rho)const {
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    spline1ddiff(rhoaw_, T, rho, d_rho, d2_rho);
    rho *= (1. / Rho_typical_);
    d_rho *= (T_typical_ / Rho_typical_);
    d2_rho *= (T_typical_ * T_typical_ / Rho_typical_);
    return;
}

// RhoW

double Thermodynamics_SuperCO2_WaterAdimensionalized::RhoW()const {
    return rhoW_const;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_RhoW(double Theta, double &rho, double &d_rho, double &d2_rho)const {
    rho = rhoW_const;
    d_rho = d2_rho = 0; // remember to change this for the gravity model together with its adimensionalization!!!!
    return;
}

// hsigmaC & Diff_hsigmaC Adimensionalized

double Thermodynamics_SuperCO2_WaterAdimensionalized::hsigmaC(double Theta) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    return spline1dcalc(hsigmaC_, T) / h_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::Diff_hsigmaC(double Theta, double &h, double &d_h, double &d2_h) const{
    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    spline1ddiff(hsigmaC_, T, h, d_h, d2_h);
    h *= (1. / h_typical_); // CAREFUL THIS ADIMENSIONALIZATION IS WITH RESPECT TO ENTHALPY
    d_h *= (T_typical_ / h_typical_);
    d2_h *= (T_typical_ * T_typical_ / h_typical_);
    return;
}

// Return the value of the constants

double Thermodynamics_SuperCO2_WaterAdimensionalized::Cr(void) const{
    return Rock_Cr;
}

double Thermodynamics_SuperCO2_WaterAdimensionalized::Cw_specific(void) const{
    return Water_Cw_specific;
}
// We do not need to make the dimesion-less analysis for the viscosities.

void Thermodynamics_SuperCO2_WaterAdimensionalized::inv_muw(double T, double &nuw, double &dnuw_dT, double &d2nuw_dT2)const {

    double inv_T = 1. / T;
    double inv_T2 = inv_T*inv_T;
    double inv_T3 = inv_T2*inv_T;
    double inv_T4 = inv_T3*inv_T;
    double inv_T5 = inv_T4*inv_T;
    double inv_T6 = inv_T5*inv_T;
    double inv_T7 = inv_T6*inv_T;

    double muw = (b0 + b1 * inv_T + b2 * inv_T2 + b3 * inv_T3 + b4 * inv_T4 + b5 * inv_T5);
    double dmuw_dT = -(b1 * inv_T2 + 2. * b2 * inv_T3 + 3. * b3 * inv_T4 + 4. * b4 * inv_T5 + 5. * b5 * inv_T6);
    double d2muw_dT2 = 2. * b1 * inv_T3 + 6. * b2 * inv_T4 + 12. * b3 * inv_T5 + 20. * b4 * inv_T6 + 30. * b5*inv_T7;

    nuw = 1. / muw;
    double inv_muw2 = nuw*nuw;
    double inv_muw3 = inv_muw2*nuw;

    dnuw_dT = -dmuw_dT*inv_muw2;
    d2nuw_dT2 = 2. * dmuw_dT * dmuw_dT * inv_muw3 - d2muw_dT2*inv_muw2;

    return;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::muw(double T, double &muw, double &dmuw_dT, double &d2muw_dT2) {

    double inv_T = 1. / T;
    double inv_T2 = inv_T*inv_T;
    double inv_T3 = inv_T2*inv_T;
    double inv_T4 = inv_T3*inv_T;
    double inv_T5 = inv_T4*inv_T;
    double inv_T6 = inv_T5*inv_T;
    double inv_T7 = inv_T6*inv_T;

    muw = (b0 + b1 * inv_T + b2 * inv_T2 + b3 * inv_T3 + b4 * inv_T4 + b5 * inv_T5);
    dmuw_dT = -(b1 * inv_T2 + 2. * b2 * inv_T3 + 3. * b3 * inv_T4 + 4. * b4 * inv_T5 + 5. * b5 * inv_T6);
    d2muw_dT2 = 2. * b1 * inv_T3 + 6. * b2 * inv_T4 + 12. * b3 * inv_T5 + 20. * b4 * inv_T6 + 30. * b5*inv_T7;

    return;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::inv_mug(double T, double &nug, double &dnug_dT, double &d2nug_dT2)const {

    double nug_den = 1. / (a0 * T * T * T + a1 * T * T + a2 * T + a3);
    nug = 1. / (1e-6 * (a0 * T * T * T + a1 * T * T + a2 * T + a3));
    double nug_denSQ = nug_den*nug_den;

    double dmug_dT = 3. * a0 * T * T + 2. * a1 * T + a2;
    double dmug_dTSQ = dmug_dT*dmug_dT;

    dnug_dT = -1e6 * nug_denSQ*dmug_dT;
    d2nug_dT2 = 1e6 * (2. * nug_denSQ * nug_den * dmug_dTSQ - nug_denSQ * (6. * T * a0 + 2. * a1));

    return;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::mug(double T, double &mug, double &dmug_dT, double &d2mug_dT2)const {

    mug = 1e-6 * (a0 * T * T * T + a1 * T * T + a2 * T + a3);
    dmug_dT = 1e-6 * (3. * a0 * T * T + 2. * a1 * T + a2);
    d2mug_dT2 = 1e-6 * (6. * a0 + 2. * a1);

    return;
}

double Thermodynamics_SuperCO2_WaterAdimensionalized::T_typical(void)const {
    return T_typical_;
}

double Thermodynamics_SuperCO2_WaterAdimensionalized::U_typical(void)const {
    return U_typical_;
}

void Thermodynamics_SuperCO2_WaterAdimensionalized::setTtypical(double newValue) {

    T_typical_ = newValue;

}

void Thermodynamics_SuperCO2_WaterAdimensionalized::setRhoTypical(double newValue) {

    Rho_typical_ = newValue;

}

void Thermodynamics_SuperCO2_WaterAdimensionalized::UTypical(double newValue) {

    U_typical_ = newValue;
}
