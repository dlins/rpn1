#include "RealVector.h"

inline int RealVector::min(int x, int y) {
    return (x < y) ? x : y;
}

RealVector::RealVector(void) {
    data = NULL;
    size_ = 0;
}

RealVector::RealVector(int n) : size_(n) {
    data = new double[n];
}

RealVector::RealVector(int n, double * values) : size_(n) {
    data = new double[n];
    for (int i = 0; i < n; i++) {
        data[i] = values[i];
    }


}

RealVector::RealVector(const RealVector &orig) : size_(orig.size_) {
    data = new double[orig.size_];
    for (int i = 0; i < orig.size_; i++) data[i] = orig.data[i];
}

RealVector::RealVector(const RealVector *orig) : size_(orig->size_) {
    data = new double[orig->size_];
    for (int i = 0; i < orig->size_; i++) data[i] = orig->data[i];
}

RealVector::~RealVector(void) {
    if (data != NULL) delete [] data;
}

int RealVector::size(void)const {
    return size_;
}

void RealVector::resize(int n) {
    if (size_ == 0) {
        data = new double[n];
        for (int i = 0; i < n; i++) data[i] = 0.0;
    } else {
        double *temp0 = new double[n];

        for (int i = 0; i < min(size_, n); i++) temp0[i] = data[i];

        delete [] data;
        data = temp0;
    }

    size_ = n;

    return;
}

// Access to individual elements
//
double & RealVector::component(int n) {
    if (n < size_) return data[n];
}

const double & RealVector::component(int n)const {
    if (n < size_) return data[n];
}

// Access to data pointer
//
double * RealVector::components(void) {
    return data;
}

const double * RealVector::components(void) const {
    return data;
}

// Assignment

RealVector RealVector::operator=(const RealVector &orig) {
    // Avoid self-assignment
    if (this != &orig) {
        int n = orig.size_;
        resize(n);
        for (int i = 0; i < n; i++) data[i] = orig.data[i];
    }

    return *this;
}

bool RealVector::operator==(const RealVector &other){
    if (size_ != other.size_) return false;

    for (int i = 0; i < other.size_; i++) {
        if (data[i] != other.data[i]) return false;
    }

    return true;
}

// Cast operator
RealVector::operator double *(void) {
    return data;
}

double RealVector::operator()(int comp) const {
    return data[comp];
}


double & RealVector::operator()(int comp) {
    return data[comp];
}

double RealVector::operator[](int comp) const {
    return data[comp];
}


double & RealVector::operator[](int comp) {
    return data[comp];
}

RealVector RealVector::zeroes(int m){
    RealVector z(m);
    for (int i = 0; i < m; i++) z(i) = 0.0;

    return z;
}

// Output to stream

std::ostream & operator<<(std::ostream &out, const RealVector &r) {
    out << "(";
    for (int i = 0; i < r.size_; i++) {
        out << r.data[i];
        if (i != r.size_ - 1) out << ", ";
    }
    out << ")";

    return out;
}

// Multiplication by a scalar

RealVector operator*(const RealVector &r, double alpha) {
    RealVector temp(r);
    for (int i = 0; i < r.size_; i++) temp.data[i] *= alpha;

    return temp;
}

RealVector operator*(double alpha, const RealVector &r) {
    return r*alpha;
}

// Division by a scalar
RealVector operator/(const RealVector &r, double alpha) {
    return r*(1.0/alpha);
}

// Sum with a scalar

RealVector operator+(const RealVector &r, double alpha) {
    RealVector temp(r);
    for (int i = 0; i < r.size_; i++) temp.data[i] += alpha;

    return temp;
}

RealVector operator+(double alpha, const RealVector &r) {
    return r + alpha;
}

// Subtraction of/from a scalar

RealVector operator-(const RealVector &r, double alpha) {
    RealVector temp(r);
    for (int i = 0; i < r.size_; i++) temp.data[i] -= alpha;

    return temp;
}

RealVector operator-(double alpha, const RealVector &r) {
    return r - alpha;
}

// Negation

RealVector operator-(const RealVector &r) {
    RealVector temp(r);
    for (int i = 0; i < r.size_; i++) temp.data[i] *= -1.0;

    return temp;
}

// Sum of two RealVectors

RealVector operator+(const RealVector &x, const RealVector &y) {
    RealVector temp(x);
    for (int i = 0; i < x.size_; i++) temp.data[i] += y.data[i];

    return temp;
}

// Subtraction of two RealVectors

RealVector operator-(const RealVector &x, const RealVector &y) {
    RealVector temp(x);
    for (int i = 0; i < x.size_; i++) temp.data[i] -= y.data[i];

    return temp;
}

// Norm of a RealVector
double norm(const RealVector &x){
    return sqrt(x*x);
}

// Normalize a RealVector
RealVector normalize(const RealVector &x){
    return RealVector(x/norm(x));
}

// Inner product of two RealVectors
double operator*(const RealVector &x, const RealVector &y){
    double p = 0.0;

    for (int i = 0; i < x.size(); i++) p += x(i)*y(i);

    return p;
}

// Vector product of two 3D RealVectors
RealVector vector_product(const RealVector &x, const RealVector &y){
    RealVector v(3);

    v(0) = x(1)*y(2) - x(2)*y(1);
    v(1) = x(2)*y(0) - x(0)*y(2);
    v(2) = x(0)*y(1) - x(1)*y(0);

    return v;
}

// Solve the system of linear equations A*x = b
int solve(const DoubleMatrix &A, const RealVector &b, RealVector &x){
    int n = A.rows();

    DoubleMatrix bb(n, 1), xx(n, 1);
    for (int i = 0; i < n; i++) bb(i, 0) = b(i);

    int info = solve(A, bb, xx);

    x.resize(n);
    for (int i = 0; i < n; i++) x(i) = xx(i, 0);

    if (info == 0) return REALVECTOR_SOLVE_LINEAR_SYSTEM_OK;
    else           return REALVECTOR_SOLVE_LINEAR_SYSTEM_ERROR;
}

// Multiplication of a DoubleMatrix by a RealVector
RealVector operator*(const DoubleMatrix &A, const RealVector &x){
    int m = A.rows(), n = A.cols();

    RealVector b(m);

    for (int i = 0; i < m; i++){
        b(i) = 0.0;
        for (int j = 0; j < n; j++) b(i) += A(i, j)*x(j);
    }

    return b;
}

