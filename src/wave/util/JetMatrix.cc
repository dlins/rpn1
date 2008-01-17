/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "JetMatrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JetMatrix::JetMatrix(void) :n_comps_(1), size_(3), v_(size_), c0_(false), c1_(false), c2_(false){ }

JetMatrix::JetMatrix(const int n_comps) :n_comps_(n_comps), size_(n_comps * (1 + n_comps * (1 + n_comps))),
        v_(size_),
        c0_(false),
        c1_(false),
        c2_(false) { }

JetMatrix::JetMatrix(const JetMatrix & jetMatrix) :
    n_comps_(jetMatrix.n_comps_), size_(n_comps_ * (1 + n_comps_ * (1 + n_comps_))),
            v_(jetMatrix.v_), c0_(false), c1_(false), c2_(false) {  }
    
    int JetMatrix::n_comps(void) const { return n_comps_;  }
    
    JetMatrix::~JetMatrix(){};
    
    void JetMatrix::resize(int n_comps) {
        v_.resize(n_comps * (1 + n_comps * (1 + n_comps)));
        n_comps_ = n_comps;
    }
    
    
    
    JetMatrix & JetMatrix::zero(void) {
        v_.zero();
        return *this;
    }
    
    double * JetMatrix::operator()(void) {
        return v_.components();
    }
    
    double JetMatrix::operator()(int i) {
        range_check(i);
        if (!c0_)
            THROW(JetMatrix::RangeViolation());
        return v_.component(i);
    }
    
    double JetMatrix::operator()(int i, int j) {
        range_check(i);
        range_check(j);
        if (!c1_)
            THROW(JetMatrix::RangeViolation());
        return v_.component((n_comps_) + (i*n_comps_ + j));
    }
    
    void JetMatrix::f(RealVector & vector){//TODO Checar esse metodo !!!

        int i;
        for (i=0; i < n_comps();i++){
           
            vector.component(i)=operator()(i);
        }
    }
    
    void JetMatrix::jacobian(JacobianMatrix &jMatrix){
        
        int i, j;
        for (i=0;i < n_comps(); i++){
            for (j=0; j < n_comps();j++ ){
                double value = operator()(i, j);
                jMatrix.operator ()(i, j, value);
            }
        }
    }
    
    void JetMatrix::hessian(const HessianMatrix & hMatrix){
        
        int i, j, k;
        
        for (i=0;i < n_comps() ; i++){
            for (j=0; j < n_comps() ; j++){
                for (k=0 ;k < n_comps(); k++){
                    double value = hMatrix.operator()(i, j, k);
                    operator()(i, j, k, value);
                }
            }
        }
    }
    
    
    void JetMatrix::hessian(HessianMatrix & hMatrix){
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
    
    
    
    
