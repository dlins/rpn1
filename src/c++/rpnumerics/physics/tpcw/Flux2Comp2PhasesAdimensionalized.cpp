#include "Flux2Comp2PhasesAdimensionalized.h"

Flux2Comp2PhasesAdimensionalized::Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &a) : FluxFunction(a.fluxParams()) {

//    const Flux2Comp2PhasesAdimensionalized_Params & fluxParams = (const Flux2Comp2PhasesAdimensionalized_Params &) a.fluxParams();

    TD = a.TD;
    FH = a.FH;
    FV = a.FV;

    abs_perm = a.abs_perm;
    sin_beta = a.sin_beta;
    const_gravity = a.const_gravity;
    grav = a.grav;
    has_gravity = a.has_gravity;
    has_horizontal = a.has_horizontal;
}

Flux2Comp2PhasesAdimensionalized::Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &param) : FluxFunction(param) {

    cout << "Clonando parametros de fluxo" << endl;
    abs_perm = param.component(0);
    sin_beta = param.component(1);
    const_gravity = param.component(2);
    
    const Flux2Comp2PhasesAdimensionalized_Params & fluxParams = (const Flux2Comp2PhasesAdimensionalized_Params &) param;


    TD = fluxParams.get_thermodynamics();
    FH = param.get_horizontal();
    FV = param.get_vertical();

    has_gravity = param.has_gravity();
    has_horizontal = param.has_horizontal();

    grav = abs_perm * sin_beta*const_gravity;
}

Flux2Comp2PhasesAdimensionalized * Flux2Comp2PhasesAdimensionalized::clone() const {

    return new Flux2Comp2PhasesAdimensionalized(*this);
}

Flux2Comp2PhasesAdimensionalized::~Flux2Comp2PhasesAdimensionalized() {

//    delete TD;

}

