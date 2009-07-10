#ifndef EIGEN_H
#define EIGEN_H

#ifdef TESTEIGEN
#define TEST_EIGEN
#endif 
#define EIG_MAX 2 //used to be 3
#define EPS 1e-30


#include <stdio.h>
#include <stdlib.h>
#include <math.h> // To use fabs
#include "Vector3.h"
// The structure that holds an eigencouple
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

void fill_eigen(struct eigen* , double* , double*, double*, double*);
//void print_eigen(struct eigen*, int);
//void print_eigen_struct(struct eigen*);
 int eigen_comp(const void*, const void*);
void sort_eigen(struct eigen*);
void transpose(double*, int);


//LAPACK prototype

//  void dgeev_(char * , char *, int *, double *, int*, double *, double *, 
//               double *, int *, double *, int *, double *, int *, 
//               int *);

#endif // EIGEN_H
