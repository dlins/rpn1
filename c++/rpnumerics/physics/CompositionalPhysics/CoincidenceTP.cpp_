#include "CoincidenceTP.h"

CoincidenceTP::CoincidenceTP(const Flux2Comp2PhasesAdimensionalized *f) : fluxFunction_(f), td(f->getThermo()) {

}

double CoincidenceTP::lambdas_function(const RealVector &u) {
    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);

    fluxFunction_->getHorizontalFlux()->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 1, m);

    return m.get(0, 0);
}

double CoincidenceTP::lambdae_function(const RealVector &u) {

    // First we define the Buckley-Leverett jet.

    double sw = 1.0 - u.component(0);
    double Theta = u.component(1);
    JetMatrix m(2);


    fluxFunction_->getHorizontalFlux()->Diff_FracFlow2PhasesHorizontalAdimensionalized(sw, Theta, 0, m);

    double f = m.get(0);
    double s = u.component(0);

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

    double M = (drho3_dT * rho1 - rho3 * drho1_dT)*(rho1 * rhoaw - rho2 * rhoac) - (drho2_dT * rho1 - rho2 * drho1_dT)*(rho1 * Ha - rho3 * rhoac);
    double N1 = (dHa_dT * rho1 - rho3 * drhoac_dT)*(rho1 * rhoaw - rho2 * rhoac) - (rho1 * drhoaw_dT - rho2 * drhoac_dT)*(rho1 * Ha - rho3 * rhoac);
    double N2 = N1 + (dHr_dT / phi) * rho1 * (rho1 * rhoaw - rho2 * rhoac);

    double reduced_lambdae = (f * M + N1) / (s * M + N2);
    return reduced_lambdae;
}

int CoincidenceTP::function_on_square(double *foncub, int i, int j) {
    double f_aux[4];

    for (int l = 0; l < 2; l++) {
        for (int k = 0; k < 2; k++) {
            RealVector u(2);
            u(0) = gv->grid(i + l, j + k).component(0);
            u(1) = gv->grid(i + l, j + k).component(1);
            //            double lambdas = lambdas_function(RealVector(3,gv->grid(i + l, j + k).components()));
            //            double lambdae = lambdae_function(RealVector(3,gv->grid(i + l, j + k).components()));
            double lambdas = lambdas_function(u);
            double lambdae = lambdae_function(u);
            f_aux[l * 2 + k] = lambdas - lambdae;
        }
    }

    foncub[1] = f_aux[0]; // Was: foncub[0][1]
    foncub[0] = f_aux[2]; // Was: foncub[0][0]
    foncub[3] = f_aux[1]; // Was: foncub[0][2]
    foncub[2] = f_aux[3]; // Was: foncub[0][2]

 
    return 1;
}

int CoincidenceTP::curve(const FluxFunction *f, const AccumulationFunction *a,
        GridValues &g, std::vector<RealVector> &coincidence_curve) {

    phi = a->accumulationParams().component(0);

    g.fill_functions_on_grid(f, a);
    gv = &g;

    coincidence_curve.clear();

    int info = ContourMethod::contour2d(this, coincidence_curve);

    return info;
}

CoincidenceTP::~CoincidenceTP() {
}
