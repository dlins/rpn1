#ifndef _EIGEN_
#define _EIGEN_

#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

//#include "global_composite.h"



#define SUCCESSFUL_PROCEDURE 2
#define ABORTED_PROCEDURE (-7)
#define COMPLEX_EIGENVALUE (-7)
#define LAMBDA_ERROR (-7)
#define LAMBDA_NOT_INCREASING (-7)
#define LAMBDA_NOT_DECREASING (-7)



//using namespace std;

extern "C" void dgeev_(char*, char*, int*, double*, int*, double*, double*, 
           double*, int*, double*, int*, double*, int*, 
           int*);


/* Struct to hold an eigencouple. */
struct eigencouple {
    public:
        double r;           // Real part of the eigenvalue
        double i;           // Imaginary part of the eigenvalue

        std::vector<double> vlr; // Real part of the left-eigenvector
        std::vector<double> vli; // Imaginary part of the left-eigenvector
        std::vector<double> vrr; // Real part of the right-eigenvector
        std::vector<double> vri; // Imaginary part of the right-eigenvector
};
/* Struct to hold an eigencouple. */



/* Class Eigen. */
class Eigen {
    private:
        static double epsilon;
        static void transpose(int, double*);
        static void fill_eigen(int, eigencouple*, double*, double*, double*, double*);
        static int eigen_comp(const void *, const void *);
        static void sort_eigen(int, eigencouple*);
    protected:
    public:
        static int eig(int, double*, std::vector<eigencouple>&);
        static void print_eigen(const std::vector<eigencouple>&);
        static void eps(double);
        static double eps(void);
};
/* Class Eigen. */


#endif // _EIGEN_
