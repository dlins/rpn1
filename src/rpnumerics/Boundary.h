/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Boundary.h
 **/

#ifndef _Boundary_H
#define	_Boundary_H


//! Definition of class Boundary.
/*!

 TODO:
 NOTE :

 @ingroup rpnumerics
 */


class Boundary{
    
    //! To check if the point is inside the boundary.
    
    virtual bool inside(const RealVector &y) const  =0;
    
    //! Minimums boundary values accessor 
    
    virtual const  RealVector & getMinimums() const  =0;
    
    //! Maximums boundary values accessor

    virtual const   RealVector & getMaximums() const =0;
    
    virtual const  RealVector & intersect(RealVector &y1, RealVector &y2) const =0 ;
};


#endif	/* _Boundary_H */