int Flux2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const {
    double s = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);
    double U = w(2);

    //    cout <<"s: "<<s<<" Theta:"<<Theta<<" U:"<<U<<endl;

    // Recovering the U_typical_
    //    double U_typical_=TD->U_typical();


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
    double out0 = 0.0, out1 = 0.0, out2 = 0.0;

    double out00 = 0.0, out01 = 0.0, out02 = 0.0;
    double out10 = 0.0, out11 = 0.0, out12 = 0.0;
    double out20 = 0.0, out21 = 0.0, out22 = 0.0;

    double out000 = 0.0, out001 = 0.0, out002 = 0.0;
    double out010 = 0.0, out011 = 0.0, out012 = 0.0;
    double out020 = 0.0, out021 = 0.0, out022 = 0.0;

    double out100 = 0.0, out101 = 0.0, out102 = 0.0;
    double out110 = 0.0, out111 = 0.0, out112 = 0.0;
    double out120 = 0.0, out121 = 0.0, out122 = 0.0;

    double out200 = 0.0, out201 = 0.0, out202 = 0.0;
    double out210 = 0.0, out211 = 0.0, out212 = 0.0;
    double out220 = 0.0, out221 = 0.0, out222 = 0.0;

    // Begin of pure horizontal
    //    cout <<"aqui"<<endl;
    if (has_horizontal) {

        double f, df_ds, df_dTheta, d2f_ds2, d2f_dsdTheta, d2f_dThetads, d2f_dTheta2; // f=f_{sigma}, s=s_{sigma}
        JetMatrix horizontal(2);

        FH->Diff_FracFlow2PhasesHorizontalAdimensionalized(1. - s, Theta, degree, horizontal);

        if (degree >= 0) {
            f = horizontal(0);
            if (degree >= 1) {
                df_ds = horizontal(0, 0);
                df_dTheta = horizontal(0, 1);
                if (degree >= 2) {
                    d2f_ds2 = horizontal(0, 0, 0);
                    d2f_dsdTheta = horizontal(0, 0, 1);
                    d2f_dThetads = horizontal(0, 1, 0);
                    d2f_dTheta2 = horizontal(0, 1, 1);
                }
            }
        }

        if (degree >= 0) {
            out0 = U * (rhosic * f + rhoac * (1.0 - f));
            out1 = U * (rhosiw * f + rhoaw * (1.0 - f));
            out2 = U * (Hsi * f + Ha * (1.0 - f));

            if (degree >= 1) {
                out00 = U * (rhosic - rhoac) * df_ds;
                out01 = U * (d_rhosic * f + d_rhoac * (1.0 - f) + (rhosic - rhoac) * df_dTheta);
                out02 = (rhosic * f + rhoac * (1.0 - f)); // dF1_dU

                out10 = U * (rhosiw - rhoaw) * df_ds;
                out11 = U * (d_rhosiw * f + d_rhoaw * (1.0 - f) + (rhosiw - rhoaw) * df_dTheta);
                out12 = (rhosiw * f + rhoaw * (1.0 - f)); // dF2_dU

                out20 = U * (Hsi - Ha) * df_ds;
                out21 = U * (d_Hsi * f + d_Ha * (1.0 - f) + (Hsi - Ha) * df_dTheta);
                out22 = (Hsi * f + Ha * (1.0 - f)); // dF3_dU

                if (degree >= 2) {
                    out000 = U * (rhosic - rhoac) * d2f_ds2;
                    out001 = U * ((d_rhosic - d_rhoac) * df_ds + (rhosic - rhoac) * d2f_dsdTheta);
                    out002 = ((rhosic - rhoac) * df_ds); // d2F1_dsdU

                    out010 = out001; // Mixed partial
                    out011 = U * (d2_rhosic * f + d2_rhoac * (1.0 - f) + 2 * (d_rhosic - d_rhoac) * df_dTheta + (rhosic - rhoac) * d2f_dTheta2);
                    out012 = (d_rhosic * f + d_rhoac * (1.0 - f) + (rhosic - rhoac) * df_dTheta); // d2F1_dThetadU

                    out100 = U * (rhosiw - rhoaw) * d2f_ds2;
                    out101 = U * ((d_rhosiw - d_rhoaw) * df_ds + (rhosiw - rhoaw) * d2f_dsdTheta);
                    out102 = (rhosiw - rhoaw) * df_ds; // d2F2_dsdU

                    out110 = out101; // Mixed partial
                    out111 = U * (d2_rhosiw * f + d2_rhoaw * (1.0 - f) + 2 * (d_rhosiw - d_rhoaw) * df_dTheta + (rhosiw - rhoaw) * d2f_dTheta2);
                    out112 = (d_rhosiw * f + d_rhoaw * (1.0 - f) + (rhosiw - rhoaw) * df_dTheta); // d2F2_dThetadU

                    out120 = out102;
                    out121 = out112;
                    out122 = 0.; // d2F2_dU2

                    out200 = U * (Hsi - Ha) * d2f_ds2;
                    out201 = U * ((d_Hsi - d_Ha) * df_ds + (Hsi - Ha) * d2f_dsdTheta);
                    out202 = (Hsi - Ha) * df_ds; // d2F3_dsdU

                    out210 = out201; // Mixed partial
                    out211 = U * (d2_Hsi * f + d2_Ha * (1.0 - f) + 2 * (d_Hsi - d_Ha) * df_dTheta + (Hsi - Ha) * d2f_dTheta2);
                    out212 = d_Hsi * f + d_Ha * (1.0 - f) + (Hsi - Ha) * df_dTheta; // d2F3_dThetadU

                    out220 = (Hsi - Ha) * df_ds;
                    out221 = d_Hsi * f + d_Ha * (1.0 - f) + (Hsi - Ha) * df_dTheta;
                    out222 = 0.; // d2F3_dU2
                }
            }
        }
    } // End of pure horizontal

    // Begin of pure gravity
    if (has_gravity) {
        double Z, dZ_ds, dZ_dTheta, d2Z_ds2, d2Z_dsdTheta, d2Z_dThetads, d2Z_dTheta2;
        JetMatrix vertical(2);

        FV->Diff_FracFlow2PhasesVerticalAdimensionalized(1. - s, Theta, degree, vertical);

        if (degree >= 0) {
            Z = vertical(0);
            if (degree >= 1) {
                dZ_ds = vertical(0, 0);
                dZ_dTheta = vertical(0, 1);
                if (degree >= 2) {
                    d2Z_ds2 = vertical(0, 0, 0);
                    d2Z_dsdTheta = vertical(0, 0, 1);
                    d2Z_dThetads = vertical(0, 1, 0);
                    d2Z_dTheta2 = vertical(0, 1, 1);
                }
            }
        }

        double grhoa_rhosi = grav * ((rhoac + rhoaw) - (rhosic + rhosiw));
        double d_grhoa_rhosi = grav * ((d_rhoac + d_rhoaw) - (d_rhosic + d_rhosiw));
        double d2_grhoa_rhosi = grav * ((d2_rhoac + d2_rhoaw) - (d2_rhosic + d2_rhosiw));

        double rhosic_rhoac = rhosic - rhoac;
        double d_rhosic_rhoac = d_rhosic - d_rhoac;
        double d2_rhosic_rhoac = d2_rhosic - d2_rhoac;

        double rhosiw_rhoaw = rhosiw - rhoaw;
        double d_rhosiw_rhoaw = d_rhosiw - d_rhoaw;
        double d2_rhosiw_rhoaw = d2_rhosiw - d2_rhoaw;

        double Hsi_Ha = Hsi - Ha;
        double d_Hsi_Ha = d_Hsi - d_Ha;
        double d2_Hsi_Ha = d2_Hsi - d2_Ha;

        if (degree >= 0) {
            out0 += grhoa_rhosi * rhosic_rhoac*Z; // F1
            out1 += grhoa_rhosi * rhosiw_rhoaw*Z; // F2
            out2 += grhoa_rhosi * Hsi_Ha*Z; // F3

            if (degree >= 1) {
                out00 += grhoa_rhosi * rhosic_rhoac*dZ_ds; // dF1_ds
                out01 += d_grhoa_rhosi * rhosic_rhoac * Z + grhoa_rhosi * d_rhosic_rhoac * Z + grhoa_rhosi * rhosic_rhoac*dZ_dTheta; // dF1_dTheta
                //  out02 += 0; // dF1_du

                out10 += grhoa_rhosi * rhosiw_rhoaw*dZ_ds; // dF2_ds
                out11 += d_grhoa_rhosi * rhosiw_rhoaw * Z + grhoa_rhosi * d_rhosiw_rhoaw * Z + grhoa_rhosi * rhosiw_rhoaw*dZ_dTheta; // dF2_dTheta
                //  out12 += 0; // dF2_du

                out20 += grhoa_rhosi * Hsi_Ha*dZ_ds; // dF3_ds
                out21 += d_grhoa_rhosi * Hsi_Ha * Z + grhoa_rhosi * d_Hsi_Ha * Z + grhoa_rhosi * Hsi_Ha*dZ_dTheta; // dF3_dTheta
                //  out22 += 0; // dF3_du

                if (degree >= 2) {
                    out000 += grhoa_rhosi * rhosic_rhoac*d2Z_ds2; // d2F1_ds2
                    out001 += d_grhoa_rhosi * rhosic_rhoac * dZ_ds +
                            grhoa_rhosi * d_rhosic_rhoac * dZ_ds +
                            grhoa_rhosi * rhosic_rhoac*d2Z_dsdTheta; // d2F1_dsdTheta
                    // out002 += 0; // d2F1_dsdu

                    out010 += d_grhoa_rhosi * rhosic_rhoac * dZ_ds +
                            grhoa_rhosi * d_rhosic_rhoac * dZ_ds +
                            grhoa_rhosi * rhosic_rhoac*d2Z_dsdTheta; // d2F1_dThetads
                    out011 += d2_grhoa_rhosi * rhosic_rhoac * Z +
                            d_grhoa_rhosi * d_rhosic_rhoac * Z +
                            d_grhoa_rhosi * rhosic_rhoac * dZ_dTheta +
                            d_grhoa_rhosi * d_rhosic_rhoac * Z +
                            grhoa_rhosi * d2_rhosic_rhoac * Z +
                            grhoa_rhosi * d_rhosic_rhoac * dZ_dTheta +
                            d_grhoa_rhosi * rhosic_rhoac * dZ_dTheta +
                            grhoa_rhosi * d_rhosic_rhoac * dZ_dTheta +
                            grhoa_rhosi * rhosic_rhoac*d2Z_dTheta2; // d2F1_dTheta2
                    //  out012 += 0; // d2F1_dThetadu

                    //  out020 += 0; // d2F1_duds
                    //  out021 += 0; // d2F1_dudTheta
                    //  out022 += 0; // d2F1_du2

                    out100 += grhoa_rhosi * rhosiw_rhoaw*d2Z_ds2; // d2F2_ds2
                    out101 += d_grhoa_rhosi * rhosiw_rhoaw * dZ_ds +
                            grhoa_rhosi * d_rhosiw_rhoaw * dZ_ds +
                            grhoa_rhosi * rhosiw_rhoaw*d2Z_dsdTheta; // d2F2_dsdTheta
                    //  out102 += 0; // d2F2_dsdu

                    out110 += d_grhoa_rhosi * rhosiw_rhoaw * dZ_ds +
                            grhoa_rhosi * d_rhosiw_rhoaw * dZ_ds +
                            grhoa_rhosi * rhosiw_rhoaw*d2Z_dsdTheta; // d2F2_dThetads
                    out111 += d2_grhoa_rhosi * rhosiw_rhoaw * Z +
                            d_grhoa_rhosi * d_rhosiw_rhoaw * Z +
                            d_grhoa_rhosi * rhosiw_rhoaw * dZ_dTheta +
                            d_grhoa_rhosi * d_rhosiw_rhoaw * Z +
                            grhoa_rhosi * d2_rhosiw_rhoaw * Z +
                            grhoa_rhosi * d_rhosiw_rhoaw * dZ_dTheta +
                            d_grhoa_rhosi * rhosiw_rhoaw * dZ_dTheta +
                            grhoa_rhosi * d_rhosiw_rhoaw * dZ_dTheta +
                            grhoa_rhosi * rhosiw_rhoaw*d2Z_dTheta2; // d2F2_dTheta2
                    //  out112 += 0; // d2F2_dThetadu

                    //  out120 += 0; // d2F2_duds
                    //  out121 += 0; // d2F2_dudTheta
                    //  out122 += 0; // d2F2_du2

                    out200 += grhoa_rhosi * Hsi_Ha*d2Z_ds2; // d2F3_ds2
                    out201 += d_grhoa_rhosi * Hsi_Ha * dZ_ds +
                            grhoa_rhosi * d_Hsi_Ha * dZ_ds +
                            grhoa_rhosi * Hsi_Ha*d2Z_dsdTheta; // d2F3_dsdTheta
                    //  out202 += 0; // d2F3_dsdu

                    out210 += d_grhoa_rhosi * Hsi_Ha * dZ_ds +
                            grhoa_rhosi * d_Hsi_Ha * dZ_ds +
                            grhoa_rhosi * Hsi_Ha*d2Z_dThetads; // d2F3_dThetads
                    out211 += d2_grhoa_rhosi * Hsi_Ha * Z +
                            d_grhoa_rhosi * d_Hsi_Ha * Z +
                            d_grhoa_rhosi * Hsi_Ha * dZ_dTheta +
                            d_grhoa_rhosi * d_Hsi_Ha * Z +
                            grhoa_rhosi * d2_Hsi_Ha * Z +
                            grhoa_rhosi * d_Hsi_Ha * dZ_dTheta +
                            d_grhoa_rhosi * Hsi_Ha * dZ_dTheta +
                            grhoa_rhosi * d_Hsi_Ha * dZ_dTheta +
                            grhoa_rhosi * Hsi_Ha*d2Z_dTheta2; // d2F3_dTheta2
                    //  out212 += 0; // d2F3_dThetadu

                    //  out220 += 0; // d2F3_duds
                    //  out221 += 0; // d2F3_dudTheta
                    //  out222 += 0; // d2F3_du2
                }
            }
        }
    } // End of pure gravity

    if (degree >= 0) {
        m(0, out0);
        m(1, out1);
        m(2, out2);

        if (degree >= 1) {
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
            //                    printf("ff(%d, %d) = %g\n", i, j, m(i, j));
            //                }
            //            }

            if (degree >= 2) {
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

//void Flux2Comp2PhasesAdimensionalized::type(int t){
//    if (t == PURE_GRAVITY){
//        has_gravity = true;
//        has_horizontal = false;
//    }
//    else if (t == GRAVITY){
//        has_gravity = true;
//        has_horizontal = true;
//    }
//    else if (t == HORIZONTAL){
//        has_gravity = false;
//        has_horizontal = true;
//    }

//    return;
//}

