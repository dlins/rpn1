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
    
    FluxFunction(const FluxParams & params);
    virtual ~FluxFunction(void);
    FluxParams & fluxParams(void) const;
    void fluxParams(const FluxParams & params);
    
};




#endif	//! _FluxFunction_H
