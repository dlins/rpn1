#include "FluxSinglePhaseVaporAdimensionalized.h"

FluxSinglePhaseVaporAdimensionalized::FluxSinglePhaseVaporAdimensionalized(const FluxSinglePhaseVaporAdimensionalized &copy) : FluxFunction(copy.fluxParams()){
//    double mc = fluxParams().component(0);
//    double mw = fluxParams().component(1);
//    double Press = fluxParams().component(2);
//    double Tr = fluxParams().component(3);
//    double Tw = fluxParams().component(4);
//    double Cr = fluxParams().component(5);
//    double cw = fluxParams().component(6);
//    double T_typical = fluxParams().component(7);
//    double Rho_typical = fluxParams().component(8);
//    double U_typical = fluxParams().component(9);
//    double h_typical = fluxParams().component(10);

    FluxSinglePhaseVaporAdimensionalized_Params *p = (FluxSinglePhaseVaporAdimensionalized_Params*)(&fluxParams());

    thermo = copy.thermo;
}

FluxSinglePhaseVaporAdimensionalized::FluxSinglePhaseVaporAdimensionalized(const FluxSinglePhaseVaporAdimensionalized_Params &param, Thermodynamics *t) : FluxFunction(param){
//    double mc = param.component(0);
//    double mw = param.component(1);
//    double Press = param.component(2);
//    double Tr = param.component(3);
//    double Tw = param.component(4);
//    double Cr = param.component(5);
//    double cw = param.component(6);
//    double T_typical = param.component(7);
//    double Rho_typical = param.component(8);
//    double U_typical = param.component(9);
//    double h_typical = param.component(10);

    FluxSinglePhaseVaporAdimensionalized_Params *p = (FluxSinglePhaseVaporAdimensionalized_Params*)(&param);

    thermo = t;
}

FluxSinglePhaseVaporAdimensionalized * FluxSinglePhaseVaporAdimensionalized::clone() const {
    return new FluxSinglePhaseVaporAdimensionalized(*this);
}

FluxSinglePhaseVaporAdimensionalized::~FluxSinglePhaseVaporAdimensionalized(){
//    delete thermo;
}

int FluxSinglePhaseVaporAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const{
    if (degree < 0 || degree > 2) return -1; // ABORTED_PROCEDURE

    double yw    = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);
    double U     = w(2);

    // Some auxiliary variables
    JetMatrix Hsij(2);
    thermo->SuperCriticEnthalpyVol_jet(yw, Theta, degree, Hsij);

    JetMatrix rhosicj(2);
    thermo->Rhosic_jet(yw, Theta, degree, rhosicj);

    JetMatrix rhosiwj(2);
    thermo->Rhosiw_jet(yw, Theta, degree, rhosiwj);

    if (degree >= 0){
        double Hsi    = Hsij.get(0);
        double rhosic = rhosicj.get(0);
        double rhosiw = rhosiwj.get(0);

        double F0 = U*rhosic;
        double F1 = U*rhosiw;
        double F2 = U*Hsi;

        m.set(0, F0);
        m.set(1, F1);
        m.set(2, F2);

        if (degree >= 1){
            double dHsi_dyw       = Hsij.get(0, 0);
            double dHsi_dTheta    = Hsij.get(0, 1);

            double drhosic_dyw    = rhosicj.get(0, 0);
            double drhosic_dTheta = rhosicj.get(0, 1);

            double drhosiw_dyw    = rhosiwj.get(0, 0);
            double drhosiw_dTheta = rhosiwj.get(0, 1);

            double dF0_dyw        = U*drhosic_dyw;
            double dF0_dTheta     = U*drhosic_dTheta;
            double dF0_dU         = rhosic;

            double dF1_dyw        = U*drhosiw_dyw;
            double dF1_dTheta     = U*drhosiw_dTheta;
            double dF1_dU         = rhosiw;

            double dF2_dyw        = U*dHsi_dyw;
            double dF2_dTheta     = U*dHsi_dTheta;
            double dF2_dU         = Hsi;

            m.set(0, 0, dF0_dyw);
            m.set(0, 1, dF0_dTheta);
            m.set(0, 2, dF0_dU);

            m.set(1, 0, dF1_dyw);
            m.set(1, 1, dF1_dTheta);
            m.set(1, 2, dF1_dU);

            m.set(2, 0, dF2_dyw);
            m.set(2, 1, dF2_dTheta);
            m.set(2, 2, dF2_dU);

            if (degree >= 2){
                double d2Hsi_dyw2         = Hsij.get(0, 0, 0);
                double d2Hsi_dywdTheta    = Hsij.get(0, 0, 1);
                double d2Hsi_dThetadyw    = Hsij.get(0, 1, 0);
                double d2Hsi_dTheta2      = Hsij.get(0, 1, 1);

                double d2rhosic_dyw2      = rhosicj.get(0, 0, 0);
                double d2rhosic_dywdTheta = rhosicj.get(0, 0, 1);
                double d2rhosic_dThetadyw = rhosicj.get(0, 1, 0);
                double d2rhosic_dTheta2   = rhosicj.get(0, 1, 1);

                double d2rhosiw_dyw2      = rhosiwj.get(0, 0, 0);
                double d2rhosiw_dywdTheta = rhosiwj.get(0, 0, 1);
                double d2rhosiw_dThetadyw = rhosiwj.get(0, 1, 0);
                double d2rhosiw_dTheta2   = rhosiwj.get(0, 1, 1);

                double d2F0dyw2      = U*d2rhosic_dyw2;
                double d2F0dywdTheta = U*d2rhosic_dywdTheta;
                double d2F0dywdU     = drhosic_dyw;

                double d2F0dThetadyw = d2F0dywdTheta;
                double d2F0dTheta2   = U*d2rhosic_dTheta2;
                double d2F0dThetadU  = drhosic_dTheta;

                double d2F0dUdyw     = d2F0dywdU;
                double d2F0dUdTheta  = d2F0dThetadU;
                double d2F0dU2       = 0.0;

                m.set(0, 0, 0, d2F0dyw2);
                m.set(0, 0, 1, d2F0dywdTheta);
                m.set(0, 0, 2, d2F0dywdU);
                m.set(0, 1, 0, d2F0dThetadyw);
                m.set(0, 1, 1, d2F0dTheta2);
                m.set(0, 1, 2, d2F0dThetadU);
                m.set(0, 2, 0, d2F0dUdyw);
                m.set(0, 2, 1, d2F0dUdTheta);
                m.set(0, 2, 2, d2F0dU2);

                double d2F1dyw2      = U*d2rhosiw_dyw2;
                double d2F1dywdTheta = U*d2rhosiw_dywdTheta;
                double d2F1dywdU     = drhosiw_dyw;

                double d2F1dThetadyw = d2F1dywdTheta;
                double d2F1dTheta2   = U*d2rhosiw_dTheta2;
                double d2F1dThetadU  = drhosiw_dTheta;

                double d2F1dUdyw     = d2F1dywdU;
                double d2F1dUdTheta  = d2F1dThetadU;
                double d2F1dU2       = 0.0;

                m.set(1, 0, 0, d2F1dyw2);
                m.set(1, 0, 1, d2F1dywdTheta);
                m.set(1, 0, 2, d2F1dywdU);
                m.set(1, 1, 0, d2F1dThetadyw);
                m.set(1, 1, 1, d2F1dTheta2);
                m.set(1, 1, 2, d2F1dThetadU);
                m.set(1, 2, 0, d2F1dUdyw);
                m.set(1, 2, 1, d2F1dUdTheta);
                m.set(1, 2, 2, d2F1dU2);

                double d2F2dyw2      = U*d2Hsi_dyw2;
                double d2F2dywdTheta = U*d2Hsi_dywdTheta;
                double d2F2dywdU     = dHsi_dyw;

                double d2F2dThetadyw = d2F2dywdTheta;
                double d2F2dTheta2   = U*d2Hsi_dTheta2;
                double d2F2dThetadU  = dHsi_dTheta;

                double d2F2dUdyw     = d2F2dywdU;
                double d2F2dUdTheta  = d2F2dThetadU;
                double d2F2dU2       = 0.0;

                m.set(2, 0, 0, d2F2dyw2);
                m.set(2, 0, 1, d2F2dywdTheta);
                m.set(2, 0, 2, d2F2dywdU);
                m.set(2, 1, 0, d2F2dThetadyw);
                m.set(2, 1, 1, d2F2dTheta2);
                m.set(2, 1, 2, d2F2dThetadU);
                m.set(2, 2, 0, d2F2dUdyw);
                m.set(2, 2, 1, d2F2dUdTheta);
                m.set(2, 2, 2, d2F2dU2);
            }
        }
    }

    return  2; //SUCCESSFUL_PROCEDURE;
}

