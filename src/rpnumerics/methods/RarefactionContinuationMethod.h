/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.h
 **/

#ifndef _RarefactionContinuationMethod_H
#define _RarefactionContinuationMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RarefactionMethod.h"
#include "ContinuationRarefactionFlow.h"
#include "RPnCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionContinuationMethod : public RarefactionMethod {
private:

    ODESolver * solver_;
    Boundary * boundary_;
    RealVector * referenceVector_;
    int family_;


    int init(ContinuationRarefactionFlow * flow, int n, double *in, int indx, int increase, double deltaxi, double *lambda, double *rev)const;

public:

    RarefactionContinuationMethod(const ODESolver &);

    RarefactionContinuationMethod(const ODESolver &,const Boundary &,int);


    RarefactionContinuationMethod(const RarefactionContinuationMethod &);

    void curve(const RealVector &, int direction, vector<RealVector> &);

    const ODESolver & getSolver()const;

    RarefactionMethod * clone() const;

    const RealVector & getReferenceVector();

    void setReferenceVector(const RealVector &);

    virtual ~RarefactionContinuationMethod();


};
#endif //! _RarefactionMethod_H
