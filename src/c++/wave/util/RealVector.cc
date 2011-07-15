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

double & RealVector::component(int n) {
    if (n < size_) return data[n];
}


// Access to individual elements

const double & RealVector::component(int n)const {
    if (n < size_) return data[n];
}

// Access to data pointer

double * RealVector::components(void) {
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

RealVector::operator double *(void) {
    return data;
}

double RealVector::operator()(int comp) const {
    return data[comp];
}


double & RealVector::operator()(int comp) {
    return data[comp];
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

