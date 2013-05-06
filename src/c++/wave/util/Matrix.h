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

template <typename T>
class Matrix {
    private:

    protected:
        T *vec;
        int rows_, cols_;

        int min(int x, int y);

        void copy(int n, int m, const T *orig);

    public:
        Matrix();
        Matrix(int n, int m);
        Matrix(const Matrix<T> &original);
        Matrix(const Matrix<T> *original);
        Matrix(int n, int m, const T *original);
        ~Matrix();

        virtual T&       operator()(int i, int j);
        virtual T const& operator()(int i, int j) const;

        virtual T&       operator()(int i);
        virtual T const& operator()(int i) const;

        virtual T*       data(void);
        virtual T const* data(void) const;

        Matrix<T> operator=(const Matrix<T> &original);

        void resize(int newn, int newm);

        int rows(void) const;
        int cols(void) const;

        void size(int &r, int &c) const;
};

template <typename T> int Matrix<T>::min(int x, int y){
    return (x < y) ? x : y;
}

template <typename T> void Matrix<T>::copy(int n, int m, const T *orig){
    resize(n, m);

    for (int i = 0; i < n*m; i++) vec[i] = orig[i];

    return;
}

template <typename T> Matrix<T>::Matrix(){
    vec = 0;

    cols_ = rows_ = 0;
}

template <typename T> Matrix<T>::Matrix(int n, int m){
    vec = 0;

    resize(n, m);
}

template <typename T> Matrix<T>::Matrix(const Matrix<T> &original){
    vec = 0;

    copy(original.rows_, original.cols_, original.vec);
}

template <typename T> Matrix<T>::Matrix(const Matrix<T> *original){
    vec = 0;

    copy(original->rows_, original->cols_, original->vec);
}

template <typename T> Matrix<T>::Matrix(int n, int m, const T *original){
    vec = 0;

    copy(n, m, original);
}

template <typename T> Matrix<T>::~Matrix(){
    if (vec != 0) delete [] vec;
}

template <typename T> T& Matrix<T>::operator()(int i, int j){
    return vec[i*cols_ + j];
}

template <typename T> T const& Matrix<T>::operator()(int i, int j) const {
    return vec[i*cols_ + j];
}

template <typename T> T& Matrix<T>::operator()(int i){
    return vec[i];
}

template <typename T> T const& Matrix<T>::operator()(int i) const {
    return vec[i];
}

template <typename T> T* Matrix<T>::data(void){
    return &vec[0];
}

template <typename T> T const* Matrix<T>::data(void) const {
    return &vec[0];
}

template <typename T> Matrix<T> Matrix<T>::operator=(const Matrix<T> &original){
    if (this != &original) copy(original.rows_, original.cols_, original.vec);

    return *this;
}

template <typename T> void Matrix<T>::resize(int newn, int newm){
    if (newn == 0 || newm == 0) return;

    if (vec == 0) vec = new T[newn*newm];
    else {
        T *temp = new T[newn * newm];
        for (int i = 0; i < min(rows_, newn); i++){
            for (int j = 0; j < min(cols_, newm); j++){
                temp[i*newm + j] = vec[i*cols_ + j];
            }
        }

        delete [] vec;

        vec = temp;
    }

    rows_ = newn;
    cols_ = newm;

    return;
}

template <typename T> int Matrix<T>::rows(void) const {
    return rows_;
}

template <typename T> int Matrix<T>::cols(void) const {
    return cols_;
}

template <typename T> void Matrix<T>::size(int &r, int &c) const {
    r = rows_; 
    c = cols_; 
    return;
}

#endif // _MATRIX_

