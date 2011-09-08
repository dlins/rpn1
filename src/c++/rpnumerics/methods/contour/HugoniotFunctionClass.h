#ifndef _HUGONIOTFUNCTIONCLASS_
#define _HUGONIOTFUNCTIONCLASS_

#include "RealVector.h"
#include <vector>
#include "FluxFunction.h"

class HugoniotFunctionClass {
private:
    RealVector * uRef_;
    const FluxFunction *fluxFunction_; //TODO Colocar a referencia a funcao de fluxo para que o calculo da curva mude
    // quando os parametros de fluxo mudarem
protected:
public:

    HugoniotFunctionClass();
    HugoniotFunctionClass(const FluxFunction &);
    //    HugoniotFunctionClass(FluxFunction *);
    virtual double HugoniotFunction(const RealVector &u) = 0;
    virtual void completeCurve(std::vector<RealVector> &);
    RealVector & getReferenceVector();
    virtual void setReferenceVector(const RealVector &);
    const FluxFunction & getFluxFunction()const;
    void setFluxFunction(FluxFunction *);

    virtual ~HugoniotFunctionClass();

};

#endif // _HUGONIOTFUNCTIONCLASS_

