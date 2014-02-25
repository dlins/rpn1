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

#include "WaveCurve.h"

#define WAVECURVE_OK                    0
#define WAVECURVE_ERROR                 1
#define WAVECURVE_REACHED_BOUNDARY      2
#define WAVECURVEFACTORY_INIT_OK        3
#define WAVECURVEFACTORY_INIT_ERROR     4
#define WAVECURVE_REACHED_INITIAL_POINT 5
#define WAVECURVE_COMPLEX_EIGENVALUE_AT_FAMILY 6
#define WAVECURVE_REACHED_COINCIDENCE_CURVE    7

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

        int wavecurve(const RealVector &initial_point, int family, int increase, HugoniotContinuation *h, WaveCurve &hwc, int &wavecurve_stopped_because, int &edge);

        // s = side.
        int wavecurve_from_boundary(const RealVector &initial_point, int s, int family, int increase, HugoniotContinuation *h, WaveCurve &hwc, int &wavecurve_stopped_because, int &edge);
};

#endif // _WAVECURVEFACTORY_

