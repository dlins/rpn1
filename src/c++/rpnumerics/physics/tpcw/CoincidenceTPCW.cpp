#include "CoincidenceTPCW.h"

CoincidenceTPCW::CoincidenceTPCW(const Flux2Comp2PhasesAdimensionalized *f, const Accum2Comp2PhasesAdimensionalized * a) : HugoniotFunctionClass(*f), td(f->getThermo()),
phi(a->accumulationParams().component(0)) {
    phi = 1.0;
}

double CoincidenceTPCW::lambdas_function(const RealVector &u) {
    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);
    const Flux2Comp2PhasesAdimensionalized & fluxFunction = (const Flux2Comp2PhasesAdimensionalized &) getFluxFunction();
    fluxFunction.getHorizontalFlux()->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 1, m);

    return m(0, 0);
}

double CoincidenceTPCW::lambdae_function(const RealVector &u) {

    // First we define the Buckley-Leverett jet.

    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);
    const Flux2Comp2PhasesAdimensionalized & fluxFunction = (const Flux2Comp2PhasesAdimensionalized &) getFluxFunction();
    fluxFunction.getHorizontalFlux()-> Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 0, m);


    double f = m(0);
    double s = u.component(0);


    double rhosigmac;
    double drhosigmac_dT;
    double d2rhosigmac_dT2;

    double rhosigmaw;
    double drhosigmaw_dT;
    double d2rhosigmaw_dT2;

    double rhoac;
    double drhoac_dT;
    double d2rhoac_dT2;

    double rhoaw;
    double drhoaw_dT;
    double d2rhoaw_dT2;

    double Ha;
    double dHa_dT;
    double d2Ha_dT2;

    double Hsi;
    double dHsi_dT;
    double d2Hsi_dT2;

    double Hr;
    double dHr_dT;
    double d2Hr_dT;

    td->Diff_Rhosic(Theta, rhosigmac, drhosigmac_dT, d2rhosigmac_dT2);
    td->Diff_Rhosiw(Theta, rhosigmaw, drhosigmaw_dT, d2rhosigmaw_dT2);

    td->Diff_Rhoac(Theta, rhoac, drhoac_dT, d2rhoac_dT2);

    td->Diff_Rhoaw(Theta, rhoaw, drhoaw_dT, d2rhoaw_dT2);

    //    cout << rhoaw << drhoaw_dT << d2rhoaw_dT2 << endl;

    td->Diff_AqueousEnthalpyVol(Theta, Ha, dHa_dT, d2Ha_dT2);
    td->Diff_SuperCriticEnthalpyVol(Theta, Hsi, dHsi_dT, d2Hsi_dT2);

    td->Diff_RockEnthalpyVol(Theta, Hr, dHr_dT, d2Hr_dT);

    //  In this way we reproduce the artificial quantities given in Helmut's thesis numbers 3.13, 3.14, 3.15.

    double rho1 = rhosigmac - rhoac;
    double drho1_dT = drhosigmac_dT - drhoac_dT;

    double rho2 = rhosigmaw - rhoaw;
    double drho2_dT = drhosigmaw_dT - drhoaw_dT;

    double rho3 = Hsi - Ha;
    double drho3_dT = dHsi_dT - dHa_dT;

    //    double Cr = td->Cr();


    //  And finally we build equations 3.30, 3.31, 3.32 .

    double M = (drho3_dT * rho1 - rho3 * drho1_dT)*(rho1 * rhoaw - rho2 * rhoac) - (drho2_dT * rho1 - rho2 * drho1_dT)*(rho1 * Ha - rho3 * rhoac);
    double N1 = (dHa_dT * rho1 - rho3 * drhoac_dT)*(rho1 * rhoaw - rho2 * rhoac) - (rho1 * drhoaw_dT - rho2 * drhoac_dT)*(rho1 * Ha - rho3 * rhoac);
    double N2 = N1 + (dHr_dT / phi) * rho1 * (rho1 * rhoaw - rho2 * rhoac);

    double reduced_lambdae = (f * M + N1) / (s * M + N2);
    return reduced_lambdae;
}

double CoincidenceTPCW::HugoniotFunction(const RealVector &u) {

    double lambdas = lambdas_function(u);
    double lambdae = lambdae_function(u);
    return lambdas - lambdae;
}

void CoincidenceTPCW::completeCurve(std::vector<RealVector> & curve) {

}