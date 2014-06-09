#ifndef _STONEEXPLICITHUGONIOT_
#define _STONEEXPLICITHUGONIOT_

#include "ExplicitHugoniot.h"
#include "StoneFluxFunction.h"
#include "StoneAccumulation.h"
#include "Stone_Explicit_Bifurcation_Curves.h"
#include "ColorCurve.h"

// TODO: This class only works appropiately if the accumulation is trivial.
//       Add a check here or in the Physics to make sure.
//
class StoneExplicitHugoniot : public ExplicitHugoniot {
    private:
    protected:
        static double sign(double v){
            return v > 0.0 ? 1.0 : (v < 0.0 ? -1.0 : 0.0);
        }

        void regularly_sampled_segment(const RealVector &p, const RealVector &q, int n, Curve &curve);

        StoneFluxFunction *f;
        StoneAccumulation *a;
        Stone_Explicit_Bifurcation_Curves *sebf;
        std::vector<int> side_opposite_vertex;

        double muw, mug, muo;

        double expw, expg, expo;
        double expow, expog;
        double cnw, cng, cno;
        double lw, lg;
        double low, log;
        double epsl;

        // This pointer will take the address of one of the methods below.
        //
        RealVector (*function_to_use)(StoneExplicitHugoniot *seh, double phi);

        static RealVector generic(StoneExplicitHugoniot *seh, double phi);

        static RealVector umbilic(StoneExplicitHugoniot *seh, double phi);
        static RealVector G_bif(StoneExplicitHugoniot *seh, double phi);
        static RealVector O_bif(StoneExplicitHugoniot *seh, double phi);
        static RealVector W_bif(StoneExplicitHugoniot *seh, double phi);

        static RealVector GO_side(StoneExplicitHugoniot *seh, double phi);
        static RealVector GW_side(StoneExplicitHugoniot *seh, double phi);
        static RealVector WO_side(StoneExplicitHugoniot *seh, double phi);
    public:
        StoneExplicitHugoniot(StoneFluxFunction *ff, StoneAccumulation *aa,const Boundary *bb, Stone_Explicit_Bifurcation_Curves *s);
        virtual ~StoneExplicitHugoniot();

        void curve(GridValues &, ReferencePoint &, int type, std::vector<HugoniotPolyLine> &, std::vector<RealVector> &);
        RealVector fobj(double phi);
};

#endif // _STONEEXPLICITHUGONIOT_

