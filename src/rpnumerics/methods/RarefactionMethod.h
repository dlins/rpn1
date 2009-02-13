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
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod {
    
private:
    
    RarefactionFlow * rarefactionFlow_;
    
public:
    
//    RarefactionMethod(const RarefactionFlow &);
    
    virtual struct RarefactionCurve curve(const RealVector &, const ODESolver &)=0;
    
    virtual RarefactionMethod * clone() const =0;
    
    virtual ~RarefactionMethod();
    
    const   RarefactionFlow & getFlow() const;
    
    void setFlow(const RarefactionFlow &);
    
    
    
};

inline  const RarefactionFlow & RarefactionMethod::getFlow() const  {return *rarefactionFlow_;}

inline void RarefactionMethod::setFlow(const RarefactionFlow & flow){
    
//    delete rarefactionFlow_;
//    
//    rarefactionFlow_= (RarefactionFlow *)flow.clone();
}


#endif //! _RarefactionMethod_H
