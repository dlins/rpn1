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

private:
    FluxFunction * fluxVector_;


public :


    virtual ~Physics();
    
    virtual Physics * clone()const =0;
    
    virtual const AccumulationFunction & accumulation() const = 0;
    
    virtual const Boundary & boundary() const= 0;
    
    virtual void boundary(const Boundary & boundary) = 0;
    
    const FluxFunction & fluxFunction() const;

    
    virtual void fluxParams(const FluxParams &) =0;

    virtual void accumulationParams(const AccumulationParams &) = 0;
    
    virtual const Space & domain() const = 0;
    
    virtual const char * ID() const =0;


    Physics();
    Physics(vector<FluxFunction>);
    Physics(const Physics &);


    
};



#endif	//! _Physics_H
