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
    
    RealVector * f();
    JacobianMatrix *jacobian();
    HessianMatrix * hessian() ;
    
    
    void jacobian( JacobianMatrix & jMatrix);
    void hessian (HessianMatrix & hMatrix);
    
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

inline JetMatrix::JetMatrix(void) :
    n_comps_(1),
            size_(3),
            v_(size_),
            c0_(false),
            c1_(false),
            c2_(false) {
    }
    
    inline JetMatrix::JetMatrix(const int n_comps) :
        n_comps_(n_comps), size_(n_comps * (1 + n_comps * (1 + n_comps))),
                v_(size_),
                c0_(false),
                c1_(false),
                c2_(false) {
        }
        
        inline JetMatrix::JetMatrix(const JetMatrix & jetMatrix) :
            n_comps_(jetMatrix.n_comps_), size_(n_comps_ * (1 + n_comps_ * (1 + n_comps_))),
                    v_(jetMatrix.v_),
                    c0_(false),
                    c1_(false),
                    c2_(false) {
            }
            
            inline int JetMatrix::n_comps(void) const {
                return n_comps_;
            }
            
            inline void JetMatrix::resize(int n_comps) {
                v_.resize(n_comps * (1 + n_comps * (1 + n_comps)));
                n_comps_ = n_comps;
            }
            
            inline void JetMatrix::range_check(int comp) const {
                if (comp < 0 || comp >= n_comps())
                    THROW(JetMatrix::RangeViolation());
            }
            
            inline JetMatrix & JetMatrix::zero(void) {
                v_.zero();
                return *this;
            }
            
            inline double * JetMatrix::operator()(void) {
                return v_.components();
            }
            
            inline double JetMatrix::operator()(int i) {
                range_check(i);
                if (!c0_)
                    THROW(JetMatrix::RangeViolation());
                return v_.component(i);
            }
            
            inline double JetMatrix::operator()(int i, int j) {
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
            
            inline void JetMatrix::operator()(int i, double value) {
                range_check(i);
                c0_=true;
                double * value_ = & v_.component(i);
                *value_ = value;
            }
            
            inline void JetMatrix::operator()(int i, int j, double value) {
                range_check(i);
                range_check(j);
                c1_=true;
                
                double * value_ = & v_.component((n_comps_) + (i*n_comps_ + j));
                *value_ = value;
            }
            
            inline void JetMatrix::operator()(int i, int j, int k, double value) {
                range_check(i);
                range_check(j);
                range_check(k);
                c2_=true;
                double * value_ = & v_.component((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
                *value_ = value;
            }
            
                

            
            inline RealVector * JetMatrix::f(){
                
                int i ;
                RealVector * value = new RealVector(n_comps());
                for (i=0; i < n_comps();i++){
                    value->operator ()(i)= operator()(i);
                }
                return value;
            }
            
            inline JacobianMatrix * JetMatrix::jacobian(){
                int i, j;
                JacobianMatrix * jMatrix = new JacobianMatrix(n_comps());
                for (i=0;i < n_comps(); i++){
                    for (j=0; j < n_comps();j++ ){
                        double value = operator()(i, j);
                        jMatrix->operator()(i, j, value);
                    }
                }
                return jMatrix;
            }
            
            inline HessianMatrix * JetMatrix::hessian(){
                
                int i, j, k;
                
                HessianMatrix * hMatrix = new HessianMatrix(n_comps());
                
                for (i=0;i < n_comps() ; i++){
                    for (j=0; j < n_comps() ; j++){
                        for (k=0 ;k < n_comps(); k++){
                            
                            double value = operator ()(i, j, k);
                            hMatrix ->operator ()(i, j, k, value);
                        }
                    }
                }
                return hMatrix;
                
            }
            
            inline void JetMatrix::jacobian(JacobianMatrix &jMatrix){
                
                int i, j;
                for (i=0;i < n_comps(); i++){
                    for (j=0; j < n_comps();j++ ){
                        double value = operator()(i, j);
                        jMatrix.operator ()(i,j,value);
                    }
                }
            }
             inline void JetMatrix::hessian(HessianMatrix & hMatrix){
                
                int i, j, k;
                
                for (i=0;i < n_comps() ; i++){
                    for (j=0; j < n_comps() ; j++){
                        for (k=0 ;k < n_comps(); k++){
                             double value = operator()(i, j,k);
                            
                            hMatrix.operator ()(i, j, k, value);
                        }
                    }
                }
            }
            
            
            
            
#endif //! _JetMatrix_H
