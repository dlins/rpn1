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
#include <iostream>
#include <vector>
#include "GridValues.h"

#define BOUNDARY_INTERSECTION_BOTH_INSIDE    1
#define BOUNDARY_INTERSECTION_FOUND          0
#define BOUNDARY_INTERSECTION_BOTH_OUTSIDE (-1)

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

    virtual bool inside(const double*) const = 0;

    //! Virtual constructor
    virtual Boundary * clone()const = 0;

    //! Minimums boundary values accessor
    virtual const RealVector & minimums() const = 0;

    //! Maximums boundary values accessor
    virtual const RealVector & maximums() const = 0;

    virtual RealVector intersect(RealVector &y1, RealVector &y2) const = 0;
    //! Returns the boundary type
    virtual const char * boundaryType()const = 0;

    virtual int intersection(const RealVector &p, const RealVector &q, RealVector &r, int &)const;

    // Returns the physical boundary. Derived classes may be non-regular.
    // TODO: SHOULD BE PURE VIRTUAL! DERIVED CLASSES MUST IMPLEMENT THIS METHOD!
    virtual void physical_boundary(std::vector<RealVector> &){return;}
    
    
    
    
   virtual void extension_curve(const FluxFunction *f, const AccumulationFunction *a,
           GridValues &gv,
           int where_constant, int number_of_steps, bool singular,
           int fam, int characteristic,
           std::vector<RealVector> &c, std::vector<RealVector> &d){return;}

   virtual void extension_curve(const FluxFunction *df, const AccumulationFunction *da,
                                const FluxFunction *cf, const AccumulationFunction *ca,
           GridValues &gv,
           int where_constant, int number_of_steps, bool singular,
           int fam, int characteristic,
           std::vector<RealVector> &c, std::vector<RealVector> &d){return;}


   virtual void envelope_curve(const FluxFunction *f, const AccumulationFunction *a,
            GridValues &gv,
            int where_constant, int number_of_steps, bool singular,
            std::vector<RealVector> &c, std::vector<RealVector> &d){return;}

    // Return a vector originating in p that points to the interior of the domain from 
    // the Boundary side with index s.
    //
//    virtual RealVector side_transverse_interior(const RealVector &p, int s) const {
//        return RealVector(2);
//    }

    virtual RealVector side_transverse_interior(const RealVector &p, int s) const = 0;

    double max_distance() const {
        return norm(maximums() - minimums());
    }

protected:
    double epsilon;
};

#endif	/* _Boundary_H */

