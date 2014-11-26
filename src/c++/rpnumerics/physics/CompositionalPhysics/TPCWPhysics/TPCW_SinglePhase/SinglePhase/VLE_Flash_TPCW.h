#ifndef _VLE_FLASH_TPCW_
#define _VLE_FLASH_TPCW_

#include "MolarDensity.h"
#include <math.h>

class VLE_Flash_TPCW{
    private:
        MolarDensity *mdl, *mdv;

        double q1, q2, bc, bw, R, P;
        double Tcc, Tcw, Pcc, Pcw;
        double omega_c, omega_w;

        double ROOTTWO, INV_2R2;
        double Epsilon_machine;
        double zc, zw;

        double clamp(double X, double Min, double Max){
            if( X > Max )
                X = Max;
            else if( X < Min )
                X = Min;
            return X;
        }


        int fugacity_coeficient_jet(double x, double T, double bi, int degree,
                                    JetMatrix&, JetMatrix&, JetMatrix&, JetMatrix&,
                                    JetMatrix &fugacity_coeficientj);

//        int epsilon_bar_jet(double x, double T, double bi, int degree,
//                            JetMatrix&, JetMatrix&, JetMatrix&, JetMatrix&,
//                            JetMatrix &epsilon_barj);

    protected:
    public:
        VLE_Flash_TPCW(const MolarDensity *mdl_, const MolarDensity *mdv_);
        ~VLE_Flash_TPCW();

        int fugacitycoef_lc(double x, double T, int degree, JetMatrix&);
        int fugacitycoef_vc(double x, double T, int degree, JetMatrix&);
        int fugacitycoef_lw(double x, double T, int degree, JetMatrix&);
        int fugacitycoef_vw(double x, double T, int degree, JetMatrix&);

        int molarfractions_jet(double T, JetMatrix &xcj, JetMatrix &ywj, int degree);

        static int molarfractions_xcj_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &xcj){
            VLE_Flash_TPCW *flash = (VLE_Flash_TPCW*)obj;

            double T = state(0);

            JetMatrix ywj(1);

            int info = flash->molarfractions_jet(T, xcj, ywj, degree);

            return info;
        }

        static int molarfractions_ywj_jet_test(void *obj, const RealVector &state, int degree, JetMatrix &ywj){
            VLE_Flash_TPCW *flash = (VLE_Flash_TPCW*)obj;

            double T = state(0);

            JetMatrix xcj(1);

            int info = flash->molarfractions_jet(T, xcj, ywj, degree);

            return info;
        }

        int flash(double T, double &xc, double &yw);

        int epsilon_bar_jet(double x, double T, double bi, int degree,
                            JetMatrix&, JetMatrix&, JetMatrix&, JetMatrix&,
                            JetMatrix &epsilon_barj);

};

#endif // _VLE_FLASH_TPCW_

