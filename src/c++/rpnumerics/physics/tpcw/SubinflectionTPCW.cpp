#include "SubinflectionTPCW.h"

SubinflectionTPCW::SubinflectionTPCW(Thermodynamics_SuperCO2_WaterAdimensionalized *t, FracFlow2PhasesHorizontalAdimensionalized *f, double phi_) {
    td = t;
    fh = f;
    phi = phi_;
}

void SubinflectionTPCW::subinflection_function(double & reduced_lambdae, double & numeratorchiu, double & denominatorchiu, const RealVector &u) {

    // First we define the Buckley-Leverett jet.

    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(1);

    fh->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 0, m);
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
    double d2Hr_dT2;


    td->Diff_Rhosic(Theta, rhosigmac, drhosigmac_dT, d2rhosigmac_dT2);
    td->Diff_Rhosiw(Theta, rhosigmaw, drhosigmaw_dT, d2rhosigmaw_dT2);
    td->Diff_Rhoac(Theta, rhoac, drhoac_dT, d2rhoac_dT2);
    td->Diff_Rhoaw(Theta, rhoaw, drhoaw_dT, d2rhoaw_dT2);
    td->Diff_AqueousEnthalpyVol(Theta, Ha, dHa_dT, d2Ha_dT2);
    td->Diff_SuperCriticEnthalpyVol(Theta, Hsi, dHsi_dT, d2Hsi_dT2);




    //    fh->Diff_Rhosic(double Theta, double &rhosigmac, double &drhosigmac_dT, double &d2rhosigmac_dT2);
    //    fh->Diff_Rhosiw(double Theta, double &rhosigmaw, double &drhosigmaw_dT, double &d2rhosigmaw_dT2);
    //    fh->Diff_Rhoac(double Theta, double &rhoac, double &drhoac_dT, double &d2rhoac_dT2);
    //    fh->Diff_Rhoaw(double Theta, double &rhoaw, double &drhoaw_dT, double &d2rhoaw_dT2);
    //    fh->Diff_AqueousEnthalpyVol(double Theta, double &Ha, double &dHa_dT, double &d2Ha_dT2);
    //    fh->Diff_SuperCriticEnthalpyVol(double Theta, double &Hsi, double &dHsi_dT, double &d2Hsi_dT2);
    td->Diff_RockEnthalpyVol(Theta, Hr, dHr_dT, d2Hr_dT2);

    //  In this way we reproduce the artificial quantities given in Helmut's thesis numbers.

    double rho1 = rhosigmac - rhoac;
    double drho1_dT = drhosigmac_dT - drhoac_dT;
    double d2rho1_dT2 = d2rhosigmac_dT2 - d2rhoac_dT2;

    double rho2 = rhosigmaw - rhoaw;
    double drho2_dT = drhosigmaw_dT - drhoaw_dT;
    double d2rho2_dT2 = d2rhosigmaw_dT2 - d2rhoaw_dT2;

    double rho3 = Hsi - Ha;
    double drho3_dT = dHsi_dT - dHa_dT;
    double d2rho3_dT2 = d2Hsi_dT2 - d2Ha_dT2;


    //  And finally we build the equations that are needed for lambdae.
    //  Notice that these expressions will also be used for the method that calculates the Hugoniot function (change function's name TODO )

    double M = (drho3_dT * rho1 - rho3 * drho1_dT)*(rho1 * rhoaw - rho2 * rhoac) - (drho2_dT * rho1 - rho2 * drho1_dT)*(rho1 * Ha - rho3 * rhoac);

    double dM_dT = ((d2rho3_dT2 * rho1 + drho3_dT * drho1_dT) - (drho3_dT * drho1_dT + rho3 * d2rho1_dT2))*(rho1 * rhoaw - rho2 * rhoac) -
            ((d2rho2_dT2 * rho1 + drho2_dT * drho1_dT) - (drho2_dT * drho1_dT + rho2 * d2rho1_dT2))*(rho1 * Ha - rho3 * rhoac) +
            (drho3_dT * rho1 - rho3 * drho1_dT)*((drho1_dT * rhoaw + rho1 * drhoaw_dT) - (drho2_dT * rhoac + rho2 * drhoac_dT)) -
            (drho2_dT * rho1 - rho2 * drho1_dT)*((drho1_dT * Ha + rho1 * dHa_dT) - (drho3_dT * rhoac + rho3 * drhoac_dT));


    double N1 = (dHa_dT * rho1 - rho3 * drhoac_dT)*(rho1 * rhoaw - rho2 * rhoac) - (rho1 * drhoaw_dT - rho2 * drhoac_dT)*(rho1 * Ha - rho3 * rhoac);

    double dN1_dT = ((d2Ha_dT2 * rho1 + dHa_dT * drho1_dT) - (drho3_dT * drhoac_dT + rho3 * d2rhoac_dT2))*(rho1 * rhoaw - rho2 * rhoac) -
            ((drho1_dT * drhoaw_dT + rho1 * d2rhoaw_dT2) - (drho2_dT * drhoac_dT + rho2 * d2rhoac_dT2))*(rho1 * Ha - rho3 * rhoac) +
            (dHa_dT * rho1 - rho3 * drhoac_dT)*((drho1_dT * rhoaw + rho1 * drhoaw_dT) - (drho2_dT * rhoac + rho2 * drhoac_dT)) -
            (rho1 * drhoaw_dT - rho2 * drhoac_dT)*((drho1_dT * Ha + rho1 * dHa_dT) - (drho3_dT * rhoac + rho3 * drhoac_dT));


    double N2 = N1 + (dHr_dT / phi) * rho1 * (rho1 * rhoaw - rho2 * rhoac);

    double dN2_dT = dN1_dT + (d2Hr_dT2 / phi) * rho1 * (rho1 * rhoaw - rho2 * rhoac) +
            (dHr_dT / phi) * drho1_dT * (rho1 * rhoaw - rho2 * rhoac) +
            (dHr_dT / phi) * rho1 * ((drho1_dT * rhoaw + rho1 * drhoaw_dT) - (drho2_dT * rhoac + rho2 * drhoac_dT));


    reduced_lambdae = (f * M + N1) / (s * M + N2);


    double e1 = rho1 * drhoaw_dT - rho2*drhoac_dT;
    double e2 = rho1 * drhosigmaw_dT - rho2*drhosigmac_dT;
    double e3 = rhosigmaw * rhoac - rhosigmac*rhoaw;


    double Mchiu = (e2 - e1)*(N1 * rho1 - rhoac * M) + e3 * (dM_dT * rho1 - M * drho1_dT);
    double N1chiu = e1 * (N1 * rho1 - rhoac * M) + e3 * (dN1_dT * rho1 - M * drhoac_dT);
    double N2chiu = e1 * (N1 * rho1 - rhoac * M) + e3 * (dN2_dT * rho1 - M * drhoac_dT);

    numeratorchiu = f * Mchiu + N1chiu;
    denominatorchiu = s * Mchiu + N2chiu;

    return;
}

double SubinflectionTPCW::HugoniotFunction(const RealVector &u) {

    double reduc_lambdae;
    double numchiu;
    double denchiu;

    subinflection_function(reduc_lambdae, numchiu, denchiu, u);

    return numchiu - reduc_lambdae*denchiu;
}



