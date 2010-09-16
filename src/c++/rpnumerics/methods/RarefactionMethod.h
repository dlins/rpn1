/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.h
 **/

#ifndef _RarefactionMethod_H
#define _RarefactionMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "RealVector.h"
#include "RarefactionFlow.h"
#include "ODESolver.h"
#include "RPnCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod {
    
        
public:


    virtual void curve(const RealVector &, int direction, vector<RealVector> &) = 0;
    
    virtual RarefactionMethod * clone() const =0;
    
    virtual ~RarefactionMethod();
    
};


#endif //! _RarefactionMethod_H
