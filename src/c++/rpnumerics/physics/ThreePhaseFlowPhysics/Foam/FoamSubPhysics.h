#ifndef _FOAMSUBPHYSICS_
#define _FOAMSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "FoamFluxFunction.h"
#include "LSODE.h"
#include "FoamPermeability.h"
#include "FoamViscosity.h"

class FoamSubPhysics : public ThreePhaseFlowSubPhysics {
    private:
    protected:
        Parameter *cnw_parameter, *cno_parameter, *cng_parameter;
        Parameter *nw_parameter,  *no_parameter,  *ng_parameter;

//        Parameter *mug0;
        Parameter *epdry, *fdry, *foil, *fmdry, *fmmob, *fmoil, *floil, *epoil;

        // TEMPORARY HACK, KILL IT LATER!
        Parameter *fdry_switch, *fo_switch;
    public:
        FoamSubPhysics();
        virtual ~FoamSubPhysics();

        virtual bool parameter_consistency();
};

#endif // _FOAMSUBPHYSICS_

