/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RealMatrix2.h
 */

#ifndef _RealMatrix2_H
#define _RealMatrix2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Vector.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
//#define RP_... 1

extern"C" {
    void dgeev_(char * , char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);
}




class RealMatrix2 {
    
private:
    
    Vector data_;
    int row_, col_;
    
    
    class RangeViolation : public exception { };
    
public:
    
    RealMatrix2(int , int );
    RealMatrix2();
    RealMatrix2(const RealMatrix2 &);
    
    void range_check(int i, int  j) const;
    
    double operator ()(int i,int j) const ;
    
    void operator ()(int i , int j , double value);
    
    void fillEigenData(int stateSpaceDim, RealMatrix2 & df, double & eigenValR, double & eigenValI, RealVector & eigenVec);
    
    
};




#endif //! _RealMatrix2_H
