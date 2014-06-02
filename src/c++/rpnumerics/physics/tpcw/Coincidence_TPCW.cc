#include "Coincidence_TPCW.h"

Coincidence_TPCW::Coincidence_TPCW(const Flux2Comp2PhasesAdimensionalized *f, const Accum2Comp2PhasesAdimensionalized *a) : Coincidence(){
    fluxFunction_  = f;
    accumFunction_ = a;

    td = fluxFunction_->getThermo();
}

Coincidence_TPCW::~Coincidence_TPCW(){
}

void Coincidence_TPCW::lambdas(const RealVector &u, double &lambda_s, double &lambda_e, double &lambda_diff) const {
    double f, M, N1, s, N2, df0_du0;

    engine(u, f, M,  N1, s, N2, df0_du0);

    lambda_s = df0_du0;

    // This is the reduced lambda_e.
    lambda_e = (f * M + N1) / (s * M + N2);

    // Diff.
    //
    lambda_diff = lambda_s - lambda_e;

    return;
}

double Coincidence_TPCW::lambda_diff(const RealVector &p) const {
    double lambda_s, lambda_e, lambda_diff;

    lambdas(p, lambda_s, lambda_e, lambda_diff);

    return lambda_diff;
}


double Coincidence_TPCW::lambda_s(const RealVector &p) const {
    double lambda_s, lambda_e, lambda_diff;

    lambdas(p, lambda_s, lambda_e, lambda_diff);

    return lambda_s;
}

// This is the reduced lambda_e.
//
double Coincidence_TPCW::lambda_e(const RealVector &p) const {
    double lambda_s, lambda_e, lambda_diff;

    lambdas(p, lambda_s, lambda_e, lambda_diff);

    return lambda_e;
}

void Coincidence_TPCW::engine(const RealVector &u, double &f, double &M, double &N1, double &s, double &N2, double &df0_du0) const {
    double phi = accumFunction_->accumulationParams().component(0);

    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);

    fluxFunction_->getHorizontalFlux()->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 1.0, m);

    // Lambda_s.
    //
    df0_du0 = m.get(0, 0);

    // Reduced lambda_e
    // First we define the Buckley-Leverett jet.
    f = m.get(0);
    s = u.component(0);

    JetMatrix rhosicj(1);
    td->Rhosic_jet(Theta, 1, rhosicj);
    double rhosigmac     = rhosicj.get(0);
    double drhosigmac_dT = rhosicj.get(0, 0);

    JetMatrix rhosiwj(1);
    td->Rhosiw_jet(Theta, 1, rhosiwj);
    double rhosigmaw     = rhosiwj.get(0);
    double drhosigmaw_dT = rhosiwj.get(0, 0);

    JetMatrix rhoacj(1);
    td->Rhoac_jet(Theta, 1, rhoacj);
    double rhoac     = rhoacj.get(0);
    double drhoac_dT = rhoacj.get(0, 0);

    JetMatrix rhoawj(1);
    td->Rhoaw_jet(Theta, 1, rhoawj);
    double rhoaw     = rhoawj.get(0);
    double drhoaw_dT = rhoawj.get(0, 0);

    JetMatrix Haj(1);
    td->AqueousEnthalpyVol_jet(Theta, 1, Haj);
    double Ha     = Haj.get(0);
    double dHa_dT = Haj.get(0, 0);

    JetMatrix Hsij(1);
    td->SuperCriticEnthalpyVol_jet(Theta, 1, Hsij);
    double Hsi     = Hsij.get(0);
    double dHsi_dT = Hsij.get(0, 0);

    JetMatrix Hrj(1);
    td->RockEnthalpyVol_jet(Theta, 1, Hrj);
    double Hr     = Hrj.get(0);
    double dHr_dT = Hrj.get(0, 0);

    //  In this way we reproduce the artificial quantities given in Helmut's thesis numbers 3.13, 3.14, 3.15.

    double rho1 = rhosigmac - rhoac;
    double drho1_dT = drhosigmac_dT - drhoac_dT;

    double rho2 = rhosigmaw - rhoaw;
    double drho2_dT = drhosigmaw_dT - drhoaw_dT;

    double rho3 = Hsi - Ha;
    double drho3_dT = dHsi_dT - dHa_dT;

    //    double Cr = td->Cr();


    //  And finally we build equations 3.30, 3.31, 3.32 .

    M = (drho3_dT * rho1 - rho3 * drho1_dT)*(rho1 * rhoaw - rho2 * rhoac) - (drho2_dT * rho1 - rho2 * drho1_dT)*(rho1 * Ha - rho3 * rhoac);
    N1 = (dHa_dT * rho1 - rho3 * drhoac_dT)*(rho1 * rhoaw - rho2 * rhoac) - (rho1 * drhoaw_dT - rho2 * drhoac_dT)*(rho1 * Ha - rho3 * rhoac);
    N2 = N1 + (dHr_dT / phi) * rho1 * (rho1 * rhoaw - rho2 * rhoac);

    return;
}

bool Coincidence_TPCW::extension_basis(const RealVector &u, double &fe, double &se) const {
    double expw = fluxFunction_->get_expw();
    double expg = fluxFunction_->get_expg();

    double f, M, N1, s, N2, df0_du0;

    engine(u, f, M,  N1, s, N2, df0_du0);

    fe = -N1/M;
    se = -N2/M;

    return (fe != std::numeric_limits<double>::infinity() &&
            se != std::numeric_limits<double>::infinity());
}

