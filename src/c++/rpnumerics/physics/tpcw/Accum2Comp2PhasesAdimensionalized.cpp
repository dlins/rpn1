#include "Accum2Comp2PhasesAdimensionalized.h"

Accum2Comp2PhasesAdimensionalized::Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized &a) {
    TD =  a.TD;

    phi = a.phi;
}

Accum2Comp2PhasesAdimensionalized::Accum2Comp2PhasesAdimensionalized(const Accum2Comp2PhasesAdimensionalized_Params &param) {
    TD = param.get_thermodynamics();
    phi = param.component(0);
    reducedAccum_= new ReducedAccum2Comp2PhasesAdimensionalized(this);
}

 RpFunction * Accum2Comp2PhasesAdimensionalized::clone() const {

    return new Accum2Comp2PhasesAdimensionalized(*this);
}

Accum2Comp2PhasesAdimensionalized::~Accum2Comp2PhasesAdimensionalized() {
//    delete reducedAccum_;
}

// Existe uma discrepancia entre o o significado de s quando este codigo foi
// escrito usando as formulas do Rodrigo, em que s eh a saturacao da fase
// de CO2 supersaturado, e como este codigo vai ser usado no RPN, onde s eh
// a saturacao da agua.
// Isto implica em pequenas mudancas, analogas as que Helmut e eu sugerimos
// para o FracFlow, num email de ontem.
//
// Dan

