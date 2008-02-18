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


class WaveFlow :public RpFunction{
    
private:

    FluxFunction * fluxFunction_;
    
public:
    
    WaveFlow(const FluxFunction &);
    virtual ~WaveFlow();
    
    const FluxFunction & flux()const ;
    void setFlux(const FluxFunction &);
    
};

inline const FluxFunction & WaveFlow::flux()const {return *fluxFunction_;}

#endif //! _WaveFlow_H
