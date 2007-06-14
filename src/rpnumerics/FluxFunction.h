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

//! Definition of class FluxFunction.
/*!
 TODO:
 NOTE :

 @ingroup rpnumerics
 */


class FluxFunction: public RpFunctionDeriv2 {
    
    public:
        //! FluxParams accessor 
        virtual const FluxParams &fluxParams() const = 0;
        //! FluxParams mutator
        virtual void fluxParams(const FluxParams &params) = 0;
        
};


#endif	/* _FluxFunction_H */



