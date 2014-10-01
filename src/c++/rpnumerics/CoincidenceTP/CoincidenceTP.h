#ifndef _COINCIDENCETP_
#define _COINCIDENCETP_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "GridValues.h"

#include <stdio.h>
#include "Thermodynamics.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

class CoincidenceTP : public ImplicitFunction {
private:
    const Thermodynamics *td;
    const Flux2Comp2PhasesAdimensionalized *fluxFunction_;
    double phi;

    double lambdas_function(const RealVector &u);
    double lambdae_function(const RealVector &u);
protected:
public:
// TODO: Note a fluxo introduzido...
//    CoincidenceTPCW(const Flux2Comp2PhasesAdimensionalized *,const Accum2Comp2PhasesAdimensionalized *);


    CoincidenceTP(const Flux2Comp2PhasesAdimensionalized *);
    CoincidenceTP(){gv = 0;}
    ~CoincidenceTP();

    int function_on_square(double *foncub, int i, int j);

    int curve(const FluxFunction *f, const AccumulationFunction *a, 
                  GridValues &g, std::vector<RealVector> &coincidence_curve);

};

#endif // _COINCIDENCETP_

