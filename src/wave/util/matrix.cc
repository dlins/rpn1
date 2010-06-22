#include "matrix.h"

// Inner product of two vectors.
// This function uses BLAS' ddot function.
// The parameters are:
//
//     n: The number of elements of the vectors.
//     a: A pointer to a vector.
//     b: A pointer to a vector.
//
// This function can be easily replaced by a homebrew function.
// However, I find it useful to use BLAS' functions, since they
// can be more efficient.
double prodint(int n, double *a, double *b){
    extern double ddot_(int *, double *, int *, double *, int *);
    int incx = 1, incy = 1;
    return ddot_(&n, a, &incx, b, &incy);
}

// C = A*B
// A = m times p
// B = p times n
// C = m times n
void matrixmult(int m, int p, int n, double *A, double *B, double *C){
    extern double ddot_(int *, double *, int *, double *, int *);
    int i, j, k, incx = 1, incy = 1, pp = p;
    double sum;

    double arow[p], bcol[p];
    
    for (i = 0; i < m; i++){
        for (j = 0; j < n; j++){
            sum = 0;
            for (k = 0; k < p; k++) sum += A[i*p + k]*B[k*n + j];
            C[i*n + j] = sum; 
            
            // Alternate
            for (k = 0; k < p; k++){
                arow[k] = A[i*p + k];
                bcol[k] = B[k*n + j];
            }
            //C[i*n + j] = ddot_(&pp, &arow[0], &incx, &bcol[0], &incy);
            
            //printf("C[%d][%d]: Conventional = % f; ddot_ = % f\n", i, j, sum, C[i*n + j]);
            
        }
    }
     
    return;
}

// M = v*T
// v = n
// T = n times n times n
// M = n times n
// TODO: This function is to be dump'd.
void vectortensormult(int n, double *v, double *T, double *M){
    int i, j, k;
    double Tm[n][n], col[n];
    
    for (k = 0; k < n; k++){
        // Create a temporary matrix from the tensor
        //(k*m + i)*n + j
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++) Tm[i][j] = T[(k*n + i)*n + j];
        }
        
        // Multiply the vector by the temporary matrix and store it in M
        matrixmult(1, n, n, &v[0], &Tm[0][0], &col[0]);
        
        // Fill the k-th column of M with col
        for (i = 0; i < n; i++) M[i*n + k] = col[i];
    }
    
    return;
}

// applyH(xi, H, eta)
//       n: Dimension of the field.
//      xi: A vector with n components. 
//       H: Hessian of the field. It is a (n times n times n)-array, such that
//
//            H[k][i][j] = partial^2 f_k/(partial u_i*partial u_j).
//
//     eta: A vector with n components.   
//     out: A vector with n components, the output of the function. 
//          Its components are of the form:
//
//            out[k] = transpose(xi)*H[k][:][:]*eta.
//
// (See p. 111 of "An Introduction to Conservation Laws: 
// Theory and Applications to Multi-Phase Flow", dictaat-1.pdf)
void applyH(int n, double *xi, double *H, double *eta, double *out){
    int i, j, k;
    
    // Temporary matrix
    double Htemp[n][n];
    
    // Temporary vector
    double vtemp[n];
    
    for (k = 0; k < n; k++){
        for (i = 0; i < n; i++){
            for (j = 0; j < n; j++){
                // Fill the temporary matrix with the k-th layer of H,
                // i.e., with H[k][:][:]
                Htemp[i][j] = H[(k*n + i)*n + j];
                // vtemp = transpose(xi)*H[k][:][:]
                matrixmult(1, n, n, xi, &Htemp[0][0], &vtemp[0]);
                // out[k] = vtemp*eta
                matrixmult(1, n, 1, eta, &vtemp[0], &out[k]);
            }
        }
    }
    
    return;
}

// Solves the linear system A*x = b.
// This function receives the following parameters:
//
//    n: Dimension of the space
//    A: n times n matrix
//    b: n times 1 vector
//    x: n times 1 vector (here the solution will be stored)
//
// Returns:
// ABORTED_PROCEDURE: A problem arised, probably with A.
// SUCCESSFUL_PROCEDURE: Everything's OK.
int cdgesv(int n, double *A, double *b, double *x){
    int i, j;
    int dim = n;
    int nrhs = 1;
    int lda = n;
    int ipiv[n];
    int ldb = n;
    int info;

    // Create a transposed copy of A to be used by LAPACK's dgesv:
    double B[n][n];
    for (i = 0; i < n; i++){
        for (j = 0; j < n; j++) B[j][i] = A[i*n + j];
    }
    
    // Create a copy of b to be used by LAPACK's dgesv:
    double bb[n];
    for (i = 0; i < n; i++) bb[i] = b[i];
    
    dgesv_(&dim, &nrhs, &B[0][0], &lda, &ipiv[0], &bb[0], &ldb, &info);
    
    if (info == 0){
        for (i = 0; i < n; i++) x[i] = bb[i];
        return SUCCESSFUL_PROCEDURE; 
    }
    else return ABORTED_PROCEDURE;
}
