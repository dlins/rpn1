#ifndef _FOAMSUBPHYSICS_
#define _FOAMSUBPHYSICS_

#include "ThreePhaseFlowSubPhysics.h"
#include "FoamFluxFunction.h"
#include "LSODE.h"
#include "FoamPermeability.h"
#include "FoamViscosity.h"
#include "FoamShockObserver.h"

#include "HugoniotODE.h"

#define FOAMDEBUG
#ifdef FOAMDEBUG
#include "canvas.h"
#include "curve2d.h"
#endif

#ifdef FOAMDEBUG
class FoamSubPhysics : public ThreePhaseFlowSubPhysics, public Observer {
#else
class FoamSubPhysics : public ThreePhaseFlowSubPhysics {
#endif
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

        #ifdef FOAMDEBUG
        Canvas *canvas;
        Curve2D *fm_curve;
        #endif
    public:
        FoamSubPhysics();
        virtual ~FoamSubPhysics();

        virtual bool parameter_consistency();

        Parameter* max_distance_to_contact_region_parameter(){
            return max_distance_to_contact_region_parameter_;
        }

        #ifdef FOAMDEBUG
        void set_canvas(Canvas *c){canvas = c; change(); return;}

        void change(){
            if (fm_curve != 0) canvas->erase(fm_curve);

            RealVector p0(2), p1(2), p2(2);
            p0(0) = fmdry->value();
            p0(1) = 0.0;

            p1(0) = fmdry->value();
            p1(1) = fmoil->value();

            p2(0) = 1.0 - fmoil->value();
            p2(1) = fmoil->value();

            std::vector<RealVector> fmp;
            fmp.push_back(p0);
            fmp.push_back(p1);
            fmp.push_back(p2);

            fm_curve = new Curve2D(fmp, 0.0, 0.0, 0.0);

            canvas->add(fm_curve);
        }
        #endif
};

#endif // _FOAMSUBPHYSICS_

