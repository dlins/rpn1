/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.h
 */

#ifndef _JetMatrix_H
#define _JetMatrix_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Vector.h"
#include "except.h"
#include "JacobianMatrix.h"
#include "HessianMatrix.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class JetMatrix {
private:
    int n_comps_, size_;
    Vector v_;
    bool c0_, c1_, c2_;
    
    class RangeViolation : public exception { };
    
public:
    JetMatrix(void);
    JetMatrix(const int n_comps);
    JetMatrix(const JetMatrix & jetMatrix);
    virtual ~JetMatrix();
    
    void f(RealVector &);
    void jacobian(JacobianMatrix & jMatrix);
    void hessian(HessianMatrix & hMatrix);
    
    
    void f(const RealVector &);
    void jacobian(const JacobianMatrix & jMatrix);
    void hessian(const HessianMatrix & hMatrix);
    
    int n_comps(void) const;
    void resize(int n_comps);
    void range_check(int comp) const;
    
    JetMatrix &zero(void);
    
    double * operator()(void);
    double operator()(int i);
    double operator()(int i, int j);
    double operator()(int i, int j, int k);
    
    void operator()(int i, double value);
    void operator()(int i, int j, double value);
    void operator()(int i, int j, int k, double value);
    
};

inline void JetMatrix::range_check(int comp) const {
    if (comp < 0 || comp >= n_comps())
        THROW(JetMatrix::RangeViolation());
}

inline double JetMatrix::operator()(int i, int j, int k) {
    range_check(i);
    range_check(j);
    range_check(k);
    if (!c2_)
        THROW(JetMatrix::RangeViolation());
    return v_.component((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
}

inline  void JetMatrix::operator()(int i, double value) {
    range_check(i);
    c0_=true;
    double * value_ = & v_.component(i);
    *value_ = value;
}

inline  void JetMatrix::operator()(int i, int j, double value) {
    range_check(i);
    range_check(j);
    c1_=true;
    
    double * value_ = & v_.component((n_comps_) + (i*n_comps_ + j));
    *value_ = value;
}

inline  void JetMatrix::operator()(int i, int j, int k, double value) {
    range_check(i);
    range_check(j);
    range_check(k);
    c2_=true;
    double * value_ = & v_.component((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
    *value_ = value;
}
#endif //! _JetMatrix_H
