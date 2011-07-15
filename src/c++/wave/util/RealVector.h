#ifndef _REALVECTOR_
#define _REALVECTOR_

#include <iostream>
#include <fstream>

class RealVector {
private:
protected:
    double *data;
    int size_;

    int min(int x, int y);
public:
    RealVector(void);
    RealVector(int n);
    RealVector(int n, double *);
    RealVector(const RealVector &orig);
    RealVector(const RealVector *orig);

    ~RealVector(void);

    int size(void)const;
    void resize(int n);

    // Access to individual elements
    double & component(int n);

    const double & component(int n)const;

    operator double *(void);

    double operator()(int comp)const;


    double & operator()(int comp);

    // Access to data pointer
    double * components(void);

    // Assignment
    RealVector operator=(const RealVector &orig);

    // Output to stream
    friend std::ostream & operator<<(std::ostream &out, const RealVector &r);

    // Multiplication by a scalar
    friend RealVector operator*(const RealVector &r, double alpha);
    friend RealVector operator*(double alpha, const RealVector &r);

    // Sum with a scalar
    friend RealVector operator+(const RealVector &r, double alpha);
    friend RealVector operator+(double alpha, const RealVector &r);

    // Subtraction of/from a scalar
    friend RealVector operator-(const RealVector &r, double alpha);
    friend RealVector operator-(double alpha, const RealVector &r);

    // Negation
    friend RealVector operator-(const RealVector &r);

    // Sum of two RealVectors
    friend RealVector operator+(const RealVector &x, const RealVector &y);

    // Subtraction of two RealVectors
    friend RealVector operator-(const RealVector &x, const RealVector &y);
};

#endif // _REALVECTOR_

