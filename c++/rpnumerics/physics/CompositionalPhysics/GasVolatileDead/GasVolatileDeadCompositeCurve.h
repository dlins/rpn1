#ifndef _GASVOLATILEDEADCOMPOSITECURVE_
#define _GASVOLATILEDEADCOMPOSITECURVE_

#include "CompositeCurve.h"
#include "GasVolatileDeadEvaporationExtension.h"

class GasVolatileDeadCompositeCurve: public CompositeCurve {
    private:
    protected:
        GasVolatileDeadEvaporationExtension *evapext;
    public:
        GasVolatileDeadCompositeCurve(GasVolatileDeadEvaporationExtension *e, 
                                      const AccumulationFunction *a, 
                                      const FluxFunction *f, 
                                      const Boundary *b, 
                                      ShockCurve *s, 
                                      Explicit_Bifurcation_Curves *ebc);

        virtual ~GasVolatileDeadCompositeCurve();

        int curve(const AccumulationFunction *RarAccum, const FluxFunction *RarFlux,
                  const Boundary *RarBoundary, 
                  const Curve &rarcurve,
                  const RealVector &composite_initial_point,
                  int last_point_in_rarefaction,
                  const ODE_Solver *odesolver,
                  double deltaxi,
                  void *linobj, double (*linear_function)(void *o, const RealVector &p),
                  int where_composite_begins, int fam, 
                  Curve &new_rarcurve,
                  Curve &compositecurve,
                  RealVector &final_direction,
                  int &reason_why,
                  int &edge);
};

#endif // _GASVOLATILEDEADCOMPOSITECURVE_

