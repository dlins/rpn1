/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RealVector.h
 **/


#ifndef _RealVector_H
#define	_RealVector_H

#include "Vector.h"

class RealVector :public Vector{
    
    public:
        RealVector();
        RealVector (int );
        RealVector (double *);
        bool operator==(const RealVector &);
    
};
#endif	/* _RealVector_H */

