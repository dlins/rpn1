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



class WaveFlow {
    
private:

    FluxFunction * fluxFunction_;
    
public:
    
    WaveFlow(const FluxFunction &);


    virtual int flux(const RealVector &,RealVector &) const =0;
    virtual int fluxDeriv(const RealVector &,JacobianMatrix &)const =0;
    virtual int fluxDeriv2(const RealVector &,HessianMatrix &)const =0;

    virtual ~WaveFlow();
    //! Gets the flux function
    const FluxFunction & fluxFunction()const ;
    void setFluxFunction(const FluxFunction& );
    virtual WaveFlow * clone()const = 0;


    
};

inline const FluxFunction & WaveFlow::fluxFunction()const {return *fluxFunction_;}

inline void WaveFlow::setFluxFunction(const FluxFunction & flux) {

    delete fluxFunction_;
    fluxFunction_ = (FluxFunction *) flux.clone();
    
}

#endif //! _WaveFlow_H
