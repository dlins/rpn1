#include "Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized.h"

// Generate a spline
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::create_spline(const char *name, const char *verify, double P, spline1dinterpolant &spline){
    // Open the file that contains the data needed for the creation of the spline
    FILE *fid;
    fid = fopen(name, "r");
    if (fid == NULL) return SPLINE_ERROR;

    // Read the name of the variable and the pressure and verify them.
    char name_variable[100]; 
    fscanf(fid, "%s", name_variable);

    double Ptest;
    fscanf(fid, "%lf", &Ptest);

    if (strcmp(name_variable, verify) != 0 || Ptest != P){
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

    for (int i = 0; i < n; i++){
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

// Verify if the splines were created correctly. This function
// should be called immediatly after the constructor.
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::status_after_init(void){
    if (info_hsigmaC != SPLINE_OK) return 1;
    else return SPLINE_OK;
}

Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized(double mc, double mw, double Press, 
                                                                                                                         const char *hsigmaC_name,
                                                                                                                         double Tr, double Tw,
                                                                                                                         double Cr,
                                                                                                                         double cw,
                                                                                                                         double T_typical,
                                                                                                                         double Rho_typical,
                                                                                                                         double U_typical,
                                                                                                                         double h_typical){
    
    Tref_rock = Tr; Tref_water = Tw;
    Rock_Cr = Cr;
    Cw_ = cw;
    T_typical_   = T_typical;
    Rho_typical_ = Rho_typical;
    U_typical_   = U_typical;
    h_typical_   = h_typical;

    liquid = new JetSinglePhaseLiquid(mc, mw, Press);
    vapor  = new JetSinglePhaseVapor(mc, mw, Press);

    info_hsigmaC = create_spline(hsigmaC_name, "hsigmaC", Press, hsigmaC_);
}

Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::~Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized(){
    delete vapor;
    delete liquid;
}

// RockEnthalpyVol
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::RockEnthalpyVol_jet(const double Theta, int degree, JetMatrix &revj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    if (degree >= 0){
        double rev = Rock_Cr*(T - Tref_rock)/(Rho_typical_*h_typical_);

        revj.set(0, rev);

        if (degree >= 1){
            double drev_dTheta= Rock_Cr*T_typical_/(Rho_typical_*h_typical_);

            revj.set(0, 0, drev_dTheta);

            if (degree == 2){
                double d2rev_dTheta2 = 0.0;

                revj.set(0, 0, 0, d2rev_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

//SuperCriticEnthalpyVol
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::SuperCriticEnthalpyVol_jet(const double yw, const double Theta, int degree, JetMatrix &Hsij){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE

    JetMatrix rhosicj(2);
    Rhosic_jet(yw, Theta, degree, rhosicj);

    JetMatrix hsigmaCj(1);
    hsigmaC_jet(Theta, degree, hsigmaCj);

    if (degree >= 0){
        double rhosic  = rhosicj.get(0);
        double hsigmaC = hsigmaCj.get(0);

        double Hsi = rhosic*hsigmaC;

        Hsij.set(0, Hsi);

        if (degree >= 1){
            double drhosic_dyw     = rhosicj.get(0, 0);
            double drhosic_dTheta  = rhosicj.get(0, 1);
            double dhsigmaC_dTheta = hsigmaCj.get(0, 0);

            double dHsi_dyw    = drhosic_dyw*hsigmaC;
            double dHsi_dTheta = drhosic_dTheta*hsigmaC + rhosic*dhsigmaC_dTheta;

            Hsij.set(0, 0, dHsi_dyw);
            Hsij.set(0, 1, dHsi_dTheta);

            if (degree == 2){
                double d2rhosic_dyw2      = rhosicj.get(0, 0, 0);
                double d2rhosic_dywdTheta = rhosicj.get(0, 0, 1);
                double d2rhosic_dThetadyw = rhosicj.get(0, 1, 0);
                double d2rhosic_dTheta2   = rhosicj.get(0, 1, 1);

                double d2hsigmaC_dTheta2  = hsigmaCj.get(0, 0, 0);

                double d2Hsi_dyw2      = d2rhosic_dyw2*hsigmaC;
                double d2Hsi_dywdTheta = d2rhosic_dywdTheta*hsigmaC + drhosic_dyw*dhsigmaC_dTheta;
                double d2Hsi_dThetadyw = d2Hsi_dywdTheta;
                double d2Hsi_dTheta2   = d2rhosic_dTheta2*hsigmaC + 2.0*drhosic_dTheta*dhsigmaC_dTheta +
                                         rhosic*d2hsigmaC_dTheta2;

                Hsij.set(0, 0, 0, d2Hsi_dyw2);
                Hsij.set(0, 0, 1, d2Hsi_dywdTheta);
                Hsij.set(0, 1, 0, d2Hsi_dThetadyw);
                Hsij.set(0, 1, 1, d2Hsi_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// Pure water entalpy
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::hW_jet(const double Theta, int degree, JetMatrix &hWj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    if (degree >= 0){
        double hW = Cw_*(T - Tref_water);

        hWj.set(0, hW/h_typical_);

        if (degree >= 1){
            double dhW_dT = Cw_;

            hWj.set(0, 0, dhW_dT*T_typical_/h_typical_);

            if (degree == 2){
                double d2hW_dT2 = 0.0;

                hWj.set(0, 0, 0, d2hW_dT2*T_typical_*T_typical_/h_typical_);
            }
        }
    }

    return 2; // SUCCESSFUL_PROCEDURE;
}

// AqueousEnthalpyVol
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::AqueousEnthalpyVol_jet(const double xc, const double Theta, int degree, JetMatrix &Haj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    JetMatrix rhoawj(2);
    Rhoaw_jet(xc, Theta, degree, rhoawj);

    JetMatrix hWj(1);
    hW_jet(Theta, degree, hWj);

    if (degree >= 0){
        double rhoaw = rhoawj.get(0);
        double hW    = hWj.get(0);

        double Ha = rhoaw*hW;

        Haj.set(0, Ha);

        if (degree >= 1){
            double drhoaw_dxc    = rhoawj.get(0, 0);
            double drhoaw_dTheta = rhoawj.get(0, 1);
            double dhW_dTheta    = hWj.get(0, 0);

            double dHa_dxc    = drhoaw_dxc*hW;
            double dHa_dTheta = drhoaw_dTheta*hW + rhoaw*dhW_dTheta;

            Haj.set(0, 0, dHa_dxc);
            Haj.set(0, 1, dHa_dTheta);

            if (degree == 2){
                double d2rhoaw_dxc2      = rhoawj.get(0, 0, 0);
                double d2rhoaw_dxcdTheta = rhoawj.get(0, 0, 1);
                double d2rhoaw_dThetadxc = rhoawj.get(0, 1, 0);
                double d2rhoaw_dTheta2   = rhoawj.get(0, 1, 1);
                double d2hW_dTheta2      = hWj.get(0, 0, 0);

                double d2Ha_dxc2      = d2rhoaw_dxc2*hW;
                double d2Ha_dxcdTheta = d2rhoaw_dxcdTheta*hW + drhoaw_dxc*dhW_dTheta;
                double d2Ha_dThetadxc = d2Ha_dxcdTheta;
                double d2Ha_dTheta2   = d2rhoaw_dTheta2*hW + 2.0*drhoaw_dTheta*dhW_dTheta + 
                                        rhoaw*d2hW_dTheta2;

                Haj.set(0, 0, 0, d2Ha_dxc2);
                Haj.set(0, 0, 1, d2Ha_dxcdTheta);
                Haj.set(0, 1, 0, d2Ha_dThetadxc);
                Haj.set(0, 1, 1, d2Ha_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// hsigmaC
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::hsigmaC_jet(const double Theta, int degree, JetMatrix &hsigmaCj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water
    double h, d_h, d2_h;
    spline1ddiff(hsigmaC_, T, h, d_h, d2_h);

    if (degree >= 0){
        hsigmaCj.set(0, h/h_typical_);

        if (degree >= 1){
            hsigmaCj.set(0, 0, d_h*T_typical_/h_typical_);

            if (degree == 2){
                hsigmaCj.set(0, 0, 0, d2_h*T_typical_*T_typical_/h_typical_);
            }
        }
    }

    return 2; // SUCCESSFUL_PROCEDURE;
}

// Rhosic
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Rhosic_jet(const double yw, const double Theta, int degree, JetMatrix &rhosicj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    JetMatrix rhosigmacj(2);
    vapor->rhosigmac_jet(yw, T, degree, rhosigmacj);

    if (degree >= 0){
        double rhosigmac = rhosigmacj.get(0);

        double rhosic = rhosigmac/Rho_typical_;

        rhosicj.set(0, rhosic);

        if (degree >= 1){
            double drhosigmac_dyw = rhosigmacj.get(0, 0);
            double drhosigmac_dT  = rhosigmacj.get(0, 1);

            double drhosic_dyw, drhosic_dTheta;

            drhosic_dyw = drhosigmac_dyw/Rho_typical_;

            drhosic_dTheta = drhosigmac_dT*T_typical_/Rho_typical_;

            rhosicj.set(0, 0, drhosic_dyw);
            rhosicj.set(0, 1, drhosic_dTheta);

            if (degree == 2){
                double d2rhosigmac_dyw2  = rhosigmacj.get(0, 0, 0);
                double d2rhosigmac_dywdT = rhosigmacj.get(0, 0, 1);
                double d2rhosigmac_dTdyw = rhosigmacj.get(0, 1, 0);
                double d2rhosigmac_dT2   = rhosigmacj.get(0, 1, 1);

                double d2rhosic_dyw2, d2rhosic_dywdTheta, d2rhosic_dThetadyw, d2rhosic_dTheta2;

                d2rhosic_dyw2      = d2rhosigmac_dyw2/Rho_typical_;
                d2rhosic_dywdTheta = d2rhosigmac_dywdT*T_typical_/Rho_typical_;
                d2rhosic_dThetadyw = d2rhosic_dywdTheta;
                d2rhosic_dTheta2   = d2rhosigmac_dT2*T_typical_*T_typical_/Rho_typical_;

                rhosicj.set(0, 0, 0, d2rhosic_dyw2);
                rhosicj.set(0, 0, 1, d2rhosic_dywdTheta);
                rhosicj.set(0, 1, 0, d2rhosic_dThetadyw);
                rhosicj.set(0, 1, 1, d2rhosic_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// Rhosiw
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Rhosiw_jet(const double yw, const double Theta, int degree, JetMatrix &rhosiwj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    JetMatrix rhosigmawj(2);
    vapor->rhosigmaw_jet(yw, T, degree, rhosigmawj);

    if (degree >= 0){
        double rhosigmaw = rhosigmawj.get(0);

        double rhosiw = rhosigmaw/Rho_typical_;

        rhosiwj.set(0, rhosiw);

        if (degree >= 1){
            double drhosigmaw_dyw = rhosigmawj.get(0, 0);
            double drhosigmaw_dT  = rhosigmawj.get(0, 1);

            double drhosiw_dyw, drhosiw_dTheta;

            drhosiw_dyw = drhosigmaw_dyw/Rho_typical_;

            drhosiw_dTheta = drhosigmaw_dT*T_typical_/Rho_typical_;

            rhosiwj.set(0, 0, drhosiw_dyw);
            rhosiwj.set(0, 1, drhosiw_dTheta);

            if (degree == 2){
                double d2rhosigmaw_dyw2  = rhosigmawj.get(0, 0, 0);
                double d2rhosigmaw_dywdT = rhosigmawj.get(0, 0, 1);
                double d2rhosigmaw_dTdyw = rhosigmawj.get(0, 1, 0);
                double d2rhosigmaw_dT2   = rhosigmawj.get(0, 1, 1);

                double d2rhosiw_dyw2, d2rhosiw_dywdTheta, d2rhosiw_dThetadyw, d2rhosiw_dTheta2;

                d2rhosiw_dyw2      = d2rhosigmaw_dyw2/Rho_typical_;
                d2rhosiw_dywdTheta = d2rhosigmaw_dywdT*T_typical_/Rho_typical_;
                d2rhosiw_dThetadyw = d2rhosiw_dywdTheta;
                d2rhosiw_dTheta2   = d2rhosigmaw_dT2*T_typical_*T_typical_/Rho_typical_;

                rhosiwj.set(0, 0, 0, d2rhosiw_dyw2);
                rhosiwj.set(0, 0, 1, d2rhosiw_dywdTheta);
                rhosiwj.set(0, 1, 0, d2rhosiw_dThetadyw);
                rhosiwj.set(0, 1, 1, d2rhosiw_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// Rhoac
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Rhoac_jet(const double xc, const double Theta, int degree, JetMatrix &rhoacj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    JetMatrix liquid_rhoacj(2);
    liquid->rhoac_jet(xc, T, degree, liquid_rhoacj);

    if (degree >= 0){
        double liquid_rhoac = liquid_rhoacj.get(0);

        double rhoac = liquid_rhoac/Rho_typical_;

        rhoacj.set(0, rhoac);

        if (degree >= 1){
            double dliquid_rhoac_dxc = liquid_rhoacj.get(0, 0);
            double dliquid_rhoac_dT  = liquid_rhoacj.get(0, 1);

            double drhoac_dxc, drhoac_dTheta;

            drhoac_dxc    = dliquid_rhoac_dxc/Rho_typical_;

            drhoac_dTheta = dliquid_rhoac_dT*T_typical_/Rho_typical_;

            rhoacj.set(0, 0, drhoac_dxc);
            rhoacj.set(0, 1, drhoac_dTheta);

            if (degree == 2){
                double d2liquid_rhoac_dxc2  = liquid_rhoacj.get(0, 0, 0);
                double d2liquid_rhoac_dxcdT = liquid_rhoacj.get(0, 0, 1);
                double d2liquid_rhoac_dTdxc = liquid_rhoacj.get(0, 1, 0);
                double d2liquid_rhoac_dT2   = liquid_rhoacj.get(0, 1, 1);

                double d2rhoac_dxc2, d2rhoac_dxcdTheta, d2rhoac_dThetadxc, d2rhoac_dTheta2;

                d2rhoac_dxc2      = d2liquid_rhoac_dxc2/Rho_typical_;
                d2rhoac_dxcdTheta = d2liquid_rhoac_dxcdT*T_typical_/Rho_typical_;
                d2rhoac_dThetadxc = d2rhoac_dxcdTheta;
                d2rhoac_dTheta2   = d2liquid_rhoac_dT2*T_typical_*T_typical_/Rho_typical_;

                rhoacj.set(0, 0, 0, d2rhoac_dxc2);
                rhoacj.set(0, 0, 1, d2rhoac_dxcdTheta);
                rhoacj.set(0, 1, 0, d2rhoac_dThetadxc);
                rhoacj.set(0, 1, 1, d2rhoac_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// Rhoaw
//
int Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Rhoaw_jet(const double xc, const double Theta, int degree, JetMatrix &rhoawj){
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE;

    double T = Theta2T(Theta); // T = Theta*T_typical_ + Tref_water

    JetMatrix liquid_rhoawj(2);
    liquid->rhoaw_jet(xc, T, degree, liquid_rhoawj);

    if (degree >= 0){
        double liquid_rhoaw = liquid_rhoawj.get(0);

        double rhoaw = liquid_rhoaw/Rho_typical_;

        rhoawj.set(0, rhoaw);

        if (degree >= 1){
            double dliquid_rhoaw_dxc = liquid_rhoawj.get(0, 0);
            double dliquid_rhoaw_dT  = liquid_rhoawj.get(0, 1);

            double drhoaw_dxc, drhoaw_dTheta;

            drhoaw_dxc    = dliquid_rhoaw_dxc/Rho_typical_;

            drhoaw_dTheta = dliquid_rhoaw_dT*T_typical_/Rho_typical_;

            rhoawj.set(0, 0, drhoaw_dxc);
            rhoawj.set(0, 1, drhoaw_dTheta);

            if (degree == 2){
                double d2liquid_rhoaw_dxc2  = liquid_rhoawj.get(0, 0, 0);
                double d2liquid_rhoaw_dxcdT = liquid_rhoawj.get(0, 0, 1);
                double d2liquid_rhoaw_dTdxc = liquid_rhoawj.get(0, 1, 0);
                double d2liquid_rhoaw_dT2   = liquid_rhoawj.get(0, 1, 1);

                double d2rhoaw_dxc2, d2rhoaw_dxcdTheta, d2rhoaw_dThetadxc, d2rhoaw_dTheta2;

                d2rhoaw_dxc2      = d2liquid_rhoaw_dxc2/Rho_typical_;
                d2rhoaw_dxcdTheta = d2liquid_rhoaw_dxcdT*T_typical_/Rho_typical_;
                d2rhoaw_dThetadxc = d2rhoaw_dxcdTheta;
                d2rhoaw_dTheta2   = d2liquid_rhoaw_dT2*T_typical_*T_typical_/Rho_typical_;

                rhoawj.set(0, 0, 0, d2rhoaw_dxc2);
                rhoawj.set(0, 0, 1, d2rhoaw_dxcdTheta);
                rhoawj.set(0, 1, 0, d2rhoaw_dThetadxc);
                rhoawj.set(0, 1, 1, d2rhoaw_dTheta2);
            }
        }
    }
    return 2; // SUCCESSFUL_PROCEDURE;
}

// Return the value of the constants
double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Cr(void){
    return Rock_Cr;
}

double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Cw(void){
    return Cw_;
}

double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::T_typical(void){
    return T_typical_;
}

double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::U_typical(void){
    return U_typical_;
}

// Convert from Theta to T
double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::Theta2T(double Theta){
    return Theta*T_typical_ + Tref_water;
}

// Convert from T to Theta
double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::T2Theta(double T){
    return (T - Tref_water)/T_typical_;
}

// Convert from U to u
double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::U2u(double U){
    return U*U_typical_;
}

// Convert from u to U
double Thermodynamics_SuperCO2_Water_SinglePhases_Adimensionalized::u2U(double u){
    return u/U_typical_;
}
