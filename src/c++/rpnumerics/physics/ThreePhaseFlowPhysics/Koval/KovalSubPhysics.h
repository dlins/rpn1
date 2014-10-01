#ifndef _KOVALSUBPHYSICS_
#define _KOVALSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "LSODE.h"
#include "KovalPermeability.h"

#include "KovalFluxFunction.h"
#include "KovalAccumulationFunction.h"

class KovalSubPhysics : public ThreePhaseFlowSubPhysics {
    private:
    protected:
        Parameter *phi_parameter;
    public:
        KovalSubPhysics();
        virtual ~KovalSubPhysics();
};

#endif // _KOVALSUBPHYSICS_

