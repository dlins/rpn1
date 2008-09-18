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
    void dgeev_(char *, char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);
}

class RealMatrix2 {
private:

    Vector * data_;
    int row_, col_;

    class RangeViolation : public exception {
    };

public:

    RealMatrix2(int, int);
    RealMatrix2(int);

    ~RealMatrix2();
    RealMatrix2();
    RealMatrix2(const RealMatrix2 &);

    void range_check(int i, int j) const;

    double operator ()(int i, int j) const;

    void operator ()(int i, int j, double value);

    RealMatrix2 & operator-(const RealMatrix2 &);

    void mul(const RealMatrix2 &)const;

    void fillEigenData(int stateSpaceDim, RealMatrix2 & df, double & eigenValR, double & eigenValI, RealVector & eigenVec);

    void scale(double t);

    int row()const;
    int col()const;

};

inline void RealMatrix2::range_check(int i, int j) const {
    if (((i < 0) && (i > row_)) || ((j < 0) && (j > col_)))
        THROW(RealMatrix2::RangeViolation());
}

inline double RealMatrix2::operator()(int i, int j) const {
    range_check(i, j);
    return data_->component(i * row_ + j);

}

inline RealMatrix2 & RealMatrix2::operator-(const RealMatrix2 & b) {
    for (int i = 0; i < row_; i++) {
        for (int j = 0; j < col_; j++) {
            operator()(i, j, operator()(i, j) - b(i, j));
        }
    }
    return *this;

}

inline void RealMatrix2::operator ()(int i, int j, double value) {
    range_check(i, j);
    data_->component(i * row_ + j) = value;

}

inline int RealMatrix2::row()const {
    return row_;
}

inline int RealMatrix2::col()const {
    return col_;
}

inline void RealMatrix2::scale(double t) {

    for (int i = 0; i < row_; i++) {
        for (int j = 0; j < col_; j++) {
            data_->component(i * row_ + j) = data_->component(i * row_ + j) * t;
        }
    }

}
#endif //! _RealMatrix2_H
