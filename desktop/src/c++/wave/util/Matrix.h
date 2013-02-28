#ifndef _MATRIX_
#define _MATRIX_

#include <stdlib.h>
#include <math.h>



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
using namespace std;
template <typename T>
class Matrix {
    private:
        T *vec;
        int rows_, cols_;

        int min(int x, int y);

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

template <typename T> int Matrix<T>::min(int x, int y){
    return (x < y) ? x : y;
}

template <typename T> Matrix<T>::Matrix(void){
    cols_ = rows_ = 0;
    vec = 0;
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
    if (vec != 0) delete [] vec;
}

template <typename T> T& Matrix<T>::operator()(int i, int j){
    return vec[i*cols_ + j];
}

template <typename T> T& Matrix<T>::operator()(int i){
    return vec[i];
}

template <typename T> T* Matrix<T>::data(void){
    return &vec[0];
}

template <typename T> Matrix<T> Matrix<T>::operator=(const Matrix<T> &original){
//    printf("m em igual %d\n",original.cols_);
//    printf("n em igual %d\n", original.rows_);


    int n = original.rows_, m = original.cols_;
    if (n != 0 && m != 0){
    resize(n, m);
    for (int i = 0; i < n*m; i++) vec[i] = original.vec[i];
    }
    return *this;
}

template <typename T> void Matrix<T>::resize(int newn, int newm){
//    printf("Matrix::resize: n = %d, m = %d\n", newn, newm);
    if (newn == 0 || newm == 0) return;

    if (vec == 0) vec = new T[newn*newm];
    else {
        T *temp0 = new T[newn * newm];
        for (int i = 0; i < min(rows_, newn); i++){
            for (int j = 0; j < min(cols_, newm); j++){
                temp0[i*newm + j] = vec[i*cols_ + j];
            }
        }
        T *temp1 = vec;
        vec = temp0;
        temp0 = temp1;
        delete []temp0;
    }

    rows_ = newn;
    cols_ = newm;

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

