#ifndef _WAVECURVEFACTORY_
#define _WAVECURVEFACTORY_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Boundary.h"
//#include "ViscousMatrix.h"

//#include "Physics.h"

#include "RarefactionCurve.h"
#include "CompositeCurve.h"
#include "ShockCurve.h"
#include "ODE_Solver.h"
#include "Utilities.h"

#include "WaveCurve.h"
#include "HyperOctree.h"

#define WAVECURVE_OK                    0
#define WAVECURVE_ERROR                 1
#define WAVECURVE_REACHED_BOUNDARY      2
#define WAVECURVEFACTORY_INIT_OK        3
#define WAVECURVEFACTORY_INIT_ERROR     4
#define WAVECURVE_REACHED_INITIAL_POINT 5
#define WAVECURVE_COMPLEX_EIGENVALUE_AT_FAMILY 6
#define WAVECURVE_REACHED_COINCIDENCE_CURVE    7

#define WAVE_CURVE_INTERSECTION_FOUND                           300
#define WAVE_CURVE_INTERSECTION_NOT_FOUND                       400

class WaveCurveSegment {
    private:
    protected:
        static bool point_inside_box(const RealVector &p, const BoxND &box){
            bool test = true;

            int pos = 0;
            while (test && pos < 2){
                test = p(pos) >= box.pmin(pos) && p(pos) <= box.pmax(pos);
                pos++;
            }

            return test;
        }
    public:
        RealVector *rv;

        int curve_position, segment_position;

        WaveCurveSegment(const RealVector &p1, const RealVector &p2, int cp, int sp){
            rv = new RealVector[2];

            rv[0] = p1;
            rv[1] = p2;

            curve_position   = cp;
            segment_position = sp;
        }
        ~WaveCurveSegment(){
            delete [] rv;
        }

        bool intersect(const BoxND &box){
            return point_inside_box(rv[0], box) || point_inside_box(rv[1], box);
        }
};

// THIS CLASS IS FINAL. DO NOT DERIVE FROM IT.
//
class WaveCurveFactory {
    private:
    protected:
        const AccumulationFunction *g;
        const FluxFunction *f;
        const Boundary *b;
        const ODE_Solver *odesolver;

        RarefactionCurve     *rarefactioncurve;
        ShockCurve           *shockcurve;
        CompositeCurve       *compositecurve;
        HugoniotContinuation *hugoniot;

        std::vector<std::string> type;

        int family_for_directional_derivative;
        RealVector reference_for_directional_derivative;

        static bool segment_intersection(double *p1, double *p2, double *q1, double *q2, double *r);
    public:
        WaveCurveFactory(const AccumulationFunction *gg, const FluxFunction *ff, const Boundary *bb, const ODE_Solver *o, RarefactionCurve *r, ShockCurve *s, CompositeCurve *c);
        ~WaveCurveFactory();

        // This half-wavecurve is meaningless along the boundary's side.
        // If necessary, create a new one which does not use initial_direction.
        //
        int Liu_half_wavecurve(const ReferencePoint &ref, 
                               const RealVector &initial_point, 
                               int initial_family, int increase, 
                               int initial_curve, 
                               const RealVector &initial_direction, 
                               WaveCurve &hwc,
                               int &wavecurve_stopped_because, 
                               int &edge);

        int wavecurve(const RealVector &initial_point, int family, int increase, HugoniotContinuation *h, WaveCurve &hwc, 
                      int &wavecurve_stopped_because, int &edge);

        // s = side.
        int wavecurve_from_boundary(const RealVector &initial_point, int s, int family, int increase, HugoniotContinuation *h, WaveCurve &hwc, int &wavecurve_stopped_because, int &edge);

        int wavecurve_from_inflection(const std::vector<RealVector> &inflection_curve, 
                                      const RealVector &p, 
                                      int family, int increase, 
                                      HugoniotContinuation *h, 
                                      WaveCurve &hwc, int &wavecurve_stopped_because, int &edge);

        static double rarefaction_directional_derivative(void *obj, const RealVector &p);

        static int intersection(const WaveCurve &c1, const WaveCurve &c2, const RealVector &pmin, const RealVector &pmax, 
                                RealVector &p, int &subc1, int &subc1_point, int &subc2, int &subc2_point);

        int wavecurve_from_wavecurve(const WaveCurve &c, const RealVector &p, HugoniotContinuation *h, WaveCurve &hwc, int &wavecurve_stopped_because, int &edge);

        void R_regions(HugoniotContinuation *h, const WaveCurve &c, std::vector<WaveCurve> &curves);
};

#endif // _WAVECURVEFACTORY_

