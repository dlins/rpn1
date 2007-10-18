/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Physics.h
 **/

#ifndef _Physics_H
#define	_Physics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Boundary.h"
#include "PhaseSpace.h"
#include "AccumulationFunction.h"
#include "WaveFlow.h"
#include "FluxFunction.h"

//! Definition of class Physics.
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class Physics {
    
public :
    
    virtual const AccumulationFunction & accumulation() const = 0;
    
    virtual const FluxFunction & fluxFunction() const = 0;
    
    virtual const WaveFlow & flow() const = 0;
    
    virtual const PhaseSpace & domain() const = 0;
    
    virtual const Boundary & boundary() const= 0;
    
    virtual void boundary(const Boundary & boundary) = 0;
    
};


#endif	//! _Physics_H
