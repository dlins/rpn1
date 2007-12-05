/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RealMatrix2.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealMatrix2.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
//#define RP_... 1

//! Code comes here! daniel@impa.br



inline void RealMatrix2::range_check(int i, int j) const {
    if ( ((i < 0) && (i> row_)) || ((j < 0) && (j > col_))  )
        THROW(RealMatrix2::RangeViolation());
}

RealMatrix2::RealMatrix2(int row, int col):data_(row*col), row_(row), col_(col){}

RealMatrix2::RealMatrix2():data_(2), row_(2), col_(2){}

RealMatrix2::RealMatrix2(const RealMatrix2 & copy):data_(copy.data_){}


double RealMatrix2::operator()(int i, int j) const {
    range_check(i, j);
    return data_.component((row_) + (i*col_ + j));
}

void RealMatrix2::operator ()(int i, int j , double value){
    range_check(i, j);
    data_.component((row_) + (i*col_ + j))=value;
}


void RealMatrix2::fillEigenData(int stateSpaceDim, RealMatrix2 &df, double & eigenValR, double & eigenValI, RealVector & eigenVec){
    
    
    int i, j, info, lwork= 5*stateSpaceDim;
    
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



