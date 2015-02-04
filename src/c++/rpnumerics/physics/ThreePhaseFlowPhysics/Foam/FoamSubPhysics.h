#ifndef _FOAMSUBPHYSICS_
#define _FOAMSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "FoamFluxFunction.h"
#include "LSODE.h"
#include "FoamPermeability.h"
#include "FoamViscosity.h"
#include "FoamShockObserver.h"

#include "HugoniotODE.h"

class FoamSubPhysics : public ThreePhaseFlowSubPhysics {
    private:
    protected:
        Parameter *nw_parameter,  *no_parameter,  *ng_parameter;

//        Parameter *mug0;
        Parameter *epdry, *fdry, *foil, *fmdry, *fmmob, *fmoil, *floil, *epoil;

        // TEMPORARY HACK, KILL IT LATER!
        Parameter *fdry_switch, *fo_switch;

        // Most probably this will mutate into a feature of all subphyiscs: it seems to be really useful.
        // Some changes are due, the most important one is deciding what the name of this class
        // should be. It would be, as far as I can see, simply a duplicate of Parameter.
        //
        FoamShockObserver *fso;
        Parameter *max_distance_to_contact_region_parameter_;
    public:
        FoamSubPhysics();
        virtual ~FoamSubPhysics();

        virtual bool parameter_consistency();

        Parameter* max_distance_to_contact_region_parameter(){
            return max_distance_to_contact_region_parameter_;
        }
};

#endif // _FOAMSUBPHYSICS_

