/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ContinuationRarefactionFlow.h
 */

#ifndef _ContinuationRarefactionFlow_H
#define _ContinuationRarefactionFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <math.h>
#include <stdlib.h>
#include "RealVector.h"
#include "JacobianMatrix.h"
#include "HessianMatrix.h"
#include "RarefactionFlow.h"
#include "eigen.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


// Maximum number of rows and columns of the matrix being considered, 
// and consequently of the number of its eigenvalues
////#define EIG_MAX 2      

// Epsilon value, minimum threshold. 
// Any number x such that abs(x) < EPS will be considered 
// equal to zero in certain circumstances
#define EPS 1e-30      

// Maximum number of points in the orbit
#define PNT_MAX 10000  

// Functions that successfully carry on their task will return SUCCESSFUL_PROCEDURE,
// since most of the results are not floats or ints, but vectors and as such must
// be returned by means of pointers (which are passed in the parameters).
//
// Functions that encounter some kind of problem will return ABORTED_PROCEDURE.
// In this case the results of the functions should be disregarded, i.e., 
// a function invoking another function MUST check wether the invoked function
// could or could not successfully perform its task.
//
#define SUCCESSFUL_PROCEDURE 2                    
#define ABORTED_PROCEDURE (-7)
#define COMPLEX_EIGENVALUE (-7)
#define LAMBDA_ERROR (-7)
#define LAMBDA_NOT_INCREASING (-7)
#define LAMBDA_NOT_DECREASING (-7)



extern"C" {
    void dgeev_(char *, char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);


    double ddot_(int *, double *, int *, double *, int *);
}

class ContinuationRarefactionFlow : public RarefactionFlow {
private:

    // The structure that holds an eigencouple
    /*
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
     */

    int D2F(int n, double *in, double *out)const;
    int DF(int n, double *in, double *out)const;
    int cdgeev(int n, double *A, struct eigen *e) const;


    void transpose(double A[], int n)const;
    void fill_eigen(struct eigen e[], double rp[], double ip[], double vl[], double vr[])const;
    void sort_eigen(struct eigen e[])const;
    static int eigen_comp(const void *p1, const void *p2);
    void applyH(int n, double *xi, double *H, double *eta, double *out)const;

    void matrixmult(int m, int p, int n, double *A, double *B, double *C)const;


    // Last viable eigenvalue
    double lasteigenvalue;

    // Last viable shock speed
    //double shock_speed_var;

    // Reference speed (See the identity in Proposition 10.11 of
    // "An Introduction to Conservation Laws:
    // Theory and Applications to Multi-Phase Flow" (monotonicity
    // of the rarefaction).
    double ref_speed;

    // Reference vector whose inner product with the eigenvector
    // must be positive.
    double re[EIG_MAX];

    int family_;



public:
    ContinuationRarefactionFlow(const int, const int, const FluxFunction & );

    int rarefaction(int *neq, double *xi, double *in, double *out, int *nparam, double *param);
    int flux(int n, int family, double *in, double *lambda, double *out);


    int flux(const RealVector &, RealVector &);
    int fluxDeriv(const RealVector &, JacobianMatrix &);
    int fluxDeriv2(const RealVector &, HessianMatrix &);


    double prodint(int n, double *a, double *b)const;

    WaveFlow * clone()const;

    void setReferenceVectorComponent (const int , const double);
    double getReferenceVectorComponent(const int)const;
    virtual ~ContinuationRarefactionFlow();

};

#endif //! _ContinuationRarefactionFlow_H
