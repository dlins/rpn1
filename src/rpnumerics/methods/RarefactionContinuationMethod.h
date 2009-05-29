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

class RarefactionContinuationMethod:public RarefactionMethod {

private:

    ODESolver * solver_;
            
public:
    
    RarefactionContinuationMethod (const ODESolver &);
    
    RarefactionContinuationMethod (const RarefactionContinuationMethod &);
    
    RPnCurve & curve(const RealVector &, int direction);

    void curve(const RealVector &, int direction,vector<RealVector> &);
    
    const ODESolver & getSolver()const;

    RarefactionMethod * clone() const;
    
    virtual ~RarefactionContinuationMethod();
    
    
};
#endif //! _RarefactionMethod_H
