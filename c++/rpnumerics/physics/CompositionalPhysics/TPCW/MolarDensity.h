#ifndef _MOLARDENSITY_
#define _MOLARDENSITY_

#include <complex>
#include <vector>
#include <algorithm>
#include <math.h>

#include "JetMatrix.h"
#include "Parameter.h"

#define MOLAR_DENSITY_VAPOR  1
#define MOLAR_DENSITY_LIQUID 2

// Molar Density calculated by the PRSV equation of state method with the MHV2 mixing rule.
//
class MolarDensity {
    private:
    public:
        int type;   // MOLAR_DENSITY_VAPOR or MOLAR_DENSITY_LIQUID

        double P;

        double R;   // Ideal gas constant [J/(K*mol)]

        double Tcc; // Critical temperature of CO2 [K]
        double Pcc; // Critical pressure of CO2 [Pa]

        double Tcw; // Critical temperature of water [K]
        double Pcw; // Critical pressure of water [Pa]

        double ac;  // Intermolecular attraction for CO2
        double aw;  // Intermolecular attraction for water

        double bc;  // Intermolecular repulsion for CO2
        double bw;  // Intermolecular repulsion for water

        double q1;
        double q2;
        double omega_w;
        double kappa_1w;
        double omega_c;
        double kappa_1c;

        double Delta_G0_21;
        double Delta_G1_21;
        double Delta_G0_12;
        double Delta_G1_12;

	// These constants are Delta_G0_12/R and Delta_G0_21/R
	double Aux_G12;
        double Aux_G21;

        double zero_epsilon;

        // Constants for building the volume shift parameter:
        //
        //     C_c = C0_c + C1_c*T, for CO2,
        //     C_w = C0_w + C1_w*T, for water.
        //
        double C0_c; // [m^3/mol]
        double C1_c; // [m^3/(mol*K)]
        double C0_w; // [m^3/mol]
        double C1_w; // [m^3/(mol*K)]

        double log_bc, log_bw, alpha12;

        double cbrt(double x);

        int CubicRoots(const double a, const double b, const double c, const double d, 
                       std::complex<double> &r0, 
                       std::complex<double> &r1, 
                       std::complex<double> &r2);

        // Jets
        int vapor_Epsilon_jet(const double x, const double T, int degree, JetMatrix &bj);
        int liquid_Epsilon_jet(const double x, const double T, int degree, JetMatrix &bj);
      //  int Epsilon_jet(const double x, const double T, int degree, JetMatrix &epsj);

        int vapor_b_jet(const double x, int degree, JetMatrix &bj);
        int liquid_b_jet(const double x, int degree, JetMatrix &bj);
     //   int b_jet(const double x, int degree, JetMatrix &bj);

        //int A_jet(const double x, const double T, int degree, JetMatrix &Aj);

       // int B_jet(const double x, const double T, int degree, JetMatrix &Bj);

        int F_jet(const double x, const double T, const double Z, 
                        JetMatrix &Aj, JetMatrix &Bj, 
                        int degree, 
                        JetMatrix &fj);

       // int Z_jet(const double x, const double T, int degree, JetMatrix &zj); // Compressibility Factor

        int vapor_rho_jet(const double x, const double T, int degree, JetMatrix &r);
        int liquid_C_jet(const double x, const double T, int degree, JetMatrix &cj);
        int liquid_rho_jet(const double x, const double T, int degree, JetMatrix &r);

        // New methods
        //int epsilon_c_jet(double, int, JetMatrix&);
        //int epsilon_w_jet(double, int, JetMatrix&);
//        int tau12_jet(double, int, JetMatrix&);
//        int tau21_jet(double, int, JetMatrix&);
        //int G12_jet(double, int, JetMatrix&);
        //int G21_jet(double, int, JetMatrix&);
        //int vapor_Gamma1_jet(double, double, int, JetMatrix&);
        //int liquid_Gamma1_jet(double, double, int, JetMatrix&);
        //int vapor_Gamma2_jet(double, double, int, JetMatrix&);
        //int liquid_Gamma2_jet(double, double, int, JetMatrix&);
        //int GE_jet(double, double, int, JetMatrix&);
        int vapor_L_jet(double, double, int, JetMatrix&);
        int liquid_L_jet(double, double, int, JetMatrix&);
        //int L_jet(double, double, int, JetMatrix&);
        int Q_jet(double, double, double, JetMatrix&, int, JetMatrix&);
    protected:
        Parameter *P_parameter;

