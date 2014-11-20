#ifndef _THERMODYNAMICS_SUPERCO2_WATER_SINGLEPHASES_ADIMENSIONALIZED_
#define _THERMODYNAMICS_SUPERCO2_WATER_SINGLEPHASES_ADIMENSIONALIZED_

#include <stdio.h>

#include "JetSinglePhaseLiquid.h"
#include "JetSinglePhaseVapor.h"

#include "spline1d.h"
#include <string.h>

#ifndef SPLINE_OK
#define SPLINE_OK    0
#endif

#ifndef SPLINE_ERROR
#define SPLINE_ERROR 1
#endif

class Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized {
    private:
        JetSinglePhaseLiquid *liquid;
        JetSinglePhaseVapor  *vapor;

        // Splines
        spline1dinterpolant hsigmaC_;

        // Info on the creation of the splines
        int info_hsigmaC;

        double Tref_rock, Tref_water, P;
        double Rock_Cr;  // [J/(m*m*m*K)] Care should be taken: Cr and Cw display different units.
        double Cw_; // == Water_Cw_specific [J/(kg*K)]

        // Typical values for the adimensionalization
        double T_typical_;
        double Rho_typical_; // For the time being, this will be RhoWconst = 998 [kg/m^3]. In the future, this value should be the density of pure water at the temperature T_typical.
        double U_typical_;
        double h_typical_;

        // Create a spline
        int create_spline(const char*, const char*, double, spline1dinterpolant&);

        int hW_jet(const double Theta, int degree, JetMatrix &hWj);

        int hsigmaC_jet(const double Theta, int degree, JetMatrix &hsigmaCj);
    protected:
    public:
        Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized(double mc, double mw, double Press, 
                                                                    const char *hsigmaC_name,
                                                                    double Tr, double Tw,
                                                                    double Cr,
                                                                    double cw,
                                                                    double T_typical,
                                                                    double Rho_typical,
                                                                    double U_typical,
                                                                    double h_typical);

        ~Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized();

        int status_after_init(void);

        int RockEnthalpyVol_jet(const double Theta, int degree, JetMatrix &revj);

        int SuperCriticEnthalpyVol_jet(const double yw, const double Theta, int degree, JetMatrix &Hsij);

        int Rhosic_jet(const double yw, const double Theta, int degree, JetMatrix &rhosicj);

        int Rhosiw_jet(const double yw, const double Theta, int degree, JetMatrix &rhosiwj);

        int AqueousEnthalpyVol_jet(const double xc, const double Theta, int degree, JetMatrix &Haj);

        int Rhoac_jet(const double xc, const double Theta, int degree, JetMatrix &rhoacj);

        int Rhoaw_jet(const double xc, const double Theta, int degree, JetMatrix &rhoawj);

        double Cr(void);

        double Cw(void);

        double T_typical(void); // created for making values available in other classes.

        double U_typical(void);

        // Convert from Theta to T (dimensionless -> dimensioned)
        double Theta2T(double);

        // Convert from T to Theta (dimensioned -> dimensionless)
        double T2Theta(double);

        // Convert from U to u (dimensionless -> dimensioned)
        double U2u(double);

        // Convert from u to U (dimensioned -> dimensionless)
        double u2U(double);
};

#endif // _THERMODYNAMICS_SUPERCO2_WATER_SINGLEPHASES_ADIMENSIONALIZED_

