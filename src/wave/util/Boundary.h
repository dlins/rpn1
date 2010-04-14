/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Boundary.h
 **/

#ifndef _Boundary_H
#define	_Boundary_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"
#include "mathutil.h"

//!
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */


class Boundary {
    
public:
    
    virtual ~Boundary();
    
    //! Check if the point is inside the boundary.
    virtual bool inside(const RealVector &y) const = 0;
    
    //! Virtual constructor
    virtual Boundary * clone()const =0;
    
    //! Minimums boundary values accessor
    virtual const  RealVector & minimums() const = 0;
    
    //! Maximums boundary values accessor
    virtual const  RealVector & maximums() const = 0;
    
    virtual RealVector intersect(RealVector &y1, RealVector &y2) const = 0;
    //! Returns the boundary type
    virtual const char * boundaryType()const =0;

    virtual int intersection(const RealVector &p, const RealVector &q, RealVector &r);



protected:

    double distance(const RealVector &, const RealVector &);
    double static epsilon;
};




#endif	/* _Boundary_H */

