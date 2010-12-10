#include "ReducedAccum2Comp2PhasesAdimensionalized.h"

ReducedAccum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized(const ReducedAccum2Comp2PhasesAdimensionalized &a){
    TD = a.TD;
    phi = a.phi;
}

ReducedAccum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized(const ReducedAccum2Comp2PhasesAdimensionalized_Params &param){
    TD = param.get_thermodynamics();
    phi = param.component(0);
}

ReducedAccum2Comp2PhasesAdimensionalized * ReducedAccum2Comp2PhasesAdimensionalized::clone() const {
    return new ReducedAccum2Comp2PhasesAdimensionalized(*this);
}

ReducedAccum2Comp2PhasesAdimensionalized::~ReducedAccum2Comp2PhasesAdimensionalized(){
}

// Existe uma discrepancia entre o o significado de s quando este codigo foi
// escrito usando as formulas do Rodrigo, em que s eh a saturacao da fase
// de CO2 supersaturado, e como este codigo vai ser usado no RPN, onde s eh
// a saturacao da agua.
// Isto implica em pequenas mudancas, analogas as que Helmut e eu sugerimos
// para o FracFlow, num email de ontem.
//
// Dan

int ReducedAccum2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const{
    double s     = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);

    // Some auxiliary variables
    double Hr, d_Hr, d2_Hr;    TD->Diff_RockEnthalpyVol(Theta, Hr, d_Hr, d2_Hr);
    double Ha, d_Ha, d2_Ha;    TD->Diff_AqueousEnthalpyVol(Theta, Ha, d_Ha, d2_Ha);
    double Hsi, d_Hsi, d2_Hsi; TD->Diff_SuperCriticEnthalpyVol(Theta, Hsi, d_Hsi, d2_Hsi);


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
        out0 = phi*(rhosic*s + rhoac*(1.0 - s)); // G1
        out1 = phi*(rhosiw*s + rhoaw*(1.0 - s)); // G2 
        out2 = Hr + phi*(Hsi*s + Ha*(1.0 - s) ); // G3

        m(0, out0);
        m(1, out1);
        m(2, out2);

        // Jacobian
        if (degree >= 1){
            out00 = phi*(rhosic - rhoac); // dG1_ds
            out01 = phi*(d_rhosic*s + d_rhoac*(1.0 - s)); // dG1_dTheta

//            printf("out00 = %g\n", out00);
//            printf("out01 = %g\n", out01);

            out10 = phi*(rhosiw - rhoaw); // dG2_ds
            out11 = phi*(d_rhosiw*s + d_rhoaw*(1.0 - s)); // dG2_dTheta

            out20 = phi*(Hsi-Ha); // dG3_ds 
            out21 = d_Hr + phi*( d_Hsi*s + d_Ha*(1.0 - s) ); // dG3_dTheta

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
                out001 = phi*(d_rhosic - d_rhoac); // d2G1_dsdTheta
                out010 = out001; // d2G1_dThetads
                out011 = phi*(d2_rhosic*s + d2_rhoac*(1.0 - s)); // d2G1_dTheta2

                out100 = 0.; // d2G2_ds2
                out101 = phi*(d_rhosiw - d_rhoaw); // d2G2_dsdTheta
                out110 = phi*(d_rhosiw - d_rhoaw); // d2G2_dThetads
                out111 = phi*(d2_rhosiw*s + d2_rhoaw*(1.0 - s)); // d2G2_dTheta2

                out200 = 0.; // d2G3_ds2
                out201 = phi*(d_Hsi-d_Ha); // d2G3_dsdTheta 
                out210 = out201; // d2G3_dThetads
                out211 = d2_Hr + phi*(d2_Hsi*s + d2_Ha*(1.0 - s) ); // d2G3_dTheta2

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
