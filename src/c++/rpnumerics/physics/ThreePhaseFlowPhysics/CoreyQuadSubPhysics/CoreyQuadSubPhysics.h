#ifndef _COREYQUADSUBPHYSICS_
#define _COREYQUADSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "CoreyQuad.h"
#include "LSODE.h"
#include "CoreyQuadExplicitHugoniotCurve.h"
#include "CoreyQuadPermeability.h"

class CoreyQuadSubPhysics : public ThreePhaseFlowSubPhysics {
    private:
    protected:
//        Parameter *grw_parameter_, *grg_parameter_, *gro_parameter_;
//        Parameter *muw_parameter_, *mug_parameter_, *muo_parameter_;
//        Parameter *vel_parameter_;
    public:
        CoreyQuadSubPhysics();
        virtual ~CoreyQuadSubPhysics();

//        Parameter* muw_parameter(){return muw_parameter_;}
//        Parameter* muo_parameter(){return muo_parameter_;}
//        Parameter* mug_parameter(){return mug_parameter_;}
};

#endif // _COREYQUADSUBPHYSICS_

