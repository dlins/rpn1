#include "SubinflectionTP.h"

SubinflectionTP::SubinflectionTP(double p) {
    gv = 0;
    phi=p;
}

void SubinflectionTP::subinflection_function(double & reduced_lambdae, double & numeratorchiu,
        double & denominatorchiu, const RealVector &u) {

    // First we define the Buckley-Leverett jet.

    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(1);

    //    const Flux2Comp2PhasesAdimensionalized & fluxFunction = (const Flux2Comp2PhasesAdimensionalized &) getFluxFunction();

    fluxFunction_-> Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 0, m);


    double f = m.get(0);
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


//    td->Diff_Rhosic(Theta, rhosigmac, drhosigmac_dT, d2rhosigmac_dT2);
    JetMatrix rhosicj(1);
    td->Rhosic_jet(Theta, 2, rhosicj);
    rhosigmac = rhosicj.get(0); drhosigmac_dT = rhosicj.get(0, 0); d2rhosigmac_dT2 = rhosicj.get(0, 0, 0);



//    td->Diff_Rhosiw(Theta, rhosigmaw, drhosigmaw_dT, d2rhosigmaw_dT2);
    JetMatrix rhosiwj(1);
    td->Rhosiw_jet(Theta, 2, rhosiwj);
    rhosigmaw = rhosiwj.get(0); drhosigmaw_dT = rhosiwj.get(0, 0); d2rhosigmaw_dT2 = rhosiwj.get(0, 0, 0);


//    td->Diff_Rhoac(Theta, rhoac, drhoac_dT, d2rhoac_dT2);
    JetMatrix rhoacj(1);
    td->Rhoac_jet(Theta, 2, rhoacj);
    rhoac = rhoacj.get(0); drhoac_dT = rhoacj.get(0, 0); d2rhoac_dT2 = rhoacj.get(0, 0, 0);

//    td->Diff_Rhoaw(Theta, rhoaw, drhoaw_dT, d2rhoaw_dT2);
    JetMatrix rhoawj(1);
    td->Rhoaw_jet(Theta, 2, rhoawj);
    rhoaw = rhoawj.get(0); drhoaw_dT = rhoawj.get(0, 0); d2rhoaw_dT2 = rhoawj.get(0, 0, 0);

//    td->Diff_AqueousEnthalpyVol(Theta, Ha, dHa_dT, d2Ha_dT2);
    JetMatrix Haj(1);
    td->AqueousEnthalpyVol_jet(Theta, 2, Haj);
    Ha = Haj.get(0); dHa_dT = Haj.get(0, 0); d2Ha_dT2 = Haj.get(0, 0, 0);

//    td->Diff_SuperCriticEnthalpyVol(Theta, Hsi, dHsi_dT, d2Hsi_dT2);
    JetMatrix Hsij(1);
    td->AqueousEnthalpyVol_jet(Theta, 2, Hsij);
    Hsi = Hsij.get(0); dHsi_dT = Hsij.get(0, 0); d2Hsi_dT2 = Hsij.get(0, 0, 0);


    //    fh->Diff_Rhosic(double Theta, double &rhosigmac, double &drhosigmac_dT, double &d2rhosigmac_dT2);
    //    fh->Diff_Rhosiw(double Theta, double &rhosigmaw, double &drhosigmaw_dT, double &d2rhosigmaw_dT2);
    //    fh->Diff_Rhoac(double Theta, double &rhoac, double &drhoac_dT, double &d2rhoac_dT2);
    //    fh->Diff_Rhoaw(double Theta, double &rhoaw, double &drhoaw_dT, double &d2rhoaw_dT2);
    //    fh->Diff_AqueousEnthalpyVol(double Theta, double &Ha, double &dHa_dT, double &d2Ha_dT2);
    //    fh->Diff_SuperCriticEnthalpyVol(double Theta, double &Hsi, double &dHsi_dT, double &d2Hsi_dT2);


//    td->Diff_RockEnthalpyVol(Theta, Hr, dHr_dT, d2Hr_dT2);
    JetMatrix Hrj(1);
    td->RockEnthalpyVol_jet(Theta, 2, Hrj);
    Hr = Hrj.get(0); dHr_dT = Hrj.get(0, 0); d2Hr_dT2 = Hrj.get(0, 0, 0);


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

int SubinflectionTP::function_on_square(double *foncub, int i, int j) {
    double f_aux[4];
    double reduc_lambdae;
    double numchiu;
    double denchiu;

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            subinflection_function(reduc_lambdae, numchiu, denchiu, RealVector(2, gv->grid(i + l, j + k).components()));
            f_aux[l * 2 + k] = (numchiu - reduc_lambdae * denchiu);
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]
    foncub[2] = f_aux[3]; // Was: foncub[0][2]

    return 1;
}

int SubinflectionTP::curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, std::vector<RealVector> &subinflection_curve) {



    Flux2Comp2PhasesAdimensionalized * fluxAdimensional = (Flux2Comp2PhasesAdimensionalized *) f;
    fluxFunction_ = fluxAdimensional->getHorizontalFlux();

    double phi = a->accumulationParams().component(0);

    cout << "Valor de phi em subinflection: " << phi << endl;


    td = fluxAdimensional->getThermo();

    g.fill_functions_on_grid(f, a);

    g.fill_eigenpairs_on_grid(f, a);


    gv = &g;

    subinflection_curve.clear();

    int info = ContourMethod::contour2d(this, subinflection_curve);

    return info;
}

SubinflectionTP::~SubinflectionTP() {
}
