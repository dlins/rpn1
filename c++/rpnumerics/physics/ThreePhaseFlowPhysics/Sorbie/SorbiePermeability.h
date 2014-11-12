#ifndef _SORBIEPERMEABILITY_
#define _SORBIEPERMEABILITY_

#include "ThreePhaseFlowPermeability.h"

class SorbieSubPhysics;

class SorbiePermeability: public ThreePhaseFlowPermeability {
    private:
    protected:
    public:
        SorbiePermeability(ThreePhaseFlowSubPhysics *s);
        virtual ~SorbiePermeability();

        inline int PermeabilityWater_jet(const RealVector &state, int degree, JetMatrix &water);
        inline int PermeabilityOil_jet(const RealVector &state, int degree, JetMatrix &water);
        inline int PermeabilityGas_jet(const RealVector &state, int degree, JetMatrix &water);

        inline void reduced_permeability(const RealVector &state, RealVector &rp);
};

void SorbiePermeability::reduced_permeability(const RealVector &state, RealVector &rp){
    double sw = state(0);
    double so = state(1);
    double sg = 1.0 - sw - so;

    rp.resize(3);

    // Water.
    //
    rp(0) = sw;

    // Oil.
    //
    rp(1) = so;

    // Gas.
    //
    rp(2) = sg;

    return;
}

#endif // _SORBIEPERMEABILITY_

