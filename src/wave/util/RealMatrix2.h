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

//extern"C" {
//    void dgeev_(char * , char *, int *, double *, int*, double *, double *,
//            double *, int *, double *, int *, double *, int *,
//            int *);
//}




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


inline void RealMatrix2::range_check(int i, int j) const {
    if ( ((i < 0) && (i> row_)) || ((j < 0) && (j > col_))  )
        THROW(RealMatrix2::RangeViolation());
}



inline RealMatrix2::RealMatrix2(int row, int col):data_(row*col),row_(row), col_(col){}


inline RealMatrix2::RealMatrix2():data_(2), row_(2), col_(2){}



inline RealMatrix2::RealMatrix2(const RealMatrix2 & copy):data_(copy.data_){}


double RealMatrix2::operator()(int i, int j) const {
    
    
    range_check(i, j);
    
    return data_.component((row_) + (i*col_ + j));
    
    
    
}

void RealMatrix2::operator ()(int i, int j , double value){
    range_check(i, j);
    
    
    data_.component((row_) + (i*col_ + j))=value;
    
    
    
    
}


void RealMatrix2::fillEigenData(int stateSpaceDim, RealMatrix2 &df, double & eigenValR, double & eigenValI, RealVector & eigenVec){
    
    
    int i, j,info,lwork= 5*stateSpaceDim;
    
    double J[stateSpaceDim][stateSpaceDim], vl[stateSpaceDim][stateSpaceDim], vr[stateSpaceDim][stateSpaceDim];
    
//    double vr[stateSpaceDim][stateSpaceDim],vl[stateSpaceDim][stateSpaceDim];
    
    double work[stateSpaceDim];
    
    for (i=0; i < stateSpaceDim;i++){
        for (j=0;j < stateSpaceDim;j++){
            J[i][j]=df.operator ()(i, j);
        }
    }
    
//    dgeev_("N", "V", &stateSpaceDim, &J[0][0], &lda, &wr[0], &wi[0],
//            &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork,
//            &info); // only the right eigenvectors are needed.
    
//    dgeev_("N", "V", &stateSpaceDim, &J[0][0], &stateSpaceDim, &eigenValR, &eigenValI,
//            &vl[0][0], &stateSpaceDim, &vr[0][0], &stateSpaceDim, &work[0], &lwork,
//            &info); // only the right eigenvectors are needed.
    
}


#endif //! _RealMatrix2_H
