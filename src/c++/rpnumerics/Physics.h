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
#include "GridValues.h"
#include "Quad2.h"
#include "Quad3.h"
#include "Quad4.h"
#include "Stone.h"
#include "TPCW.h"
#include "PolydispersePhysics.h"
#include "CoreyQuadPhysics.h"
#include "Cub2.h"

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
   
    const Boundary * boundary_;
    string * ID_;
    Space * space_;
    int type_;
    static string rpnHome_;

    
public:

    Physics(const string &);
    Physics(const vector<SubPhysics> &, const Boundary &, const string &);
    Physics(const Physics &);

    virtual ~Physics();

    Physics * clone()const;

    const Boundary & boundary() const;

    void boundary(const Boundary & boundary);

    const Space & domain() const;

    const string & ID() const;

    SubPhysics & getSubPhysics(const int)const ;

    const vector<SubPhysics *> & getPhysicsVector()const;

    static const string & getRPnHome();

    static void setRPnHome(const string &);

    void setParams(vector<string>);
    
    



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
