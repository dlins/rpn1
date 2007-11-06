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

class RealVector :public Vector {
    
public:
    RealVector(void);
    RealVector(int);
    RealVector(double *);
    RealVector(int,double *);
    bool operator==(const RealVector &);
    
    // No need for accessor and mutator methods! Use 'Vector' methods to access components.
    //void setVal(int vindx [], double val);
    //double getVal(int vindex []) const;
    

    
};
#endif	/* _RealVector_H */

