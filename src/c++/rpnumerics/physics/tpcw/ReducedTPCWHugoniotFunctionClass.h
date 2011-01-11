#ifndef _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_
#define _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_

#include "ReducedFlux2Comp2PhasesAdimensionalized.h"
#include "ReducedAccum2Comp2PhasesAdimensionalized.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

class ReducedTPCWHugoniotFunctionClass : public HugoniotFunctionClass {
private:
    //        ReducedFlux2Comp2PhasesAdimensionalized_Params  *ReducedTPCWFluxAdimensionalized_Params;
    ReducedFlux2Comp2PhasesAdimensionalized *ReducedTPCWFluxAdimensionalized;

    //        ReducedAccum2Comp2PhasesAdimensionalized_Params *ReducedTPCWAccumAdimensionalized_Params;
    ReducedAccum2Comp2PhasesAdimensionalized *ReducedTPCWAccumAdimensionalized;

    //        Flux2Comp2PhasesAdimensionalized_Params         *TPCWFluxAdimensionalized_Params;
    Flux2Comp2PhasesAdimensionalized *TPCWFluxAdimensionalized;

    //        Accum2Comp2PhasesAdimensionalized_Params        *TPCWAccumAdimensionalized_Params;
    Accum2Comp2PhasesAdimensionalized *TPCWAccumAdimensionalized;

    RealVector Uref;
    double *aref_F;
    double *bref_F;

    int n;

    std::vector<eigenpair> ve_uref;

    bool Uref_is_elliptic;

    double det(int nn, double *A);

protected:
public:
    ReducedTPCWHugoniotFunctionClass(const RealVector &U,
            double abs_perm,
            double phi,
            double const_gravity,
            Thermodynamics_SuperCO2_WaterAdimensionalized *td,
            FracFlow2PhasesHorizontalAdimensionalized *fh); // U=dim 3  , u=dim 2.
    virtual ~ReducedTPCWHugoniotFunctionClass();

//    HugoniotFunctionClass * clone() const;


//    ReducedTPCWHugoniotFunctionClass(const ReducedTPCWHugoniotFunctionClass &);

    double HugoniotFunction(const RealVector &u); // TODO: u tem que ter dimensao 2 e nao 3.



    void CompleteHugoniot(double &darcy_speedplus, const RealVector &uplus); // TODO: u tem que ter dimensao 2 e nao 3.
    void completeCurve(vector<RealVector> & curve);

};

#endif // _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_

