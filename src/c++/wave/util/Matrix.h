#ifndef _MATRIX_
#define _MATRIX_

#include <stdlib.h>

/*    SIMPLE USE EXAMPLE:

#include "Matrix.h"
#include <stdio.h>

int main(){
    int n = 2, m = 3;
    Matrix<double> A(n, m);

    for (int i = 0; i < n; i++){
        for (int j = 0; j < m; j++) A(i, j) = 1 + 2*i + 3*j;
    }

    Matrix<double> B(A);

    Matrix<double> C, D;
    C = D = B;

    printf("C.rows = %d, C.cols = %d\n", C.rows(), C.cols());

    for (int i = 0; i < C.rows(); i++){
        for (int j = 0; j < C.cols(); j++) printf("C(%d, %d) = %f\n", i, j, C(i, j));
    }

    return 0;
}

     END OF EXAMPLE */

template <typename T>
class Matrix {
    private:
        T *vec;
        int rows_, cols_;

    public:
        Matrix(void);
        Matrix(int n, int m);
        Matrix(const Matrix<T> &original);
        Matrix(int n, int m, const T *original);
        ~Matrix();

        T& operator()(int i, int j);
        T& operator()(int i);

        T* data(void);

        Matrix<T> operator=(const Matrix<T> &original);

        void resize(int newn, int newm);

        int rows(void);
        int cols(void);

        void size(int &r, int &c) const;
};

template <typename T> Matrix<T>::Matrix(void){
    vec = 0;
    //resize(1, 1);
}

template <typename T> Matrix<T>::Matrix(int n, int m){
    vec = 0;
    resize(n, m);
}

template <typename T> Matrix<T>::Matrix(const Matrix<T> &original){
    int n = original.rows_, m = original.cols_;
    vec = 0;
    resize(n, m);
    for (int i = 0; i < n*m; i++) vec[i] = original.vec[i];
}

template <typename T> Matrix<T>::Matrix(int n, int m, const T *original){
    vec = 0;
    resize(n, m);
    for (int i = 0; i < n*m; i++) vec[i] = original[i];
}

template <typename T> Matrix<T>::~Matrix(){
    //if (vec != 0) free(vec);
    delete [] vec;
}

template <typename T> T& Matrix<T>::operator()(int i, int j){
    return vec[i*cols_ + j];
}

template <typename T> T& Matrix<T>::operator()(int i){
    return vec[i];
}

template <typename T> T* Matrix<T>::data(void){
    return vec;
}

template <typename T> Matrix<T> Matrix<T>::operator=(const Matrix<T> &original){
    int n = original.rows_, m = original.cols_;
    resize(n, m);
    for (int i = 0; i < n*m; i++) vec[i] = original.vec[i];
    return *this;
}

template <typename T> void Matrix<T>::resize(int newn, int newm){
    rows_ = newn;
    cols_ = newm;

//    if (vec == 0) vec = (T*)malloc(rows_*cols_*sizeof(T));
//    else          vec = (T*)realloc(vec, rows_*cols_*sizeof(T));
    if (vec == 0) vec = new T[rows_*cols_];
    else {
        T *temp0 = new T[rows_*cols_];
        for (int i = 0; i < rows_*cols_; i++) temp0[i] = vec[i];
        T *temp1 = vec;
        vec = temp0;
        temp0 = temp1;
        delete [] temp0;
    }

    return;
}

template <typename T> int Matrix<T>::rows(void){
    return rows_;
}

template <typename T> int Matrix<T>::cols(void){
    return cols_;
}

template <typename T> void Matrix<T>::size(int &r, int &c) const {
    r = rows_; 
    c = cols_; 
    return;
}

#endif // _MATRIX_

