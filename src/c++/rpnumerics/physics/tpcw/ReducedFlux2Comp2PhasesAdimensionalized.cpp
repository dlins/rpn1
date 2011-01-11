#include "ReducedFlux2Comp2PhasesAdimensionalized.h"

ReducedFlux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized(const ReducedFlux2Comp2PhasesAdimensionalized_Params &param) : FluxFunction(param){
    abs_perm       = param.component(0);
    sin_beta       = param.component(1);
    const_gravity  = param.component(2);

    TD = param.get_thermodynamics();
    FH = param.get_horizontal();

//    grav = abs_perm*sin_beta*const_gravity; // TODO: Eliminate
}

ReducedFlux2Comp2PhasesAdimensionalized * ReducedFlux2Comp2PhasesAdimensionalized::clone() const {
    cout<<"chamando clone de reduced"<<endl;
    return new ReducedFlux2Comp2PhasesAdimensionalized(*this);
}

ReducedFlux2Comp2PhasesAdimensionalized::~ReducedFlux2Comp2PhasesAdimensionalized(){
}

int ReducedFlux2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const{
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
    double   out0 = 0.0,   out1 = 0.0,   out2 = 0.0;

    double  out00 = 0.0,  out01 = 0.0 ;
    double  out10 = 0.0,  out11 = 0.0 ;
    double  out20 = 0.0,  out21 = 0.0 ;

    double out000 = 0.0, out001 = 0.0 ;
    double out010 = 0.0, out011 = 0.0 ;

    double out100 = 0.0, out101 = 0.0 ;
    double out110 = 0.0, out111 = 0.0 ;

    double out200 = 0.0, out201 = 0.0 ;
    double out210 = 0.0, out211 = 0.0 ;


    double f, df_ds, df_dTheta, d2f_ds2, d2f_dsdTheta, d2f_dThetads, d2f_dTheta2; // f=f_{sigma}, s=s_{sigma}

    JetMatrix horizontal(2);

    FH->Diff_FracFlow2PhasesHorizontalAdimensionalized(1. - s, Theta, degree, horizontal);


    // Here we recover the values of the Fractional Flow Function.

    if (degree >= 0){
        f = horizontal(0);
        if (degree >= 1){
            df_ds = horizontal(0, 0); 
            df_dTheta = horizontal(0, 1);
            if (degree >= 2){
                d2f_ds2  = horizontal(0, 0, 0);
                d2f_dsdTheta = horizontal(0, 0, 1);
                d2f_dThetads = horizontal(0, 1, 0);
                d2f_dTheta2  = horizontal(0, 1, 1); 
            }
         }
    }

    if (degree >= 0){
        out0 = (rhosic*f + rhoac*(1.0 - f));
        out1 = (rhosiw*f + rhoaw*(1.0 - f));
        out2 = ( Hsi*f + Ha*(1.0 - f) );

        if (degree >= 1){
            out00 = (rhosic - rhoac)*df_ds;
            out01 = ( d_rhosic*f + d_rhoac*(1.0 - f) + (rhosic - rhoac)*df_dTheta );

            out10 = (rhosiw - rhoaw)*df_ds;
            out11 = ( d_rhosiw*f + d_rhoaw*(1.0 - f) + (rhosiw - rhoaw)*df_dTheta );

            out20 = ( Hsi - Ha )*df_ds;
            out21 = ( d_Hsi*f  + d_Ha*(1.0 - f) + (Hsi- Ha)*df_dTheta );

            if (degree >= 2){
                out000 = (rhosic - rhoac)*d2f_ds2;
                out001 = ((d_rhosic - d_rhoac)*df_ds + (rhosic - rhoac)*d2f_dsdTheta);

                out010 = out001; // Mixed partial
                out011 = (d2_rhosic*f + d2_rhoac*(1.0 - f) + 2*(d_rhosic - d_rhoac)*df_dTheta + (rhosic - rhoac)*d2f_dTheta2);

                out100 = (rhosiw - rhoaw)*d2f_ds2;
                out101 = ((d_rhosiw - d_rhoaw)*df_ds + (rhosiw - rhoaw)*d2f_dsdTheta);

                out110 = out101; // Mixed partial
                out111 = (d2_rhosiw*f + d2_rhoaw*(1.0 - f) + 2*(d_rhosiw - d_rhoaw)*df_dTheta + (rhosiw - rhoaw)*d2f_dTheta2);

                out200 = (Hsi - Ha)*d2f_ds2 ;
                out201 = ((d_Hsi - d_Ha)*df_ds  + (Hsi - Ha)*d2f_dsdTheta);

                out210 = out201; // Mixed partial
                out211 = (d2_Hsi*f + d2_Ha*(1.0 - f) + 2*(d_Hsi- d_Ha)*df_dTheta + (Hsi- Ha)*d2f_dTheta2);

            }
        }
    }

    if (degree >= 0){
        m(0, out0);
        m(1, out1);
        m(2, out2);

        if (degree >= 1){
            m(0, 0, out00);
            m(0, 1, out01);

            m(1, 0, out10);
            m(1, 1, out11);

            m(2, 0, out20);
            m(2, 1, out21);

            if (degree >= 2){
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

