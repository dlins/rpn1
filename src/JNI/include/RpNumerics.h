/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpNumerics.h
 **/

#ifndef _RpNumerics_H
#define	_RpNumerics_H


#include "Physics.h"
#include "FluxFunction.h"
//#include "ODESolver.h"


class RpNumerics {
    
    
private:
    
    static Physics * physics_;
    static WaveFlow * flow_;
    
//    static ODESolver * solver_;
    
    
public:
    
    virtual  ~RpNumerics();
    
    static Physics *  getPhysics();
    static const FluxFunction & getFlux();
    static WaveFlow * getFlow();
    
//  static ODESolver * getODESolver();
    
    static void setPhysics(Physics *);
    static void setFlow(WaveFlow *);
    
    
    
//    static void setODESolver (ODESolver *);
    
    
};



#endif	/* _JNIDEFS_H */

