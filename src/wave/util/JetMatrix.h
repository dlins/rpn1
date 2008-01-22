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
    
    
//    void setF(RealVector &);
    void f(RealVector &);
    void jacobian(JacobianMatrix & jMatrix);
    void hessian(HessianMatrix & hMatrix);
    
    void setf(const RealVector &);
    void setjacobian(const JacobianMatrix & jMatrix);
    void sethessian(const HessianMatrix & hMatrix);
    
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


inline   JetMatrix & JetMatrix::zero(void) {
    v_.zero();
    return *this;
}

inline  double * JetMatrix::operator()(void) {
    return v_.components();
}

inline  double JetMatrix::operator()(int i) {
    range_check(i);
    if (!c0_)
        THROW(JetMatrix::RangeViolation());
    return v_.component(i);
}

inline  double JetMatrix::operator()(int i, int j) {
    range_check(i);
    range_check(j);
    if (!c1_)
        THROW(JetMatrix::RangeViolation());
    return v_.component((n_comps_) + (i*n_comps_ + j));
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



inline void JetMatrix::sethessian(const HessianMatrix & input){
    
    int i, j, k;
    for (i=0;i < n_comps() ; i++){
        for (j=0; j < n_comps() ; j++){
            for (k=0 ;k < n_comps(); k++){
                
                operator()(i, j, k, input.operator()(i, j, k));
                
            }
        }
    }
}


inline void JetMatrix::hessian(HessianMatrix & hMatrix){
    int i, j, k;
    for (i=0;i < n_comps() ; i++){
        for (j=0; j < n_comps() ; j++){
            for (k=0 ;k < n_comps(); k++){
                double value = operator()(i, j, k);
                hMatrix.operator ()(i, j, k, value);
            }
        }
    }
}

inline void JetMatrix::setf(const RealVector &input){
    int i;
    for (i=0;i< n_comps();i++){
        operator()(i, input.component(i));
    }
    
    
}

inline void JetMatrix::f(RealVector & vector){
    
    int i;
    for (i=0; i < n_comps();i++){
        
        vector.component(i)=operator()(i);
    }
}


inline void JetMatrix::setjacobian(const JacobianMatrix & input){
    
    int i, j;
    for(i=0;i< n_comps();i++){
        for (j=0; j < n_comps();j++){
            operator()(i, j, input.operator ()(i, j));
        }
    }
    
    
}

inline void JetMatrix::jacobian(JacobianMatrix &jMatrix){
    
    int i, j;
    for (i=0;i < n_comps(); i++){
        for (j=0; j < n_comps();j++ ){
            double value = operator()(i, j);
            jMatrix.operator ()(i, j, value);
        }
    }
}






#endif //! _JetMatrix_H
