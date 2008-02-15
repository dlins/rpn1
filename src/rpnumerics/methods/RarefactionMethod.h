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

#include <vector>
#include "RealVector.h"
#include "RarefactionFlow.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod {
    
private:
    
    RarefactionFlow * rarefactionFlow_;
    
public:
    RarefactionMethod(const RarefactionFlow &);
    
    virtual vector<RealVector> curve(const RealVector &, int ) =0 ;
    
    virtual struct RarefactionCurve plot(const RealVector &,int)=0;
    
    virtual RarefactionMethod * clone() const =0;
    
    virtual ~RarefactionMethod();
    
    const RarefactionFlow & getFlow()const ;
    

    
    
};

inline const RarefactionFlow & RarefactionMethod::getFlow() const {return *rarefactionFlow_;}


#endif //! _RarefactionMethod_H
