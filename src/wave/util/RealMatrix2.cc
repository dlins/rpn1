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





RealMatrix2::RealMatrix2(int row, int col) : data_(new Vector(row*col)), row_(row), col_(col) {
}

RealMatrix2::RealMatrix2(int rowcol) : data_(new Vector(rowcol*rowcol)), row_(rowcol), col_(rowcol) {

    for (int i = 0; i < rowcol; i++) {
        for (int j = 0; j < rowcol; j++) {
           
           
            if (i == j)
                data_->component(i * rowcol + j) = 1;
            else
                data_->component(i * rowcol + j) = 0;
        }
    }

}

RealMatrix2::RealMatrix2() : data_(new Vector(4)), row_(2), col_(2) {
}

RealMatrix2::RealMatrix2(const RealMatrix2 & copy) : data_(copy.data_) {
}

void RealMatrix2::fillEigenData(int stateSpaceDim, RealMatrix2 &df, double & eigenValR, double & eigenValI, RealVector & eigenVec) {


    int i, j, info, lwork = 5 * stateSpaceDim;

    int lda = stateSpaceDim;
    int ldvr = stateSpaceDim;
    int ldvl = stateSpaceDim;

    double wr[stateSpaceDim];
    double wi[stateSpaceDim];

    double J[stateSpaceDim][stateSpaceDim], vl[stateSpaceDim][stateSpaceDim], vr[stateSpaceDim][stateSpaceDim];

    double work[stateSpaceDim];

    for (i = 0; i < stateSpaceDim; i++) {
        for (j = 0; j < stateSpaceDim; j++) {
            J[i][j] = df(i, j);
        }
    }

    dgeev_("N", "V", &stateSpaceDim, &J[0][0], &lda, &wr[0], &wi[0],
            &vl[0][0], &ldvl, &vr[0][0], &ldvr, &work[0], &lwork,
            &info); // only the right eigenvectors are needed.

}

void RealMatrix2::mul(const RealMatrix2 & m1) const {

    int i, j, k;

    if (col_ != m1.row() || col_ != m1.col())

        THROW(RealMatrix2::RangeViolation());

    Vector temp(row_ * col_);

    for (i = 0; i < row_; i++) {
        for (j = 0; j < col_; j++) {
            temp.component((row_) + (i * col_ + j)) = 0.0;
            for (k = 0; k < col_; k++) {
                temp.component((row_) + (i * col_ + j)) += data_->component((row_) + (i * col_ + k)) * m1(k, j);
            }
        }
    }

    for (i = 0; i < row_; i++) {
        for (j = 0; j < col_; j++) {
            data_->component((row_) + (i * col_ + j)) = temp((row_) + (i * col_ + j));
        }
    }
}

std::ostream & operator<<(std::ostream& o, const RealMatrix2& jMatrix) {

    for (int i = 0; i < jMatrix.row_; i++) {
        for (int j = 0; j < jMatrix.col_; j++) {

            o << "("<<i<<","<<j<<")"<<jMatrix(i, j)<<"\n";
        }
    }

    return o;
}




RealMatrix2::~RealMatrix2() {
    delete data_;
}

