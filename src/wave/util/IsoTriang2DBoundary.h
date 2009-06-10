/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) IsoTriang2DBoundary.h
 */

#ifndef _IsoTriang2DBoundary_H
#define _IsoTriang2DBoundary_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Boundary.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class IsoTriang2DBoundary:public Boundary{
    
private:
    
    RealVector * minimums_;
    RealVector * maximums_;
    
    RealVector  * A_;
    RealVector  * B_;
    RealVector  * C_;

    const char * type_;
public:
    
    virtual ~IsoTriang2DBoundary();
    
    IsoTriang2DBoundary(const RealVector & A, const RealVector & B, const RealVector & C);
    
    IsoTriang2DBoundary(const IsoTriang2DBoundary &);
    
    bool inside(const RealVector &y) const;
    
    //! Virtual constructor
    Boundary * clone()const;
    
    const  RealVector & minimums() const;
    
    const  RealVector & maximums() const;
    
    RealVector intersect(RealVector &y1, RealVector &y2) const;
    
    
    const RealVector  & getA()const ;
    const RealVector  & getB()const;
    const RealVector  & getC()const;
    const char * boundaryType()const;
    
};


inline bool IsoTriang2DBoundary::inside(const RealVector & U)const {
    double x = U.component(0);
    double y= U.component(1);
    
    if ((x > 0.) && (y > 0.) && (x + y <= 1.))
        return true;
    return false;
    
}

inline const char * IsoTriang2DBoundary::boundaryType()const {return type_;}
inline const RealVector  &  IsoTriang2DBoundary::getA()const {return *A_;}
inline const RealVector  &  IsoTriang2DBoundary::getB()const{return *B_;}
inline const RealVector  &  IsoTriang2DBoundary::getC()const {return *C_;}

inline const  RealVector & IsoTriang2DBoundary::minimums() const {return *minimums_;}

inline const  RealVector & IsoTriang2DBoundary::maximums() const {return *maximums_;}


#endif //! _IsoTriang2DBoundary_H
