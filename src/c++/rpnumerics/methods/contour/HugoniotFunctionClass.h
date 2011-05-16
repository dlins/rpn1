#ifndef _HUGONIOTFUNCTIONCLASS_
#define _HUGONIOTFUNCTIONCLASS_

#include "RealVector.h"
#include <vector>
#include "FluxFunction.h"
class HugoniotFunctionClass {
private:
    RealVector * uRef_;
    FluxFunction *fluxFunction_; //TODO Colocar a referencia a funcao de fluxo para que o calculo da curva mude
    // quando os parametros de fluxo mudarem
protected:
public:

    HugoniotFunctionClass();
    HugoniotFunctionClass(const FluxFunction &);
    virtual double HugoniotFunction(const RealVector &u) = 0;
    virtual void completeCurve(std::vector<RealVector> &);
    RealVector & getReferenceVector();
    virtual void setReferenceVector(const RealVector &);
    const FluxFunction & getFluxFunction()const;
    void setFluxFunction(FluxFunction *);

    virtual ~HugoniotFunctionClass();
//    virtual HugoniotFunctionClass * clone()const = 0;
};

#endif // _HUGONIOTFUNCTIONCLASS_

