#ifndef _COREYQUADSUBPHYSICS_
#define _COREYQUADSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "CoreyQuadFluxFunction.h"
#include "LSODE.h"
#include "CoreyQuadExplicitHugoniotCurve.h"
#include "CoreyQuadPermeability.h"
#include "CoreyQuadTransitionalLine.h"

class CoreyQuadSubPhysics : public ThreePhaseFlowSubPhysics {
    private:
    protected:
    public:
        CoreyQuadSubPhysics();
        virtual ~CoreyQuadSubPhysics();
};

#endif // _COREYQUADSUBPHYSICS_

