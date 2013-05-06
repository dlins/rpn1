#ifndef _DOUBLEMATRIX_
#define _DOUBLEMATRIX_

#include <stdio.h>
#include <string>
#include <sstream>
#include "Matrix.h"

#define DOUBLEMATRIXPRINTWIDTH 14

extern "C" {
    // Linear system of equations solver
    void dgesv_(int*, int*, double*, int*, int*, double*, int*, int*);

    // LU decomposition
    void dgetrf_(int *M, int *N, double *A, int *LDA, int *IPIV, int *INFO);

    // Matrix inversion
    void dgetri_(int *N, double *A, int *LDA, int *IPIV, double *WORK, int *LWORK, int *INFO);
}

class DoubleMatrix : public Matrix<double> {
    private:
    protected:
        int w_; // Default width for the centered_text.
        static std::string centered_text(double x, int w);
    public:
        DoubleMatrix(void);
        DoubleMatrix(int n, int m);
        DoubleMatrix(const DoubleMatrix &original);
        DoubleMatrix(const DoubleMatrix *original);
        ~DoubleMatrix();

        void print(void) const;

        // Create the identity matrix
        static DoubleMatrix eye(int n);

        // Set/get the width used for printing
        void w(int ww){w_ = ww; return;}
        int w(void) const {return w_;}

        // Matrix sum and subtraction
        friend DoubleMatrix sum(const DoubleMatrix &A, const DoubleMatrix &B);
        friend DoubleMatrix operator+(const DoubleMatrix &A, const DoubleMatrix &B);

        friend DoubleMatrix sub(const DoubleMatrix &A, const DoubleMatrix &B);
        friend DoubleMatrix operator-(const DoubleMatrix &A, const DoubleMatrix &B);

        // Matrix multiplication 
        friend DoubleMatrix mult(const DoubleMatrix &A, const DoubleMatrix &B);
        friend DoubleMatrix operator*(const DoubleMatrix &A, const DoubleMatrix &B);

        // Multiplication by a scalar
        friend DoubleMatrix mult(const DoubleMatrix &A, double alpha);
        friend DoubleMatrix mult(double alpha, const DoubleMatrix &A);
        friend DoubleMatrix operator*(const DoubleMatrix &A, double alpha);
        friend DoubleMatrix operator*(double alpha, const DoubleMatrix &A);

        // Some matrix operations
        friend int solve(const DoubleMatrix &A, const DoubleMatrix &b, DoubleMatrix &x);
        friend int inverse(const DoubleMatrix &A, DoubleMatrix &B);
        friend double det(const DoubleMatrix &A);
        friend DoubleMatrix transpose(const DoubleMatrix &A);
};

#endif // _DOUBLEMATRIX_

