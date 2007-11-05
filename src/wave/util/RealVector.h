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
//#include "JetMatrix.h"

class RealVector :public Vector{//, public JetMatrix{
    
public:
    RealVector();
    RealVector(int );
    RealVector(double *);
    RealVector (int,double *);
    bool operator==(const RealVector &);
    
    void setVal(int vindx [], double val);
    double getVal(int vindex []) const;
    

    
};
#endif	/* _RealVector_H */

