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
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionContinuationMethod:public RarefactionMethod {

private:

    ODESolver * solver_;
    ODESolution *solution_;
            
public:
    
    RarefactionContinuationMethod (const ODESolver &);
    
    RarefactionContinuationMethod (const RarefactionContinuationMethod &);
    
    void curve(const RealVector &, int direction);
    
    vector <RealVector> getCoords() const;
    
    const ODESolver & getSolver()const;

    RarefactionMethod * clone() const;
    
    virtual ~RarefactionContinuationMethod();
    
    
};
#endif //! _RarefactionMethod_H
