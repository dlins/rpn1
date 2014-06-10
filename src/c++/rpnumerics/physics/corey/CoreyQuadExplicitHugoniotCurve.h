#ifndef _COREYQUADEXPLICITHUGONIOTCURVE_
#define _COREYQUADEXPLICITHUGONIOTCURVE_

#include "CoreyQuadHugoniotCurve.h"
#include "ParametricPlot.h"
#include "Utilities.h"

class CoreyQuadExplicitHugoniotCurve : public CoreyQuadHugoniotCurve {
    private:
    protected:
        static double sign(double v){
            return v > 0.0 ? 1.0 : (v < 0.0 ? -1.0 : 0.0);
        }

        // The three vertices of the triangle. Move this to the Boundary, which is anyway known and specific to this case.
        //
        RealVector pure_W_vertex, pure_O_vertex, pure_G_vertex;

        std::vector<int> side_opposite_vertex;

        double muw, mug, muo;

        // This pointer will take the address of one of the methods below.
        //
        RealVector (*function_to_use)(void *cqeh, double phi);

        static RealVector generic(void *cqeh, double phi);

//        static RealVector G_vertex(void *cqeh, double phi);
//        static RealVector O_vertex(void *cqeh, double phi);
//        static RealVector W_vertex(void *cqeh, double phi);

        static RealVector G_bif(void *cqeh, double phi);
        static RealVector O_bif(void *cqeh, double phi);
        static RealVector W_bif(void *cqeh, double phi);

        static RealVector GO_side(void *cqeh, double phi);
        static RealVector GW_side(void *cqeh, double phi);
        static RealVector WO_side(void *cqeh, double phi);

//        static RealVector umbilic(void *cqeh, double phi);

        RealVector compute_umbilic_point(const RealVector &mu);

    public:
        CoreyQuadExplicitHugoniotCurve(const CoreyQuad *ff, const AccumulationFunction *aa, Stone_Explicit_Bifurcation_Curves *s, const Boundary *b);
        ~CoreyQuadExplicitHugoniotCurve();

        void curve(const ReferencePoint &ref, int type, std::vector<Curve> &c);

        virtual void curve(const ReferencePoint &ref, int type, std::vector<HugoniotPolyLine> &classified_curve){
            CoreyQuadHugoniotCurve::curve(ref, type, classified_curve);

            return;
        }

        // To be used by ParametricPlot.
        //
//        static RealVector f(void *obj, double phi);
        static bool f_asymptote(void *obj, const RealVector &p, const RealVector &q);
};

#endif // _COREYQUADEXPLICITHUGONIOTCURVE_

