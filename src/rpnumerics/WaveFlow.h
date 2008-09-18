/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveFlow.h
 */

#ifndef _WaveFlow_H
#define _WaveFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpFunction.h"
#include "FluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



/*! Definition of class WaveFlow.
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */



class WaveFlow :public RpFunction{
    
private:

    FluxFunction * fluxFunction_;
    
public:
    
    WaveFlow(const FluxFunction &);

    virtual ~WaveFlow();
    //! Gets the flux function
    const FluxFunction & fluxFunction()const ;


    
};

inline const FluxFunction & WaveFlow::fluxFunction()const {return *fluxFunction_;}

#endif //! _WaveFlow_H
