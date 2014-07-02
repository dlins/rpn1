#ifndef _COMPOSITECURVETPCW_
#define _COMPOSITECURVETPCW_

#include "CompositeCurve.h"
#include "TPCW_Evap_Extension.h"
#include "TPCW_IntSat_Extension.h"

class CompositeCurveTPCW : public CompositeCurve {
    private:
    protected:
        TPCW_Evap_Extension   *evapextension;
        TPCW_IntSat_Extension *intsatextension;
    public:
        CompositeCurveTPCW(const AccumulationFunction *a, const FluxFunction *f, const Boundary *b, ShockCurve *s, Explicit_Bifurcation_Curves *ebc, TPCW_Evap_Extension *ee, TPCW_IntSat_Extension *ii);
        virtual ~CompositeCurveTPCW();

        int curve(const AccumulationFunction *RarAccum, const FluxFunction *RarFlux,
                  const Boundary *RarBoundary, 
                  const Curve &rarcurve,
//                  std::vector<RealVector> &rarcurve, std::vector<double> &lambda,
                  const RealVector &composite_initial_point,
                  int last_point_in_rarefaction,
                  const ODE_Solver *odesolver,
                  double deltaxi,
                  int where_composite_begins, int fam, 
//                  std::vector<RealVector> &newrarcurve,
//                  std::vector<RealVector> &compositecurve,
                  Curve &new_rarcurve,
                  Curve &compositecurve,
                  RealVector &final_direction,
                  int &reason_why,
                  int &edge);

};

#endif // _COMPOSITECURVETPCW_

