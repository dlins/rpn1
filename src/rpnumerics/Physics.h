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
#include "AccumulationFunction.h"
#include "Boundary.h"
#include "FluxFunction.h"
#include "Space.h"


//!
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

class Physics {
    
public :
    virtual ~Physics();
    
    virtual Physics * clone()const =0;
    
    virtual const AccumulationFunction & accumulation() const = 0;
    
    virtual const Boundary & boundary() const= 0;
    
    virtual void boundary(const Boundary & boundary) = 0;
    
    virtual const FluxFunction & fluxFunction() const = 0;
    
    virtual const Space & domain() const = 0;
    

    
};



#endif	//! _Physics_H