int Accum2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const {
    double s = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);

    double U = w(2);

    // Some auxiliary variables
    double Hr, d_Hr, d2_Hr;
    TD->Diff_RockEnthalpyVol(Theta, Hr, d_Hr, d2_Hr);
    double Ha, d_Ha, d2_Ha;
    TD->Diff_AqueousEnthalpyVol(Theta, Ha, d_Ha, d2_Ha);
    double Hsi, d_Hsi, d2_Hsi;
    TD->Diff_SuperCriticEnthalpyVol(Theta, Hsi, d_Hsi, d2_Hsi);


    double rhosic, d_rhosic, d2_rhosic;
    TD->Diff_Rhosic(Theta, rhosic, d_rhosic, d2_rhosic);

    double rhosiw, d_rhosiw, d2_rhosiw;
    TD->Diff_Rhosiw(Theta, rhosiw, d_rhosiw, d2_rhosiw);

    double rhoac, d_rhoac, d2_rhoac;
    TD->Diff_Rhoac(Theta, rhoac, d_rhoac, d2_rhoac);

    double rhoaw, d_rhoaw, d2_rhoaw;
    TD->Diff_Rhoaw(Theta, rhoaw, d_rhoaw, d2_rhoaw);



    // Output
    double out0, out1, out2;

    double out00, out01, out02;
    double out10, out11, out12;
    double out20, out21, out22;

    double out000, out001, out002;
    double out010, out011, out012;
    double out020, out021, out022;

    double out100, out101, out102;
    double out110, out111, out112;
    double out120, out121, out122;

    double out200, out201, out202;
    double out210, out211, out212;
    double out220, out221, out222;

    // Function
    if (degree >= 0) {
        out0 = phi * (rhosic * s + rhoac * (1.0 - s)); // G1
        out1 = phi * (rhosiw * s + rhoaw * (1.0 - s)); // G2
        out2 = Hr + phi * (Hsi * s + Ha * (1.0 - s)); // G3

        m(0, out0);
        m(1, out1);
        m(2, out2);

        // Jacobian
        if (degree >= 1) {
            out00 = phi * (rhosic - rhoac); // dG1_ds
            out01 = phi * (d_rhosic * s + d_rhoac * (1.0 - s)); // dG1_dTheta
            out02 = 0.; // dG1_dU

            //            printf("out00 = %g\n", out00);
            //            printf("out01 = %g\n", out01);
            //            printf("out02 = %g\n", out02);

            out10 = phi * (rhosiw - rhoaw); // dG2_ds
            out11 = phi * (d_rhosiw * s + d_rhoaw * (1.0 - s)); // dG2_dTheta
            out12 = 0.; // dG1_dU

            out20 = phi * (Hsi - Ha); // dG3_ds
            out21 = d_Hr + phi * (d_Hsi * s + d_Ha * (1.0 - s)); // dG3_dTheta
            out22 = 0.; // dG3_dU

            m(0, 0, out00);
            m(0, 1, out01);
            m(0, 2, out02);

            m(1, 0, out10);
            m(1, 1, out11);
            m(1, 2, out12);

            m(2, 0, out20);
            m(2, 1, out21);
            m(2, 2, out22);

            //            for (int i = 0; i < 3; i++){
            //                for (int j = 0; j < 3; j++){
            //                    printf("aa(%d, %d) = %g\n", i, j, m(i, j));
            //                }
            //            }

            // Hessian
            if (degree == 2) {
                out000 = 0.; // d2G1_ds2
                out001 = phi * (d_rhosic - d_rhoac); // d2G1_dsdTheta
                out002 = 0.; // d2G1_dsdU
                out010 = out001; // d2G1_dThetads
                out011 = phi * (d2_rhosic * s + d2_rhoac * (1.0 - s)); // d2G1_dTheta2
                out012 = 0.; // d2G1_dThetadU
                out020 = 0.; // d2G1_dUds
                out021 = 0.; // d2G1_dUdTheta
                out022 = 0.; // d2G1_dU2

                out100 = 0.; // d2G2_ds2
                out101 = phi * (d_rhosiw - d_rhoaw); // d2G2_dsdTheta
                out102 = 0.; // d2G2_dsdU
                out110 = phi * (d_rhosiw - d_rhoaw); // d2G2_dThetads
                out111 = phi * (d2_rhosiw * s + d2_rhoaw * (1.0 - s)); // d2G2_dTheta2
                out112 = 0.; // d2G2_dThetadU
                out120 = 0.; // d2G2_dUds
                out121 = 0.; // d2G2_dUdTheta
                out122 = 0.; // d2G2_dU2

                out200 = 0.; // d2G3_ds2
                out201 = phi * (d_Hsi - d_Ha); // d2G3_dsdTheta
                out202 = 0.; // d2G3_dsdU
                out210 = out201; // d2G3_dThetads
                out211 = d2_Hr + phi * (d2_Hsi * s + d2_Ha * (1.0 - s)); // d2G3_dTheta2
                out212 = 0.; // d2G3_dThetadU
                out220 = 0.; // d2G3_dUds
                out221 = 0.; // d2G3_dUdTheta
                out222 = 0.; // d2G3_dU2

                m(0, 0, 0, out000);
                m(0, 0, 1, out001);
                m(0, 0, 2, out002);
                m(0, 1, 0, out010);
                m(0, 1, 1, out011);
                m(0, 1, 2, out012);
                m(0, 2, 0, out020);
                m(0, 2, 1, out021);
                m(0, 2, 2, out022);


                m(1, 0, 0, out100);
                m(1, 0, 1, out101);
                m(1, 0, 2, out102);
                m(1, 1, 0, out110);
                m(1, 1, 1, out111);
                m(1, 1, 2, out112);
                m(1, 2, 0, out120);
                m(1, 2, 1, out121);
                m(1, 2, 2, out122);


                m(2, 0, 0, out200);
                m(2, 0, 1, out201);
                m(2, 0, 2, out202);
                m(2, 1, 0, out210);
                m(2, 1, 1, out211);
                m(2, 1, 2, out212);
                m(2, 2, 0, out220);
                m(2, 2, 1, out221);
                m(2, 2, 2, out222);
            }
        }
    }

    return 2; //SUCCESSFUL_PROCEDURE;
}


Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized(Accum2Comp2PhasesAdimensionalized * outer):AccumulationFunction(outer->accumulationParams()){
    totalAccum_=outer;

 }

Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized::~ReducedAccum2Comp2PhasesAdimensionalized(){

}

 Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized * Accum2Comp2PhasesAdimensionalized::getReducedAccumulation()const{
     return reducedAccum_;

 }

RpFunction * Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized::clone() const{

}

int Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const{
    double s     = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);

    // Some auxiliary variables
    double Hr, d_Hr, d2_Hr;    totalAccum_->TD->Diff_RockEnthalpyVol(Theta, Hr, d_Hr, d2_Hr);
    double Ha, d_Ha, d2_Ha;    totalAccum_->TD->Diff_AqueousEnthalpyVol(Theta, Ha, d_Ha, d2_Ha);
    double Hsi, d_Hsi, d2_Hsi; totalAccum_->TD->Diff_SuperCriticEnthalpyVol(Theta, Hsi, d_Hsi, d2_Hsi);


    double rhosic, d_rhosic, d2_rhosic;
    totalAccum_->TD->Diff_Rhosic(Theta, rhosic, d_rhosic, d2_rhosic);

    double rhosiw, d_rhosiw, d2_rhosiw;
    totalAccum_->TD->Diff_Rhosiw(Theta, rhosiw, d_rhosiw, d2_rhosiw);

    double rhoac, d_rhoac, d2_rhoac;
    totalAccum_->TD->Diff_Rhoac(Theta, rhoac, d_rhoac, d2_rhoac);

    double rhoaw, d_rhoaw, d2_rhoaw;
    totalAccum_->TD->Diff_Rhoaw(Theta, rhoaw, d_rhoaw, d2_rhoaw);


    // Output
    double out0, out1, out2;

    double out00, out01 ;
    double out10, out11 ;
    double out20, out21 ;

    double out000, out001 ;
    double out010, out011 ;

    double out100, out101 ;
    double out110, out111 ;

    double out200, out201 ;
    double out210, out211 ;


    // Function
    if (degree >= 0){
        out0 = totalAccum_->phi*(rhosic*s + rhoac*(1.0 - s)); // G1
        out1 = totalAccum_->phi*(rhosiw*s + rhoaw*(1.0 - s)); // G2
        out2 = Hr + totalAccum_->phi*(Hsi*s + Ha*(1.0 - s) ); // G3

        m(0, out0);
        m(1, out1);
        m(2, out2);

        // Jacobian
        if (degree >= 1){
            out00 = totalAccum_->phi*(rhosic - rhoac); // dG1_ds
            out01 = totalAccum_->phi*(d_rhosic*s + d_rhoac*(1.0 - s)); // dG1_dTheta

//            printf("out00 = %g\n", out00);
//            printf("out01 = %g\n", out01);

            out10 = totalAccum_->phi*(rhosiw - rhoaw); // dG2_ds
            out11 = totalAccum_->phi*(d_rhosiw*s + d_rhoaw*(1.0 - s)); // dG2_dTheta

            out20 = totalAccum_->phi*(Hsi-Ha); // dG3_ds
            out21 = d_Hr + totalAccum_->phi*( d_Hsi*s + d_Ha*(1.0 - s) ); // dG3_dTheta

            m(0, 0, out00);
            m(0, 1, out01);

            m(1, 0, out10);
            m(1, 1, out11);

            m(2, 0, out20);
            m(2, 1, out21);

//            for (int i = 0; i < 3; i++){
//                for (int j = 0; j < 3; j++){
//                    printf("aa(%d, %d) = %g\n", i, j, m(i, j));
//                }
//            }


            // Hessian
            if (degree == 2){
                out000 = 0.; // d2G1_ds2
                out001 = totalAccum_->phi*(d_rhosic - d_rhoac); // d2G1_dsdTheta
                out010 = out001; // d2G1_dThetads
                out011 = totalAccum_->phi*(d2_rhosic*s + d2_rhoac*(1.0 - s)); // d2G1_dTheta2

                out100 = 0.; // d2G2_ds2
                out101 = totalAccum_->phi*(d_rhosiw - d_rhoaw); // d2G2_dsdTheta
                out110 = totalAccum_->phi*(d_rhosiw - d_rhoaw); // d2G2_dThetads
                out111 = totalAccum_->phi*(d2_rhosiw*s + d2_rhoaw*(1.0 - s)); // d2G2_dTheta2

                out200 = 0.; // d2G3_ds2
                out201 = totalAccum_->phi*(d_Hsi-d_Ha); // d2G3_dsdTheta
                out210 = out201; // d2G3_dThetads
                out211 = d2_Hr + totalAccum_->phi*(d2_Hsi*s + d2_Ha*(1.0 - s) ); // d2G3_dTheta2

                m(0, 0, 0, out000);
                m(0, 0, 1, out001);
                m(0, 1, 0, out010);
                m(0, 1, 1, out011);

                m(1, 0, 0, out100);
                m(1, 0, 1, out101);
                m(1, 1, 0, out110);
                m(1, 1, 1, out111);

                m(2, 0, 0, out200);
                m(2, 0, 1, out201);
                m(2, 1, 0, out210);
                m(2, 1, 1, out211);
            }
        }
    }

    return  2; //SUCCESSFUL_PROCEDURE;
}