#ifndef _THREEPHASEFLOWPERMEABILITY_
#define _THREEPHASEFLOWPERMEABILITY_

#include "AuxiliaryFunction.h"

class ThreePhaseFlowSubPhysics;

class ThreePhaseFlowPermeability : public AuxiliaryFunction {
    private:
    protected:
        ThreePhaseFlowSubPhysics *subphysics_;
    public:
        ThreePhaseFlowPermeability(ThreePhaseFlowSubPhysics *s);
        virtual ~ThreePhaseFlowPermeability();

        virtual int PermeabilityWater_jet(const RealVector &state, int degree, JetMatrix &water) = 0;
        virtual int PermeabilityGas_jet(const RealVector &state, int degree, JetMatrix &gas) = 0;
        virtual int PermeabilityOil_jet(const RealVector &state, int degree, JetMatrix &oil) = 0;

        virtual void reduced_permeability(const RealVector &state, RealVector &rp) = 0;
};

#endif // _THREEPHASEFLOWPERMEABILITY_

