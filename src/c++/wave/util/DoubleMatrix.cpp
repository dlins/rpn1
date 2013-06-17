#include "DoubleMatrix.h"
#include "Debug.h"

std::string DoubleMatrix::centered_text(double x, int w){
    std::stringstream s;
    s << x;

    std::string temp(s.str());

    int noel, sl, noer;
    sl = temp.size();
    noel = floor((w - sl)/2);
    noer = w - sl - noel;

    std::stringstream ss;
    for (int i = 0; i < noel; i++) ss << std::string(" ");

    ss << x;

    for (int i = 0; i < noer; i++) ss << std::string(" ");

    return ss.str();
}

DoubleMatrix::DoubleMatrix(void) : Matrix<double>(), w_(DOUBLEMATRIXPRINTWIDTH){}

DoubleMatrix::DoubleMatrix(int n, int m) : Matrix<double>(n, m), w_(DOUBLEMATRIXPRINTWIDTH){}

DoubleMatrix::DoubleMatrix(const DoubleMatrix &original) : Matrix<double>(original), w_(DOUBLEMATRIXPRINTWIDTH){}

DoubleMatrix::DoubleMatrix(const DoubleMatrix *original) : Matrix<double>(original), w_(DOUBLEMATRIXPRINTWIDTH){}

DoubleMatrix::~DoubleMatrix(){
    if ( Debug::get_debug_level() == 5 ) {
        printf("DoubleMatrix::~DoubleMatrix()\n");
    }
}

void DoubleMatrix::print(void) const {
    for (int i = 0; i < rows_; i++){
        printf("|");
        for (int j = 0; j < cols_; j++){
            //printf(" % 14.8g ", vec[i*cols_ + j]);
            printf("%s", centered_text(vec[i*cols_ + j], w_).c_str());
            if (j < cols_ - 1) printf(":");
        }
        printf("|\n");
    }

    return;
}

// Identity matrix
//
DoubleMatrix DoubleMatrix::eye(int n){
    DoubleMatrix e(n, n);

    for (int i = 0; i < n; i++){
        for (int j = 0; j < i; j++) e(i, j) = e(j, i) = 0.0;
        e(i, i) = 1.0;
    }

    return e;
}

// Returns A + B
//
DoubleMatrix sum(const DoubleMatrix &A, const DoubleMatrix &B){
    DoubleMatrix C(A.rows_, A.cols_);

    for (int i = 0; i < A.rows_*A.cols_; i++) C(i) = A(i) + B(i);

    return C;
}

DoubleMatrix operator+(const DoubleMatrix &A, const DoubleMatrix &B){
    return sum(A, B);
}

// Returns A - B
//
DoubleMatrix sub(const DoubleMatrix &A, const DoubleMatrix &B){
    DoubleMatrix C(A.rows_, A.cols_);

    for (int i = 0; i < A.rows_*A.cols_; i++) C(i) = A(i) - B(i);
    return C;
}

DoubleMatrix operator-(const DoubleMatrix &A, const DoubleMatrix &B){
    return sub(A, B);
}

// Returns A*B
//
DoubleMatrix mult(const DoubleMatrix &A, const DoubleMatrix &B){
    int m = A.rows_;
    int n = B.cols_;
    int p = A.cols_;

    DoubleMatrix C(m, n);

    for (int i = 0; i < m; i++){
        for (int j = 0; j < n; j++){
            double s = 0.0;

            for (int k = 0; k < p; k++) s += A(i, k)*B(k, j);
            C(i, j) = s;
        }
    }

    return C;
}

DoubleMatrix operator*(const DoubleMatrix &A, const DoubleMatrix &B){
    return mult(A, B);
}

// Returns alpha*A
//
DoubleMatrix mult(const DoubleMatrix &A, double alpha){
    DoubleMatrix B(A);

    for (int i = 0; i < B.rows_*B.cols_; i++) B(i) *= alpha;

    return B;
}

DoubleMatrix mult(double alpha, const DoubleMatrix &A){
    return mult(A, alpha);
}

DoubleMatrix operator*(const DoubleMatrix &A, double alpha){
    return mult(A, alpha);
}

DoubleMatrix operator*(double alpha, const DoubleMatrix &A){
    return mult(A, alpha);
}

int solve(const DoubleMatrix &A, const DoubleMatrix &b, DoubleMatrix &x){
    int i, j;
    int dim = A.rows();
    int nrhs = 1;
    int lda = dim;
    int ipiv[dim];
    int ldb = dim;
    int info;

    // Create a transposed copy of A to be used by LAPACK's dgesv:
    DoubleMatrix B(transpose(A));

    // Create a copy of b to be used by LAPACK's dgesv:
    double bb[dim];
    for (i = 0; i < dim; i++) bb[i] = b(i, 0);

    dgesv_(&dim, &nrhs, B.data(), &lda, &ipiv[0], bb, &ldb, &info);

    x.resize(dim, 1);
    if (info == 0){
        for (i = 0; i < dim; i++) x(i, 0) = bb[i];
    }

    return info;
}

int inverse(const DoubleMatrix &A, DoubleMatrix &B){
    int n = A.rows_;

    B = transpose(A);

    // LU factorization
    //
    int lu_info, lda = n, ipiv[n];
    dgetrf_(&n, &n, B.data(), &lda, ipiv, &lu_info);
    if (lu_info != 0) return lu_info;

    if ( Debug::get_debug_level() == 5 ) {
        for (int i = 0; i < n; i++) printf("ipiv(%d) = %d\n", i, ipiv[i]);
    }

    // Matrix inversion proper
    //
    int lwork = n;
    double work[lwork];
    int inv_info;
    dgetri_(&n, B.data(), &lda, ipiv, work, &lwork, &inv_info);

    B = transpose(B);

    return inv_info;
}

double det(const DoubleMatrix &A){
    int n = A.rows(); 

    if (n == 1) return A(0, 0);
    else {
        double temp = 0.0, power = 1.0;

        for (int i = 0; i < n; i++){
            DoubleMatrix AA(n - 1, n - 1);
            if (i == 0){
                for (int j = 1; j < n; j++){
                    for (int k = 1; k < n; k++) AA(j - 1, k - 1) = A(j, k);
                }
            }
            else if (i == (n - 1)){
                for (int j = 1; j < n; j++){
                    for (int k = 0; k < n - 1; k++) AA(j - 1, k - 0) = A(j, k);
                }
            }
            else {
                for (int j = 1; j < n; j++){
                    for (int k = 0; k <= i - 1; k++) AA(j - 1, k - 0) = A(j, k);
                    for (int k = i + 1; k < n; k++) AA(j - 1, k - 1) = A(j, k);
                }
            }

            double det_AA = det(AA);

            temp += A(0, i)*det_AA*power;
            power *= -1.0;
        }

        return temp;
    }
}

DoubleMatrix transpose(const DoubleMatrix &A){
    DoubleMatrix B(A.cols_, A.rows_);

    for (int i = 0; i < A.rows_; i++){
        for (int j = 0; j < A.cols_; j++) B(j, i) = A(i, j);
    }

    return B;
}

