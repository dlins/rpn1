#ifndef _THERMODYNAMICS_SUPERCO2_WATERADIMENSIONALIZED_
#define _THERMODYNAMICS_SUPERCO2_WATERADIMENSIONALIZED_

//#include <stdio.h>
//#include <stdlib.h>

#include <string.h>
//#include<string>
#include "spline1d.h"
#include <iostream>


#define SPLINE_OK    0
#define SPLINE_ERROR 1

// TODO: Check that T is within the interval

class Thermodynamics_SuperCO2_WaterAdimensionalized {
private:
    // Some constants
    double Tref_rock, Tref_water, P;
    double rhoW_const;
    double Rock_Cr; // [J/(m*m*m*K)] Care should be taken: Cr and Cw display different units.
    double Water_Cw_specific; // [J/(kg*K)]

    std::string rpnHomePath_;
    static std::string dataPath_;

    // Splines
    spline1dinterpolant rhosigmac_, rhosigmaw_, rhoac_, rhoaw_, rhoW_, hsigmaC_;

    // Info on the creation of the splines
    int info_rhosigmac, info_rhosigmaw, info_rhoac, info_rhoaw, info_rhoW, info_hsigmaC;

    // Create a spline
    int create_spline(const std::string &, const char*, double, spline1dinterpolant&);

    // Some constants for mug
    const double a0, a1, a2, a3;

    // some constants for muw
    const double b0, b1, b2, b3, b4, b5;

    // Typical values for the adimensionalization
    double T_typical_;
    double Rho_typical_; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
    double U_typical_;
    double h_typical_; // Specific enthalpy of pure water at the temperature T_typical


protected:
public:
    Thermodynamics_SuperCO2_WaterAdimensionalized(const Thermodynamics_SuperCO2_WaterAdimensionalized &);

    Thermodynamics_SuperCO2_WaterAdimensionalized(const std::string &,double,double,double );

    virtual ~Thermodynamics_SuperCO2_WaterAdimensionalized();

    int status_after_init(void); 

    double RockEnthalpyVol(double)const;
    void Diff_RockEnthalpyVol(double, double&, double&, double&)const;

    double AqueousEnthalpyVol(double);
    void Diff_AqueousEnthalpyVol(double, double&, double&, double&)const;

    double SuperCriticEnthalpyVol(double)const;
    void Diff_SuperCriticEnthalpyVol(double, double&, double&, double&)const;

    double Rhosic(double)const;
    void Diff_Rhosic(double, double&, double&, double&)const;

    double Rhosiw(double)const;
    void Diff_Rhosiw(double, double&, double&, double&)const;

    double Rhoac(double)const;
    void Diff_Rhoac(double, double&, double&, double&)const;

    double Rhoaw(double)const;
    void Diff_Rhoaw(double, double&, double&, double&)const;

    double RhoW()const;
    void Diff_RhoW(double, double&, double&, double&)const;

    double hsigmaC(double)const;
    void Diff_hsigmaC(double, double&, double&, double&)const;




    void inv_muw(double T, double &nuw, double &dnuw_dT, double &d2nuw_dT2)const; // Water viscosity
    void muw(double T, double &muw, double &dmuw_dT, double &d2muw_dT2); // Water viscosity

    void inv_mug(double T, double &nug, double &dnug_dT, double &d2nug_dT2)const; // Gas   viscosity
    void mug(double T, double &mug, double &dmug_dT, double &d2mug_dT2)const; // Gas   viscosity







    void setTtypical(double);
    void setRhoTypical(double);
    void UTypical (double);



    double Cr(void)const;
    double Cw_specific(void)const;
    // Convert from Theta to T (dimensionless -> dimensioned)
    double Theta2T(double)const;
    // Convert from T to Theta (dimensioned -> dimensionless)
    double T2Theta(double)const;
    // Convert from U to u (dimensionless -> dimensioned)
    double U2u(double)const;
    // Convert from u to U (dimensioned -> dimensionless)
    double u2U(double)const;
    double T_typical(void)const;
    double U_typical(void)const;

};

#endif // _THERMODYNAMICS_SUPERCO2_WATERADIMENSIONALIZED_

