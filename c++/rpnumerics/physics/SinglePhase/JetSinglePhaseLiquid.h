#ifndef _JETSINGLEPHASELIQUID_
#define _JETSINGLEPHASELIQUID_

#include "JetMatrix.h"
#include "MolarDensity.h"

class JetSinglePhaseLiquid {
    private:
    protected:
        double MC, MW; // Molecular weights for CO2 and Water
        MolarDensity *rho_md;

        Parameter *P_parameter;
    public:
//        JetSinglePhaseLiquid(double mc, double mw, double P);
        JetSinglePhaseLiquid(double mc, double mw, Parameter *P);
        ~JetSinglePhaseLiquid();

        int rhoac_jet(const double xc, const double T, int degree, JetMatrix &m);
        int rhoaw_jet(const double xc, const double T, int degree, JetMatrix &m);
};

#endif // _JETSINGLEPHASELIQUID_

