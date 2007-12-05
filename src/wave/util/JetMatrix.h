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

            
            
            
#endif //! _JetMatrix_H
