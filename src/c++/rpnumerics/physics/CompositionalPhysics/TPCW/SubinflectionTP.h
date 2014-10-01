#ifndef _SUBINFLECTIONTP_
#define _SUBINFLECTIONTP_

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Matrix.h"
#include "RealVector.h"

#include "ImplicitFunction.h"
#include "ContourMethod.h"
#include "GridValues.h"

//#include "Thermodynamics_Common.h"
#include "Thermodynamics.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"

class SubinflectionTP : public ImplicitFunction {
private:
//    const Thermodynamics_Common *td;
    const Thermodynamics *td;
    Flux2Comp2PhasesAdimensionalized::FracFlow2PhasesHorizontalAdimensionalized * fluxFunction_;


    void subinflection_function(double & reduced_lambdae, double & numeratorchiu,
            double & denominatorchiu, const RealVector &u);
    
    double phi;
protected:
public:
    // TODO: Note a fluxo introduzido...
    //    SubinflectionTPCW(const Flux2Comp2PhasesAdimensionalized *, const Accum2Comp2PhasesAdimensionalized *);



    SubinflectionTP(double );
    ~SubinflectionTP();

    int function_on_square(double *foncub, int i, int j);

    int curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &g, std::vector<RealVector> &subinflection_curve);

};

#endif // _SUBINFLECTIONTP_

