/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FluxFunction.h
 **/

#ifndef _FluxFunction_H
#define	_FluxFunction_H

#include "RpFunction.h"
#include "FluxParams.h"

//! Definition of class FluxFunction.
/*!
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class FluxFunction: public RpFunction {
    
private:
    FluxParams *params_;
    
public:
   
    /*! Creates a flux function with flux parameters
     * @param params  Flux parameters
     */ 
    
    FluxFunction(const FluxParams & params);
    virtual ~FluxFunction(void);
    
    //! Flux parameters accessor
    const FluxParams & fluxParams(void) const;

    //! Flux parameters mutator 
    //\param params - New flux parameters 
    
    void fluxParams(const FluxParams & params);
    
};




#endif	//! _FluxFunction_H
