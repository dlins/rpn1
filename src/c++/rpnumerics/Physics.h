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
#include "SubPhysics.h"

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
    vector<SubPhysics *> * physicsVector_;
    Boundary * boundary_;

public:

    Physics(const vector<SubPhysics> &,const Boundary &,const char *);
    Physics(const Physics &);

    virtual ~Physics();

    virtual Physics * clone()const = 0;

    const Boundary & boundary() const;

    void boundary(const Boundary & boundary);

    const Space & domain() const;

    virtual const char * ID() const = 0;

    const SubPhysics & getSubPhysics(const int);

    const vector<SubPhysics *> & getPhysicsVector()const;

    //deprecated
    const FluxFunction & fluxFunction() const;

    //deprecated

    const AccumulationFunction & accumulation() const;
    //deprecated

    void fluxParams(const FluxParams &);
    //deprecated

    void accumulationParams(const AccumulationParams &);
    //deprecated
    const AccumulationParams & accumulationParams()const;


};



#endif	//! _Physics_H
