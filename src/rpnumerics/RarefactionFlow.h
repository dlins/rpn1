/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlow.h
 */

#ifndef _RarefactionFlow_H
#define _RarefactionFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpFunction.h"
#include "WaveState.h"

#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


#define EIG_MAX 2
#define ABORTED_PROCEDURE   (-7)
#define COMPLEX_EIGENVALUE  (-7)
#define SUCCESSFUL_PROCEDURE  2
#define EPS 1e-30

extern "C" {
    
    double ddot_(int *, double *, int *, double *, int *);
    
    void dgeev_(char * , char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);
}


class RarefactionFlow: public RpFunction{
    
private:
    
    
    int familyIndex_;
    
    
    int rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param) const ;
    
    struct eigen{
        double r;            // Eigenvalue's real part
        double i;            // Eigenvalue's imaginary part
        double vlr[EIG_MAX]; // Left-eigenvector's real part
        double vli[EIG_MAX]; // Left-eigenvector's imaginary part
        double vrr[EIG_MAX]; // Right-eigenvector's real part
        double vri[EIG_MAX]; // Right-eigenvector's imaginary part
        
        int n: EIG_MAX;     // Probably not necessary!
        // See what is to be done in case of non-trivial Jordan canonical forms
    };
    
    
    // Eigenproblem
// This function computes the eigenvalues and left- and right-eigenvectors of a matrix
// using LAPACK's dgeev and sorts them according to the value of the real part
// of the eigenvalues, in crescent order.
//
// The parameters are:
//
//     n: Dimension of the field and therefore of the matrix (n*n). Probably not necessary.
//     A: The n*n matrix.
//     e: The array of struct eigen where the eigencouples will be stored.
//
// The function returns:
//
//     ABORTED_PROCEDURE:   LAPACK's dgeev returned info != 0, there was a problem. This
//                          should never happen.
//     SUCCESFUL_PROCEDURE: Everything's OK.
//
    int cdgeev(int n, double *A, struct eigen *e)const ;
    
    void transpose(double A[], int n)const;
    
    void fill_eigen(struct eigen e[], double rp[], double ip[], double vl[], double vr[])const ;
    
    void sort_eigen(struct eigen e[])const ;
    
    static int eigen_comp(const void *p1, const void *p2);
    
    void applyH(int n, double *xi, double *H, double *eta, double *out)const ;
    
    double prodint(int n, double *a, double *b)const ;
    
    void matrixmult(int m, int p, int n, double *A, double *B, double *C)const ;
    
    int flux(int n, int family, double *in,  double *out) const;
    
    RealVector * referenceVector_;
    
    double lambda_;
    
    
    
public:
    int jet(const WaveState &u, JetMatrix &m, int degree) const ;
    
    RarefactionFlow(const RealVector &, const int);
    
    RarefactionFlow(const int);
    
    RarefactionFlow(const RarefactionFlow &);
    
    const RealVector & getReferenceVector() const;
    
    void setReferenceVector(const RealVector & );
    
    RpFunction * clone() const;
    
    double lambdaCalc(const WaveState & , int );
    
    virtual ~RarefactionFlow();
    
    void setLambda(const double );
    double getLambda();
    
    int getFamilyIndex()const;
    void setFamilyIndex(int);
    
};


inline void RarefactionFlow::setLambda(const double lambda){lambda_=lambda;}

inline double RarefactionFlow::getLambda(){return lambda_;}

inline int RarefactionFlow::getFamilyIndex()const {return familyIndex_;}

inline void RarefactionFlow::setFamilyIndex(int familyIndex ) {familyIndex_=familyIndex;}

inline double RarefactionFlow::prodint(int n, double *a, double *b)const {
    int incx = 1, incy = 1;
    return ddot_(&n, a, &incx, b, &incy);
}


inline const RealVector & RarefactionFlow::getReferenceVector() const {return *referenceVector_;}
//
inline void RarefactionFlow::setReferenceVector(const RealVector & referenceVector){
    delete referenceVector_;
    referenceVector_=new RealVector(referenceVector);
}






#endif //! _RarefactionFlow_H
