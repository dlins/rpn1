#ifndef _JETSINGLEPHASEVAPOR_
#define _JETSINGLEPHASEVAPOR_

#include "JetMatrix.h"
#include "MolarDensity.h"

class JetSinglePhaseVapor {
    private:
    protected:
        double MC, MW; // Molecular weights for CO2 and Water
        MolarDensity *rho_md;

        Parameter *P_parameter;
    public:
//        JetSinglePhaseVapor(double mc, double mw, double P);
        JetSinglePhaseVapor(double mc, double mw, Parameter *P);
        ~JetSinglePhaseVapor();

        int rhosigmac_jet(const double yw, const double T, int degree, JetMatrix &m);
        int rhosigmaw_jet(const double yw, const double T, int degree, JetMatrix &m);
};

#endif // _JETSINGLEPHASEVAPOR_

