#ifndef _THREEPHASEFLOWWAVECURVEFACTORY_
#define _THREEPHASEFLOWWAVECURVEFACTORY_

#include "WaveCurveFactory.h"
#include "ThreePhaseFlowSubPhysics.h"

#define THREEPHASEFLOWWAVECURVEFACTORY_GENERIC_POINT WAVECURVEFACTORY_GENERIC_POINT

#define THREEPHASEFLOWWAVECURVEFACTORY_O_TO_B        1001
#define THREEPHASEFLOWWAVECURVEFACTORY_O_TO_W        1002
#define THREEPHASEFLOWWAVECURVEFACTORY_O_TO_G        1003

#define THREEPHASEFLOWWAVECURVEFACTORY_W_TO_E        1004
#define THREEPHASEFLOWWAVECURVEFACTORY_W_TO_G        1005
#define THREEPHASEFLOWWAVECURVEFACTORY_W_TO_O        1006

#define THREEPHASEFLOWWAVECURVEFACTORY_G_TO_D        1007
#define THREEPHASEFLOWWAVECURVEFACTORY_G_TO_W        1008
#define THREEPHASEFLOWWAVECURVEFACTORY_G_TO_O        1009

#define THREEPHASEFLOWWAVECURVEFACTORY_GW_SIDE       1010
#define THREEPHASEFLOWWAVECURVEFACTORY_GO_SIDE       1011
#define THREEPHASEFLOWWAVECURVEFACTORY_WO_SIDE       1012

#define THREEPHASEFLOWWAVECURVEFACTORY_INVALID_PARAMETERS WAVECURVEFACTORY_INVALID_PARAMETERS

class ThreePhaseFlowWaveCurveFactory: public WaveCurveFactory {
    private:
    protected:
        ThreePhaseFlowSubPhysics *coreyquadsubphysics_;
    public:
        ThreePhaseFlowWaveCurveFactory(const FluxFunction *ff, const AccumulationFunction *gg, 
                                       const Boundary *bb, const ODE_Solver *o, 
                                       RarefactionCurve *r, ShockCurve *s, CompositeCurve *c, 
                                       ThreePhaseFlowSubPhysics *tpfsp);
        virtual ~ThreePhaseFlowWaveCurveFactory();

        virtual int wavecurve(int type, const RealVector &initial_point, int family, int increase, HugoniotContinuation *h, WaveCurve &hwc, 
                              int &wavecurve_stopped_because, int &edge){

            return wavecurve(type, initial_point, family, increase, h, 
                             0, 0, 
                             hwc, 
                             wavecurve_stopped_because, edge);

        }

        virtual int wavecurve(int type, const RealVector &initial_point, int family, int increase, HugoniotContinuation *h, 
                              void *linobj, double (*linear_function)(void *o, const RealVector &p),
                              WaveCurve &hwc, 
                              int &wavecurve_stopped_because, int &edge);

        void list_of_initial_points(std::vector<int> &type, std::vector<std::string> &name) const;
};

#endif // _THREEPHASEFLOWWAVECURVEFACTORY_

