#include "Vector.h"

#ifndef _RealVector_H
#define	_RealVector_H

class RealVector :public Vector{
    
    public:
        RealVector();
        RealVector (int );
        RealVector (double *);
        bool operator==(const RealVector &);
    
    
    
};
#endif	/* _RealVector_H */

