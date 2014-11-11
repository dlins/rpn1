#ifndef _THREEPHASEFLOWPERMEABILITYLEVELCURVE_
#define _THREEPHASEFLOWPERMEABILITYLEVELCURVE_

class ThreePhaseFlowSubPhysics;

#include "ImplicitFunction.h"
#include "Curve.h"

#define WATER_PERMEABILITY_CURVE 0
#define OIL_PERMEABILITY_CURVE   1
#define GAS_PERMEABILITY_CURVE   2

class ThreePhaseFlowPermeabilityLevelCurve: public ImplicitFunction {
    private:
    protected:
        ThreePhaseFlowSubPhysics *subphysics_;

        static double water_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p);
        static double oil_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p);
        static double gas_permeability(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p);

        double (*permeabilityfunction)(ThreePhaseFlowPermeabilityLevelCurve *obj, const RealVector &p);
        double level;
    public:
        ThreePhaseFlowPermeabilityLevelCurve(ThreePhaseFlowSubPhysics *s);
        virtual ~ThreePhaseFlowPermeabilityLevelCurve();

        virtual int function_on_square(double *foncub, int i, int j);
        void curve(const RealVector &ref, int type, std::vector<RealVector> &c);
};

#endif // _THREEPHASEFLOWPERMEABILITYLEVELCURVE_

