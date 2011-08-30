#include "Flux2Comp2PhasesAdimensionalized.h"

Flux2Comp2PhasesAdimensionalized::Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized &a) : FluxFunction(a.fluxParams()) {

    //    const Flux2Comp2PhasesAdimensionalized_Params & fluxParams = (const Flux2Comp2PhasesAdimensionalized_Params &) a.fluxParams();
    cout << "Construtor de copia de Flux2CompPhasesAdimensionalized" << endl;


    TD = a.TD;
    FH = new FracFlow2PhasesHorizontalAdimensionalized(this);
    FV = new FracFlow2PhasesVerticalAdimensionalized(this);

    reducedFlux = new ReducedFlux2Comp2PhasesAdimensionalized(this);
    abs_perm = a.abs_perm;
    sin_beta = a.sin_beta;
    const_gravity = a.const_gravity;
    grav = a.grav;
    has_gravity = a.has_gravity;
    has_horizontal = a.has_horizontal;
}

Flux2Comp2PhasesAdimensionalized::Flux2Comp2PhasesAdimensionalized(const Flux2Comp2PhasesAdimensionalized_Params &param) : FluxFunction(param) {

    cout << "Tamanho dos parametros: " << param.params().size() << endl;


    abs_perm = param.component(0);
    sin_beta = param.component(1);
    //    const_gravity = param.component(2);


    const_gravity = 9.8;

    cnw = param.component(4);
    cng = param.component(5);
    expw = param.component(6);
    expg = param.component(7);


    TD = param.get_thermodynamics();


    FH = new FracFlow2PhasesHorizontalAdimensionalized(this);
    FV = new FracFlow2PhasesVerticalAdimensionalized(this);
    reducedFlux = new ReducedFlux2Comp2PhasesAdimensionalized(this);

    has_gravity = param.has_gravity();
    has_horizontal = param.has_horizontal();

    grav = abs_perm * sin_beta*const_gravity;
}

RpFunction * Flux2Comp2PhasesAdimensionalized::clone() const {

    return new Flux2Comp2PhasesAdimensionalized(*this);
}

Flux2Comp2PhasesAdimensionalized::~Flux2Comp2PhasesAdimensionalized() {

    delete FH;
    delete FV;
    delete reducedFlux;
}

int Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized::jet(const WaveState &w, JetMatrix &m, int degree) const {
    double s = w(0); // s_{sigma} = sg in FracFlow2PhasesHorizontal & FracFlow2PhasesVertical
    double Theta = w(1);

    // Some auxiliary variables
    double Hr, d_Hr, d2_Hr;
    fluxComplete_->TD->Diff_RockEnthalpyVol(Theta, Hr, d_Hr, d2_Hr);
    double Ha, d_Ha, d2_Ha;
    fluxComplete_->TD->Diff_AqueousEnthalpyVol(Theta, Ha, d_Ha, d2_Ha);
    double Hsi, d_Hsi, d2_Hsi;
    fluxComplete_->TD->Diff_SuperCriticEnthalpyVol(Theta, Hsi, d_Hsi, d2_Hsi);

    double rhosic, d_rhosic, d2_rhosic;
    fluxComplete_->TD->Diff_Rhosic(Theta, rhosic, d_rhosic, d2_rhosic);

    double rhosiw, d_rhosiw, d2_rhosiw;
    fluxComplete_->TD->Diff_Rhosiw(Theta, rhosiw, d_rhosiw, d2_rhosiw);

    double rhoac, d_rhoac, d2_rhoac;
    fluxComplete_->TD->Diff_Rhoac(Theta, rhoac, d_rhoac, d2_rhoac);

    double rhoaw, d_rhoaw, d2_rhoaw;
    fluxComplete_->TD->Diff_Rhoaw(Theta, rhoaw, d_rhoaw, d2_rhoaw);

    // Output
    double out0 = 0.0, out1 = 0.0, out2 = 0.0;

    double out00 = 0.0, out01 = 0.0;
    double out10 = 0.0, out11 = 0.0;
    double out20 = 0.0, out21 = 0.0;

    double out000 = 0.0, out001 = 0.0;
    double out010 = 0.0, out011 = 0.0;

    double out100 = 0.0, out101 = 0.0;
    double out110 = 0.0, out111 = 0.0;

    double out200 = 0.0, out201 = 0.0;
    double out210 = 0.0, out211 = 0.0;


    double f, df_ds, df_dTheta, d2f_ds2, d2f_dsdTheta, d2f_dThetads, d2f_dTheta2; // f=f_{sigma}, s=s_{sigma}

    JetMatrix horizontal(2);

    fluxComplete_->FH->Diff_FracFlow2PhasesHorizontalAdimensionalized(1. - s, Theta, degree, horizontal);


    // Here we recover the values of the Fractional Flow Function.

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
        out0 = (rhosic * f + rhoac * (1.0 - f));
        out1 = (rhosiw * f + rhoaw * (1.0 - f));
        out2 = (Hsi * f + Ha * (1.0 - f));

        if (degree >= 1) {
            out00 = (rhosic - rhoac) * df_ds;
            out01 = (d_rhosic * f + d_rhoac * (1.0 - f) + (rhosic - rhoac) * df_dTheta);

            out10 = (rhosiw - rhoaw) * df_ds;
            out11 = (d_rhosiw * f + d_rhoaw * (1.0 - f) + (rhosiw - rhoaw) * df_dTheta);

            out20 = (Hsi - Ha) * df_ds;
            out21 = (d_Hsi * f + d_Ha * (1.0 - f) + (Hsi - Ha) * df_dTheta);

            if (degree >= 2) {
                out000 = (rhosic - rhoac) * d2f_ds2;
                out001 = ((d_rhosic - d_rhoac) * df_ds + (rhosic - rhoac) * d2f_dsdTheta);

                out010 = out001; // Mixed partial
                out011 = (d2_rhosic * f + d2_rhoac * (1.0 - f) + 2 * (d_rhosic - d_rhoac) * df_dTheta + (rhosic - rhoac) * d2f_dTheta2);

                out100 = (rhosiw - rhoaw) * d2f_ds2;
                out101 = ((d_rhosiw - d_rhoaw) * df_ds + (rhosiw - rhoaw) * d2f_dsdTheta);

                out110 = out101; // Mixed partial
                out111 = (d2_rhosiw * f + d2_rhoaw * (1.0 - f) + 2 * (d_rhosiw - d_rhoaw) * df_dTheta + (rhosiw - rhoaw) * d2f_dTheta2);

                out200 = (Hsi - Ha) * d2f_ds2;
                out201 = ((d_Hsi - d_Ha) * df_ds + (Hsi - Ha) * d2f_dsdTheta);

                out210 = out201; // Mixed partial
                out211 = (d2_Hsi * f + d2_Ha * (1.0 - f) + 2 * (d_Hsi - d_Ha) * df_dTheta + (Hsi - Ha) * d2f_dTheta2);

            }
        }
    }

    if (degree >= 0) {
        m(0, out0);
        m(1, out1);
        m(2, out2);

        if (degree >= 1) {
            m(0, 0, out00);
            m(0, 1, out01);

            m(1, 0, out10);
            m(1, 1, out11);

            m(2, 0, out20);
            m(2, 1, out21);

            if (degree >= 2) {
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

    return 2; //SUCCESSFUL_PROCEDURE;
}

Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized(Flux2Comp2PhasesAdimensionalized * outer) : FluxFunction(outer->fluxParams()) {

    fluxComplete_ = outer;

}

Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized(Flux2Comp2PhasesAdimensionalized * outer) {
    fluxComplete_ = outer;
}

Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesVerticalAdimensionalized::FracFlow2PhasesVerticalAdimensionalized(Flux2Comp2PhasesAdimensionalized * outer) {

    fluxComplete_ = outer;

}

RpFunction * Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized::clone() const {

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

Thermodynamics_SuperCO2_WaterAdimensionalized * Flux2Comp2PhasesAdimensionalized::getThermo() const{
    return TD;
}

Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized * Flux2Comp2PhasesAdimensionalized::getHorizontalFlux()const {
    return FH;
}

Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesVerticalAdimensionalized * Flux2Comp2PhasesAdimensionalized::getVerticalFlux()const {
    return FV;
}

Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized * Flux2Comp2PhasesAdimensionalized::getReducedFlux()const  {
    return reducedFlux;
}

int Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized::Diff_FracFlow2PhasesHorizontalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m) {
    // Esta funcao retorna a funcao de fluxo da agua e seu jato;
    // esta correta para uso do Helmut apenas
    // ja tiramos grw, grg de todos os lugares e as variaveis que as usavam


    double T = fluxComplete_->TD->Theta2T(Theta);

    //    double T = Flux2Comp2PhasesAdimensionalized::getThermo()->Theta2T(Theta);

    // Auxiliary variables
    double sg, law, lag;
    double dlaw_dsw, dlag_dsw;
    double d2law_dsw2, d2lag_dsw2;
    double la, dla_dsw, d2la_dsw2;
    double dflw_dsw, dflw_dT; // Derivatives of the Fractional Flow of water
    double d2flw_dsw2; // d2flw_dsw2 // Second derivative of Fractional Flow of water

    double flw; // Fractional Flow for water

    // Some variables to facilitate the operations with powers
    double pow0, pow1, pow2;

    double nuw, nug; // nuw= 1/muw , nug=1/mug
    double dnuw_dT, d2nuw_dT2; // This values are assigned using TD
    // dnuw_dT = dnuw_dT(T).
    // d2nuw_dT2 = d2nuw_dT2(T).

    double dnug_dT, d2nug_dT2; // This values are assigned using TD
    // dnug_dT = dnug_dT(T),
    // d2nug_dT2 = d2nug_dT2(T).

    double muw, dmuw_dT, d2muw_dT2;
    double mug, dmug_dT, d2mug_dT2;

    double dlaw_dT, d2law_dT2; // Temperature derivatives of law (are not passed as parameters).

    double dlag_dT, d2lag_dT2; // Temperature derivatives of lag (are not passed as parameters).

    double d2law_dswdT; // Mixed derivative (it is not passed as parameter).
    double d2lag_dswdT; // Mixed derivative (it is not passed as parameter).

    double dla_dT;
    double d2flw_dswdT;
    double d2la_dswdT;
    double d2flw_dT2;
    double d2la_dT2;

    // Finally we assign sg
    sg = 1. - sw;

    fluxComplete_->TD->inv_muw(T, nuw, dnuw_dT, d2nuw_dT2);
    fluxComplete_->TD->inv_mug(T, nug, dnug_dT, d2nug_dT2);

    fluxComplete_->TD->muw(T, muw, dmuw_dT, d2muw_dT2);
    fluxComplete_->TD->mug(T, mug, dmug_dT, d2mug_dT2);


    // Before evaluating proper
    if (sw < fluxComplete_->cnw) {
        law = 0.;
        dlaw_dsw = 0.;
        d2law_dsw2 = 0.;

        dlaw_dT = 0.;
        d2law_dT2 = 0.;

        d2law_dswdT = 0.; // remember that d2law_dswdT is equal to d2law_dTdsw
    } else {

        double temp = (sw - fluxComplete_->cnw) / (1. - fluxComplete_->cnw - fluxComplete_->cng);

        pow2 = 0.5 * pow(temp, fluxComplete_->expw - 2.); // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-2}
        // This model coincides with the formulae in Helmut's Thesis. 0.5 is the re-scaling factor.

        pow1 = temp*pow2; // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-1}
        // Observe that pow1 = pow((sw - cnw)/(1.-cnw-cng),expw-1) ;

        pow0 = temp*pow1; // 0.5*( (sw-s_{connate water})/(1-s_{connate water-s_{con gas}}) )^{expw}
        // Observe that pow0 = pow((sw - cnw)/(1.-cnw-cng),expw)


        law = pow0*nuw; // k_{rw}/mu_{w}(T) This is equivalent to k_{ra}/mu_{a} in Helmut's thesis.
        dlaw_dsw = fluxComplete_->expw * pow1*nuw; // expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-1}/mu_{w}  derivation w.r.t sw
        d2law_dsw2 = (fluxComplete_->expw - 1.) * fluxComplete_->expw * pow2*nuw; // (expw - 1.) * expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-2}/mu_{w}

        dlaw_dT = pow0*dnuw_dT; // the value of the derivatives of muw
        // will be passed by the viscosity function.
        d2law_dT2 = pow0*d2nuw_dT2; //

        d2law_dswdT = fluxComplete_->expw * pow1*dnuw_dT;
    }

    if (sg < fluxComplete_->cng) {
        lag = 0.;
        dlag_dsw = 0.;
        d2lag_dsw2 = 0.;

        dlag_dT = 0.;
        d2lag_dT2 = 0.;

        d2lag_dswdT = 0.;
    } else {

        double temp = (sg - fluxComplete_->cng) / (1. - fluxComplete_->cnw - fluxComplete_->cng);
        pow2 = 0.95 * pow(temp, fluxComplete_->expg - 2.); // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}
        pow1 = temp*pow2; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1} Observe that pow1 = pow(temp, expg-1)
        pow0 = temp*pow1; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg}   Observe that pow0 = pow(temp, expg) ;

        lag = pow0*nug; //  k_{rg}/mu_{g}(T)  this viscosity is function of temperature   ;
        dlag_dsw = -fluxComplete_->expg * pow1*nug; //  - expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1}/mu_{g}
        d2lag_dsw2 = (fluxComplete_->expg - 1.) * fluxComplete_->expg * pow2*nug; // (expg-1)*expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}/mu_{g}

        dlag_dT = pow0*dnug_dT; // derivatives given by the viscosity function
        d2lag_dT2 = pow0*d2nug_dT2; //

        d2lag_dswdT = -fluxComplete_->expg * pow1*dnug_dT; //
    }

    /* Evaluate sum of la's and its first and second derivatives */
    /* --------------------------------------------------------  */
    la = law + lag;
    dla_dsw = dlaw_dsw + dlag_dsw;
    d2la_dsw2 = d2law_dsw2 + d2lag_dsw2;
    d2la_dswdT = d2law_dswdT + d2lag_dswdT;
    dla_dT = dlaw_dT + dlag_dT;
    d2la_dT2 = d2law_dT2 + d2lag_dT2;

    d2la_dswdT = d2law_dswdT + d2lag_dswdT;

    // Evaluate the Flux Function
    // tw = (vel + lag*(grw - grg));

    flw = law / la;

    // F
    if (degree >= 0) {

        m(0, (1. - flw)); // We are giving the value of flg=1-flw, the fractional flow for gas, as required in Flux2Comp2PhasesAdimensionalized as
        // the output of the jet!!! IMPORTANT!

        // Jacobian
        if (degree >= 1) {
            /* Evaluate first derivatives of fluxes relative to gas saturation */
            /* ------------------------------------------------------- */
            dflw_dsw = (la * dlaw_dsw - law * dla_dsw) / (la * la);
            m(0, 0, dflw_dsw); /* Now observe that from the chain rule dflg_dsg=dflw_dsw HERE WE DO NOT NEED TO WORRY ABOUT THE DIMENSION */

            /* Evaluate first derivatives of fluxes relative to temperature */
            dflw_dT = (la * dlaw_dT - law * dla_dT) / (la * la);
            m(0, 1, (-dflw_dT) * fluxComplete_->TD->T_typical()); // We are giving dflg_dTheta = (  -dflw_dT( T (Theta) )  )*T_typical_
            // Hessian
            if (degree == 2) {
                /* Evaluate second derivatives of fluxes */
                /* ------------------------------------- */
                d2flw_dsw2 = ((la * d2law_dsw2 - law * d2la_dsw2) / la - 2. * dla_dsw * dflw_dsw) / la;

                d2flw_dT2 = (la * la * (la * d2law_dT2 - law * d2la_dT2) - 2. * la * dla_dT * (la * dlaw_dT - law * dla_dT)) / (la * la * la * la);

                /* Mixed derivative */
                /*
                d2flw_dswdT=d2flg_dswdT ;
                 */
                d2flw_dswdT = (la * la * (dla_dT * dlaw_dsw + la * d2law_dswdT - dlaw_dT * dla_dsw - law * d2la_dswdT)
                        - 2. * la * dla_dT * (la * dlaw_dsw - law * dla_dsw)) / (la * la * la * la);

                //               m(0, 0, 0, j000);
                //               m(0, 0, 1, j001);
                //               m(0, 1, 0, j010);
                //               m(0, 1, 1, j011);

                m(0, 0, 0, -d2flw_dsw2); // We are passing d2fg_dsg2  NO CHANGE!
                m(0, 0, 1, (d2flw_dswdT) * fluxComplete_->TD->T_typical()); // double j001 = d2flg_dsgdTheta = d2flg_dsgdT(T(Theta))*T_typical_  = d2flg_dswdT*T_typical_ = d2flw_dswdT*T_typical_;
                m(0, 1, 0, (d2flw_dswdT) * fluxComplete_->TD->T_typical()); // double j010 = j001;
                m(0, 1, 1, (-d2flw_dT2) * fluxComplete_->TD->T_typical() * fluxComplete_->TD->T_typical()); // double j011 = d2flg_dTheta2 = d2flg_dT2*T_typical_^2 = -d2flw_dsw2*T_typical_^2 // CHECKED BY HELMUT 21/10/2010

            }
        }
    }
    return 2;
}

int Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesVerticalAdimensionalized::Diff_FracFlow2PhasesVerticalAdimensionalized(double sw, double Theta, int degree, JetMatrix &m) {
    double T = fluxComplete_->TD->Theta2T(Theta);

    double d2Z_dsw2; // <- Unique to Vertical (only different variable with respect to Horizontal)
    double sg, law, lag;
    double dlaw_dsw, dlag_dsw;
    double d2law_dsw2, d2lag_dsw2;
    double la, dla_dsw, d2la_dsw2;
    double dflw_dsw, dflw_dT; // Derivatives of the Fractional Flow of water
    double d2flw_dsw2; // Second derivative of Fractional Flow of water

    double flw; // Fractional Flow for water

    // Some variables to facilitate the operations with powers
    double pow0, pow1, pow2;

    double nuw, nug; // nuw= 1/muw , nug=1/mug
    double dnuw_dT, d2nuw_dT2; // This values are assigned using TD
    // dnuw_dT = dnuw_dT(T).
    // d2nuw_dT2 = d2nuw_dT2(T).

    double dnug_dT, d2nug_dT2; // This values are assigned using TD
    // dnug_dT = dnug_dT(T),
    // d2nug_dT2 = d2nug_dT2(T).

    double dlaw_dT, d2law_dT2; // Temperature derivatives of law (are not passed as parameters).

    double dlag_dT, d2lag_dT2; // Temperature derivatives of lag (are not passed as parameters).

    double d2law_dswdT; // Mixed derivative (it is not passed as parameter).
    double d2lag_dswdT; // Mixed derivative (it is not passed as parameter).

    double dla_dT;
    double d2flw_dswdT;
    double d2la_dswdT;
    double d2flw_dT2;
    double d2la_dT2;

    // Finally we assign sg
    sg = 1. - sw;

    fluxComplete_->TD->inv_muw(T, nuw, dnuw_dT, d2nuw_dT2);
    fluxComplete_->TD->inv_mug(T, nug, dnug_dT, d2nug_dT2);


    // Before evaluating proper
    if (sw < fluxComplete_->cnw) {
        law = 0.;
        dlaw_dsw = 0.;
        d2law_dsw2 = 0.;

        dlaw_dT = 0.;
        d2law_dT2 = 0.;

        d2law_dswdT = 0.; // remember that d2law_dswdT is equal to d2law_dTdsw
    } else {

        double temp = (sw - fluxComplete_->cnw) / (1. - fluxComplete_->cnw - fluxComplete_->cng);

        pow2 = 0.5 * pow(temp, fluxComplete_->expw - 2.); // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-2}
        // This model coincides with the formulae in Helmut's Thesis. 0.5 is the re-scaling factor.

        pow1 = temp*pow2; // 0.5*( (sw-s_{connate water})/(1-s_{connate water}-s_{con gas}) )^{expw-1}
        // Observe that pow1 = pow((sw - cnw)/(1.-cnw-cng),expw-1) ;

        pow0 = temp*pow1; // 0.5*( (sw-s_{connate water})/(1-s_{connate water-s_{con gas}}) )^{expw}
        // Observe that pow0 = pow((sw - cnw)/(1.-cnw-cng),expw)


        law = pow0*nuw; // k_{rw}/mu_{w}(T) This is equivalent to k_{ra}/mu_{a} in Helmut's thesis.
        dlaw_dsw = fluxComplete_->expw * pow1*nuw; // expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-1}/mu_{w}  derivation w.r.t sw
        d2law_dsw2 = (fluxComplete_->expw - 1.) * fluxComplete_->expw * pow2*nuw; // (expw - 1.) * expw*( (sw-s_{connate water })/(1.-cnw-cng) )^{expw-2}/mu_{w}

        dlaw_dT = pow0*dnuw_dT; // the value of the derivatives of muw
        // will be passed by the viscosity function.
        d2law_dT2 = pow0*d2nuw_dT2; //

        d2law_dswdT = fluxComplete_->expw * pow1*dnuw_dT;
    }

    if (sg < fluxComplete_->cng) {
        lag = 0.;
        dlag_dsw = 0.;
        d2lag_dsw2 = 0.;

        dlag_dT = 0.;
        d2lag_dT2 = 0.;

        d2lag_dswdT = 0.;
    } else {

        double temp = (sg - fluxComplete_->cng) / (1. - fluxComplete_->cnw - fluxComplete_->cng);
        pow2 = 0.95 * pow(temp, fluxComplete_->expg - 2.); // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}
        pow1 = temp*pow2; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1} Observe that pow1 = pow(temp, expg-1)
        pow0 = temp*pow1; // ( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg}   Observe that pow0 = pow(temp, expg) ;

        lag = pow0*nug; //  k_{rg}/mu_{g}(T)  this viscosity is function of temperature   ;
        dlag_dsw = -fluxComplete_->expg * pow1*nug; //  - expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-1}/mu_{g}
        d2lag_dsw2 = (fluxComplete_->expg - 1.) * fluxComplete_->expg * pow2*nug; // (expg-1)*expg*( (sg-s_{connate gas})/(1.-cnw-cng) )^{expg-2}/mu_{g}

        dlag_dT = pow0*dnug_dT; // derivatives given by the viscosity function
        d2lag_dT2 = pow0*d2nug_dT2; //

        d2lag_dswdT = -fluxComplete_->expg * pow1*dnug_dT; //
    }

    /* Evaluate sum of la's and its first and second derivatives */
    /* --------------------------------------------------------  */
    la = law + lag;
    dla_dsw = dlaw_dsw + dlag_dsw;
    d2la_dsw2 = d2law_dsw2 + d2lag_dsw2;
    d2la_dswdT = d2law_dswdT + d2lag_dswdT;
    dla_dT = dlaw_dT + dlag_dT;
    d2la_dT2 = d2law_dT2 + d2lag_dT2;

    d2la_dswdT = d2law_dswdT + d2lag_dswdT;

    flw = law / la;

    // Z
    if (degree >= 0) {

        m(0, flw * lag); // double j0 = Z = flw*lag;

        // Jacobian
        if (degree >= 1) {
            /* Evaluate first derivative of Z relative to gas saturation */
            /* --------------------------------------------------------- */

            dflw_dsw = (la * dlaw_dsw - law * dla_dsw) / (la * la); // BE CAREFUL, WE ARE USING THE CHAIN RULE TO PASS THE CORRECT DERIVATIVE
            m(0, 0, -(dflw_dsw * lag + flw * dlag_dsw)); // this is double j00 = dZ_dsg= -dZ_dsw = -(dflw_dsw*lag + flw*dlag_dsw); NO CHANGE FOR DIM-LESS

            /* Evaluate first derivative of Z relative to temperature */
            dflw_dT = (la * dlaw_dT - law * dla_dT) / (la * la);

            m(0, 1, (dflw_dT * lag + flw * dlag_dT) * fluxComplete_->getThermo()->T_typical()); // this is double j01 =  dZ_dTheta = dZ_dT*T_typical_  = (dflw_dT*lag + flw*dlag_dT)*T_typical_;

            // Hessian
            if (degree == 2) {

                /* Evaluate second derivatives of Z */
                /* ------------------------------------- */
                d2flw_dsw2 = ((la * d2law_dsw2 - law * d2la_dsw2) / la - 2. * dla_dsw * dflw_dsw) / la;

                d2Z_dsw2 = d2flw_dsw2 * lag + 2. * dflw_dsw * dlag_dsw + flw*d2lag_dsw2;

                double j000 = d2Z_dsw2; // Note we are passing d2Z_dsg2 = d2Z_dsw2 NO CHANGE FOR DIM-LESS



                /* Mixed derivative */
                /*
                d2Z_dsgdT = -d2Z_dswdT = -( dflw_dswdT*lag + dflw_dsw*dlag_dT +
                                            dflw_dT*dlag_dsw + flw*d2lag_dswdT  );
                 */


                d2flw_dT2 = (la * la * (la * d2law_dT2 - law * d2la_dT2) - 2. * la * dla_dT * (la * dlaw_dT - law * dla_dT)) / (la * la * la * la);

                d2flw_dswdT = (la * la * (dla_dT * dlaw_dsw + la * d2law_dswdT - dlaw_dT * dla_dsw - law * d2la_dswdT)
                        - 2. * la * dla_dT * (la * dlaw_dsw - law * dla_dsw)) / (la * la * la * la);

                double j001 = (-(d2flw_dswdT * lag + dflw_dsw * dlag_dT +
                        dflw_dT * dlag_dsw + flw * d2lag_dswdT)) * fluxComplete_->getThermo()->T_typical(); // Observe that we are passing really d2Z_dsgdTheta = d2Z_dsgdT*T_typical_

                double j010 = j001;

                /*
                d2Z_dT2 = d2flw_dT2*lag + dflw_dT*dlag_dT +
                          dflw_dT*dlag_dT + flw*d2lag_dT2;
                 */

                double j011 = (d2flw_dT2 * lag + dflw_dT * dlag_dT +
                        dflw_dT * dlag_dT + flw * d2lag_dT2) * fluxComplete_->getThermo()->T_typical() * fluxComplete_->getThermo()->T_typical(); // d2Z_dTheta2 = d2Z_dT2 * T_typical_^2

                m(0, 0, 0, j000);
                m(0, 0, 1, j001);
                m(0, 1, 0, j010);
                m(0, 1, 1, j011);
            }
        }
    }
    return 2;
}

void Flux2Comp2PhasesAdimensionalized::type(int t) {
    if (t == FLUX2COMP2PHASESADIMENSIONALIZED_PURE_GRAVITY) {
        has_gravity = true;
        has_horizontal = false;
    } else if (t == FLUX2COMP2PHASESADIMENSIONALIZED_GRAVITY) {
        has_gravity = true;
        has_horizontal = true;
    } else if (t == FLUX2COMP2PHASESADIMENSIONALIZED_HORIZONTAL) {
        has_gravity = false;
        has_horizontal = true;
    }

    return;
}

