#ifndef _EIGEN_
#define _EIGEN_

#include <vector>
#include <algorithm>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

using namespace std;

// When beta is near zero (for the generalized eigenproblem),
// function eig() must return this value. This situation is anomalous.
//
#define _EIGEN_BETA_NEAR_ZERO_ -123
#define COMPLEX_EIGENVALUE (-7)
#define SUCCESSFUL_PROCEDURE 2
#define ABORTED_PROCEDURE (-7)
#define LAMBDA_ERROR (-7)
#define LAMBDA_NOT_INCREASING (-7)
#define LAMBDA_NOT_DECREASING (-7)
#define _SIMPLE_ACCUMULATION_  10  // Traditional rarefaction, using dgeev.
#define _GENERAL_ACCUMULATION_ 11  // Rarefactio


// Eigenproblem, A*v = lambda*v
//
extern "C" void dgeev_(const char*, const char*, int*, double*, int*, double*, double*, 
           double*, int*, double*, int*, double*, int*, 
           int*);

// Generalized eigenproblem, A*v = lambda*B*v
//
extern "C" void dggev_(const char*, const char*,  // JOBVL, JOBVR
                  int*,                      // N
                  double*, int*,             // A, LDA
                  double*, int*,             // B, LDB
                  double*,                   // ALPHAR
                  double*,                   // ALPHAI
                  double*,                   // BETA
                  double*, int*,             // VL, LDVL,
                  double*, int*,             // VR, LDVR,
                  double*, int*,             // WORK, LWORK
                  int*                       // INFO
                 );

/* Struct to hold an eigenpair. */
struct eigenpair {
    public:
        double r;           // Real part of the eigenvalue
        double i;           // Imaginary part of the eigenvalue

        vector<double> vlr; // Real part of the left-eigenvector
        vector<double> vli; // Imaginary part of the left-eigenvector
        vector<double> vrr; // Real part of the right-eigenvector
        vector<double> vri; // Imaginary part of the right-eigenvector
};
/* Struct to hold an eigenpair. */

/* Class Eigen. */
class Eigen {
    private:
        static double epsilon;
        static void transpose(int, double*);
        static void fill_eigen(int, eigenpair*, double*, double*, double*, double*);
        static int eigen_comp(const void *, const void *); // To be deprecated
        static bool eigen_compare(const eigenpair&, const eigenpair&);
        static void sort_eigen(int, eigenpair*);
    protected:
    public:
        static int eig(int, const double*, vector<eigenpair>&);                // Eigenproblem
        static int eig(int, const double*, const double*, vector<eigenpair>&); // Generalized eigenproblem
        static void print_eigen(const vector<eigenpair> &);
        static void eps(double);
        static double eps(void);
};
/* Class Eigen. */


#endif // _EIGEN_
