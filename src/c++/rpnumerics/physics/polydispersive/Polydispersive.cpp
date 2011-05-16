#include "Polydispersive.h"

Polydispersive::Polydispersive(const Polydispersive_Params &param) : FluxFunction(param){
    // Maximum packing concentration
    phimax = param.component(0);

    // Terminal (settling) velocities
    V1inf = param.component(1);
    V2inf = param.component(2);

    // Power velocities
    n1 = param.component(3);
    n2 = param.component(4);
}

Polydispersive * Polydispersive::clone() const {
    return new Polydispersive(*this);
}

Polydispersive::Polydispersive(const Polydispersive & copy): FluxFunction(copy.fluxParams()){
    for (int i = 0; i < copy.fluxParams().params().size(); i++) {
        cout<<copy.fluxParams().params().component(i)<<endl;


    }
      // Maximum packing concentration
    phimax = copy.fluxParams().params().component(0);

    // Terminal (settling) velocities
    V1inf = copy.fluxParams().params().component(1);
    V2inf = copy.fluxParams().params().component(2);

    // Power velocities
    n1 = copy.fluxParams().params().component(3);
    n2 = copy.fluxParams().params().component(4);




}


Polydispersive::~Polydispersive(){
}

int Polydispersive::Slip_jet(double phi1, double phi2, JetMatrix &uj, int degree) const{
    double phi = phi1 + phi2;      // Total concentration

    if (degree >= 0){
        double u1 = ( (phi < phimax) ? V1inf * pow(1.0 - phi, n1 - 1.0) : 0.0 );
        double u2 = ( (phi < phimax) ? V2inf * pow(1.0 - phi, n2 - 1.0) : 0.0 );

        uj(0, u1);
        uj(1, u2);

        if (degree >= 1){
            double du1_dphi = ( (phi < phimax) ? - V1inf * (n1 - 1.0) * pow(1.0 - phi, n1 - 2.0) : 0.0 );
            double du2_dphi = ( (phi < phimax) ? - V2inf * (n2 - 1.0) * pow(1.0 - phi, n2 - 2.0) : 0.0 );

            uj(0, 0, du1_dphi);
            uj(1, 0, du2_dphi);

            if (degree >= 2){
                double d2u1_dphi2 = ( (phi < phimax) ? V1inf * (n1 - 1.0) * (n1 - 2.0) * pow(1.0 - phi, n1 - 3.0) : 0.0 );
                double d2u2_dphi2 = ( (phi < phimax) ? V2inf * (n2 - 1.0) * (n2 - 2.0) * pow(1.0 - phi, n2 - 3.0) : 0.0 );

                uj(0, 0, 0, d2u1_dphi2);
                uj(1, 0, 0, d2u2_dphi2);
            }
            else return -1; // ABORTED_PROCEDURE
        }
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}

int Polydispersive::Settling_jet(double phi1, double phi2, JetMatrix &vj, int degree) const{
    double phi = phi1 + phi2;      // Total concentration

    JetMatrix slipj(2);
    Slip_jet(phi1, phi2, slipj, degree);

    if (degree >= 0){
        double u1 = slipj(0);
        double u2 = slipj(1);

        double v1 = (1.0 - phi1)*u1 - phi2*u2;
        double v2 = (1.0 - phi2)*u2 - phi1*u1;

        vj(0, v1);
        vj(1, v2);

        if (degree >= 1){
            double du1_dphi = slipj(0, 0);
            double du2_dphi = slipj(1, 0);

            double dv1_dphi1 = (1.0 - phi1)*du1_dphi - phi2*du2_dphi - u1;
            double dv1_dphi2 = (1.0 - phi1)*du1_dphi - phi2*du2_dphi - u2;

            double dv2_dphi1 = (1.0 - phi2)*du2_dphi - phi1*du1_dphi - u1;
            double dv2_dphi2 = (1.0 - phi2)*du2_dphi - phi1*du1_dphi - u2;

            vj(0, 0, dv1_dphi1);
            vj(0, 1, dv1_dphi2);

            vj(1, 0, dv2_dphi1);
            vj(1, 1, dv2_dphi2);

            if (degree >= 2){
                double d2u1_dphi2 = slipj(0, 0, 0);
                double d2u2_dphi2 = slipj(1, 0, 0);

                double d2v1_dphi12    = (1.0 - phi1)*d2u1_dphi2 - phi2*d2u2_dphi2 - 2.0*du1_dphi;
                double d2v1_dphi1phi2 = (1.0 - phi1)*d2u1_dphi2 - phi2*d2u2_dphi2;
                double d2v1_dphi2phi1 = d2v1_dphi1phi2;
                double d2v1_dphi22    = (1.0 - phi1)*d2u1_dphi2 - phi2*d2u2_dphi2 - 2.0*du2_dphi;

                double d2v2_dphi12    = (1.0 - phi2)*d2u2_dphi2 - phi1*d2u1_dphi2 - 2.0*du1_dphi;
                double d2v2_dphi1phi2 = (1.0 - phi2)*d2u2_dphi2 - phi1*d2u1_dphi2;
                double d2v2_dphi2phi1 = d2v2_dphi1phi2;
                double d2v2_dphi22    = (1.0 - phi2)*d2u2_dphi2 - phi1*d2u1_dphi2 - 2.0*du2_dphi;

                vj(0, 0, 0, d2v1_dphi12   );
                vj(0, 0, 1, d2v1_dphi1phi2);
                vj(0, 1, 0, d2v1_dphi2phi1);
                vj(0, 1, 1, d2v1_dphi22   );

                vj(1, 0, 0, d2v2_dphi12   );
                vj(1, 0, 1, d2v2_dphi1phi2);
                vj(1, 1, 0, d2v2_dphi2phi1);
                vj(1, 1, 1, d2v2_dphi22   );
            }
            else return -1; // ABORTED_PROCEDURE
        }
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}

int Polydispersive::jet(const WaveState &Phi, JetMatrix &flux, int degree) const{
    double phi1 = Phi(0);
    double phi2 = Phi(1);
    double phi = phi1 + phi2;      // Total concentration



    JetMatrix settlingj(2);
    Settling_jet(phi1, phi2, settlingj, degree);

    if (degree >= 0){
        double v1 = settlingj(0);
        double v2 = settlingj(1);
        double f1 = phi1*v1;
        double f2 = phi2*v2;

        flux(0, f1);
        flux(1, f2);

        if (degree >= 1){
            double dv1_dphi1 = settlingj(0, 0);
            double dv1_dphi2 = settlingj(0, 1);
            double dv2_dphi1 = settlingj(1, 0);
            double dv2_dphi2 = settlingj(1, 1);

            double df1_dphi1 = v1 + phi1*dv1_dphi1;
            double df1_dphi2 = phi1*dv1_dphi2;

            double df2_dphi1 = phi2*dv2_dphi1;
            double df2_dphi2 = v2 + phi2*dv2_dphi2;

            flux(0, 0, df1_dphi1);
            flux(0, 1, df1_dphi2);
            flux(1, 0, df2_dphi1);
            flux(1, 1, df2_dphi2);

            if (degree >= 2){
                double d2v1_dphi12    = settlingj(0, 0, 0);
                double d2v1_dphi1phi2 = settlingj(0, 0, 1);
                double d2v1_dphi22    = settlingj(0, 1, 1);
                double d2v2_dphi12    = settlingj(1, 0, 0);
                double d2v2_dphi1phi2 = settlingj(1, 0, 1);
                double d2v2_dphi22    = settlingj(1, 1, 1);

                double d2f1_dphi12    = 2.0*dv1_dphi1 + phi1*d2v1_dphi12;
                double d2f1_dphi1phi2 = dv1_dphi2 + phi1*d2v1_dphi1phi2;
                double d2f1_dphi2phi1 = d2f1_dphi1phi2;
                double d2f1_dphi22    = phi1*d2v1_dphi22;

                double d2f2_dphi12    = phi2*d2v2_dphi12;
                double d2f2_dphi1phi2 = dv2_dphi1 + phi2*d2v2_dphi1phi2;
                double d2f2_dphi2phi1 = d2f2_dphi1phi2;
                double d2f2_dphi22    = 2.0*dv2_dphi2 + phi2*d2v2_dphi22;

                flux(0, 0, 0, d2f1_dphi12   );
                flux(0, 0, 1, d2f1_dphi1phi2);
                flux(0, 1, 0, d2f1_dphi2phi1);
                flux(0, 1, 1, d2f1_dphi22   );
                flux(1, 0, 0, d2f2_dphi12   );
                flux(1, 0, 1, d2f2_dphi1phi2);
                flux(1, 1, 0, d2f2_dphi2phi1);
                flux(1, 1, 1, d2f2_dphi22   );
            }
            else return -1; // ABORTED_PROCEDURE
        }
    }
    return 2; //SUCCESSFUL_PROCEDURE;
}

