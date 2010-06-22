#ifndef MATRIX_H
#define MATRIX_H


#ifdef TESTMATRIX
#define TEST_MATRIX
#endif 

#include "eigen.h"

extern"C" {

    void dgesv_(int *, int *, double *, int *, int *, double *, int *, int *);

}

double prodint(int, double*, double*);
void matrixmult(int, int, int, double*, double*, double*);
void vectortensormult(int, double*, double*, double*);
void applyH(int, double*, double*, double*, double*);
int cdgesv(int, double*, double*, double*);

#endif // MATRIX_H