        void init();
    public:
//        MolarDensity(int t, double Pressure); // TODO: Deprecate ASAP!
        MolarDensity(int t, Parameter *Pressure);

        ~MolarDensity();

        int rho_jet(const double x, const double T, int degree, JetMatrix &r);
        int A_jet(const double x, const double T, int degree, JetMatrix &Aj);

        int b_jet(const double x, int degree, JetMatrix &bj);
        int B_jet(const double x, const double T, int degree, JetMatrix &Bj);
        int Z_jet(const double x, const double T, int degree, JetMatrix &zj); // Compressibility Factor
 
        int Epsilon_jet(const double x, const double T, int degree, JetMatrix&);

        int epsilon_c_jet(const double T, int degree, JetMatrix&);

        static int epsilon_c_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->epsilon_c_jet(T, degree, jm);
        }

        int epsilon_w_jet(const double T, int degree, JetMatrix&);

        static int epsilon_w_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->epsilon_w_jet(T, degree, jm);
        }

        int Gamma1_jet(double, double, int, JetMatrix&);

        static int Gamma1_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *molar = (MolarDensity*)obj;

            double x = state(0);
            double T = state(1);

            jm.resize(2, 1);

            return molar->Gamma1_jet(x, T, degree, jm);
        }

        int Gamma2_jet(double, double, int, JetMatrix&);

        static int Gamma2_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *molar = (MolarDensity*)obj;

            double x = state(0);
            double T = state(1);

            jm.resize(2, 1);

            return molar->Gamma2_jet(x, T, degree, jm);
        }

        int gamma1_jet(double, double, int, JetMatrix&);

        static int gamma1_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *molar = (MolarDensity*)obj;

            double x = state(0);
            double T = state(1);

            jm.resize(2, 1);

            return molar->gamma1_jet(x, T, degree, jm);
        }

        int gamma2_jet(double, double, int, JetMatrix&);

        static int gamma2_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *molar = (MolarDensity*)obj;

            double x = state(0);
            double T = state(1);

            jm.resize(2, 1);

            return molar->gamma2_jet(x, T, degree, jm);
        }

        int GE_jet(const double x, const double T, int degree, JetMatrix&);
        int L_jet(const double x, const double T, int degree, JetMatrix&);

        int G12_jet(double, int, JetMatrix&);

        static int G12_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->G12_jet(T, degree, jm);
        }

        int G21_jet(double, int, JetMatrix&);

        static int G21_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->G21_jet(T, degree, jm);
        }

        int tau12_jet(double, int, JetMatrix&);

        static int tau12_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->tau12_jet(T, degree, jm);
        }

        int tau21_jet(double, int, JetMatrix&);

        static int tau21_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &jm){
            MolarDensity *m = (MolarDensity*)obj;

            double T = state(0);
            jm.resize(1);

            return m->tau21_jet(T, degree, jm);
        }

        double get_R(void){return R;}
        double get_P(void){return P;}
        double get_q1(void){return q1;}
        double get_q2(void){return q2;}
        double get_bc(void){return bc;}
        double get_bw(void){return bw;}

        double get_Pcc(void){return Pcc;}
        double get_Pcw(void){return Pcw;}
        double get_Tcc(void){return Tcc;}
        double get_Tcw(void){return Tcw;}

        double get_omega_c(void){return omega_c;}
        double get_omega_w(void){return omega_w;}

        // Test.
        int null_jet(const double x, const double T, int degree, JetMatrix &n){
            n.resize(2, 1);

            n.set(0, 0.0);

            n.set(0, 0, 0.0);
            n.set(0, 1, 0.0);

            n.set(0, 0, 0, 0.0);
            n.set(0, 0, 1, 0.0);
            n.set(0, 1, 0, 0.0);
            n.set(0, 1, 1, 0.0);

            return degree;
        }
};

#endif // _MOLARDENSITY_

