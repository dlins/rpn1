#ifndef _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_
#define _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_


#include "Flux2Comp2PhasesAdimensionalized.h"
#include "Accum2Comp2PhasesAdimensionalized.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

class ReducedTPCWHugoniotFunctionClass : public HugoniotFunctionClass {
private:
 
    Accum2Comp2PhasesAdimensionalized *TPCWAccumAdimensionalized;
    Flux2Comp2PhasesAdimensionalized * TPCWFluxAdimensionalized;

    RealVector Uref;
    double *aref_F;
    double *bref_F;

    int n;

    std::vector<eigenpair> ve_uref;

    bool Uref_is_elliptic;

    double det(int nn, double *A);

    HugoniotFunctionClass * clone()const;

protected:
public:
//    ReducedTPCWHugoniotFunctionClass(const RealVector &U,
//            double abs_perm,
//            double phi,
//            double const_gravity,
//            Thermodynamics_SuperCO2_WaterAdimensionalized *td,
//            FracFlow2PhasesHorizontalAdimensionalized *fh); // U=dim 3  , u=dim 2.
    virtual ~ReducedTPCWHugoniotFunctionClass();

    ReducedTPCWHugoniotFunctionClass(const RealVector &, Flux2Comp2PhasesAdimensionalized *, Accum2Comp2PhasesAdimensionalized *);

    ReducedTPCWHugoniotFunctionClass(const ReducedTPCWHugoniotFunctionClass &);

    void setReferenceVector(const RealVector & refVec) ;

    double HugoniotFunction(const RealVector &u); // TODO: u tem que ter dimensao 2 e nao 3.

    void CompleteHugoniot(double &darcy_speedplus, const RealVector &uplus); // TODO: u tem que ter dimensao 2 e nao 3.
    void completeCurve(vector<RealVector> & curve);

};

#endif // _REDUCEDTPCWHUGONIOTFUNCTIONCLASS_

