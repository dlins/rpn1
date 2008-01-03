#ifndef _Vector3_h
#define _Vector3_h
#ifdef __GNUC__
#pragma interface
#endif

// Vector3.h:
//
// This file defines three-component vectors and matrices.
//
// Control macros:
//
//	Vector3_NO_CHECKING	-- turn off range checking


#include <math.h>
#include <stdlib.h>	// only needed for exit()
#include <iostream>
#include "except.h"
//#include "eigen.h"

using namespace std;

class Matrix3;

// Exceptions:

class RangeViolation3 : public exception { };

class Vector3 {
public:
    Vector3(void);
    Vector3(const Vector3 &vector);
    ~Vector3(void);
    Vector3 &operator=(const Vector3 &vector);
    Vector3 &zero(void);
    
    static int n_comps(void);
    static void range_check(int comp);
    
    double &operator()(int comp);
    double operator()(int comp) const;
    double &component(int comp);
    double component(int comp) const;
    
    Vector3 &operator*=(double s);
    Vector3 &operator+=(const Vector3 &b);
    Vector3 &operator-=(const Vector3 &b);
    
private:
    double v1, v2, v3;
    
    Vector3(double v1_, double v2_, double v3_);
    
    friend inline Vector3 operator*(double s, const Vector3 &a);
    friend inline Vector3 operator+(const Vector3 &a, const Vector3 &b);
    friend inline Vector3 operator-(const Vector3 &a, const Vector3 &b);
    friend inline Vector3 operator*(const Matrix3 &matrix,
            const Vector3 &vector);
    friend inline double operator*(const Vector3 &a, const Vector3 &b);
    friend inline istream &operator>>(istream &is, Vector3 &vector);
    friend inline ostream &operator<<(ostream &os, const Vector3 &vector);
    friend class Matrix3;	// for row() and column()
};

inline Vector3 operator*(double s, const Vector3 &a);
inline Vector3 operator+(const Vector3 &a, const Vector3 &b);
inline Vector3 operator-(const Vector3 &a, const Vector3 &b);
inline double operator*(const Vector3 &a, const Vector3 &b);

//
// TODO Use this lapack function
//
extern"C" {
    void dgeev_(char * , char *, int *, double *, int*, double *, double *,
            double *, int *, double *, int *, double *, int *,
            int *);
}

//
//  TODO Use this function ???
//
int eigen_rl(int , Matrix3 & , Vector3 &, Matrix3 &);//(int, double*, struct eigen*);

inline istream &operator>>(istream &is, Vector3 &vector);
inline ostream &operator<<(ostream &os, const Vector3 &vector);

inline int
        Vector3::n_comps(void) {
    return 3;
}

#ifndef Vector3_NO_CHECKING

inline void
        Vector3::range_check(int comp) {
    if (comp < 0 || comp >= Vector3::n_comps())
        THROW(RangeViolation3());
}

#else /* ! Vector3_NO_CHECKING */

inline void
        Vector3::range_check(int /* comp */) {
}

#endif /* Vector3_NO_CHECKING */

inline
        Vector3::Vector3(void) {
}

inline
        Vector3::Vector3(const Vector3 &vector)
        : v1(vector.v1),
                v2(vector.v2),
                v3(vector.v3) {
        }
        
        inline
                Vector3::Vector3(double v1_, double v2_, double v3_)
                : v1(v1_),
                        v2(v2_),
                        v3(v3_) {
                }
                
                inline
                        Vector3::~Vector3(void) {
                }
                
                inline Vector3 &
                        Vector3::operator=(const Vector3 &vector) {
                    v1 = vector.v1;
                    v2 = vector.v2;
                    v3 = vector.v3;
                    return *this;
                }
                
                inline Vector3 &
                        Vector3::zero(void) {
                    v1 = 0.;
                    v2 = 0.;
                    v3 = 0.;
                    return *this;
                }
                
                inline double &
                        Vector3::operator()(int comp) {
                    range_check(comp);
                    return (comp == 0) ? v1 : ( (comp == 1) ? v2 : v3 );
                }
                
                inline double
                        Vector3::operator()(int comp) const {
                    range_check(comp);
                    return (comp == 0) ? v1 : ( (comp == 1) ? v2 : v3 );
                }
                
                inline double &
                        Vector3::component(int comp) {
                    range_check(comp);
                    return (comp == 0) ? v1 : ( (comp == 1) ? v2 : v3 );
                }
                
                inline double
                        Vector3::component(int comp) const {
                    range_check(comp);
                    return (comp == 0) ? v1 : ( (comp == 1) ? v2 : v3 );
                }
                
                inline Vector3 &
                        Vector3::operator*=(double s) {
                    v1 *= s;
                    v2 *= s;
                    v3 *= s;
                    return *this;
                }
                
                inline Vector3 &
                        Vector3::operator+=(const Vector3 &b) {
                    v1 += b.v1;
                    v2 += b.v2;
                    v3 += b.v3;
                    return *this;
                }
                
                inline Vector3 &
                        Vector3::operator-=(const Vector3 &b) {
                    v1 -= b.v1;
                    v2 -= b.v2;
                    v3 -= b.v3;
                    return *this;
                }
                
                inline Vector3
                        operator*(double s, const Vector3 &a) {
                    return Vector3(s*a.v1, s*a.v2, s*a.v3);
                }
                
                inline Vector3
                        operator+(const Vector3 &a, const Vector3 &b) {
                    return Vector3(a.v1 + b.v1, a.v2 + b.v2, a.v3 + b.v3);
                }
                
                inline Vector3
                        operator-(const Vector3 &a, const Vector3 &b) {
                    return Vector3(a.v1 - b.v1, a.v2 - b.v2, a.v3 - b.v3);
                }
                
                inline double
                        operator*(const Vector3 &a, const Vector3 &b) {
                    return a.v1*b.v1 + a.v2*b.v2 + a.v3*b.v3;
                }
                
                inline istream &
                        operator>>(istream &is, Vector3 &vector) {
                    is >> vector.v1 >> vector.v2 >> vector.v3;
                    return is;
                }
                
                inline ostream &
                        operator<<(ostream &os, const Vector3 &vector) {
                    os << vector.v1 << " " << vector.v2 << " " << vector.v3;
                    return os;
                }
                
                class Matrix3 {
                public:
                        
                        Matrix3(void);
                        Matrix3(const Matrix3 &matrix);
                        ~Matrix3(void);
                        Matrix3 &operator=(const Matrix3 &matrix);
                        Matrix3 &zero(void);
                        
                        static void range_check(int row, int col);
                        
                        double &operator()(int row, int col);
                        double operator()(int row, int col) const;
                        
                        Vector3 row(int i);
                        Vector3 column(int j);
                        
                        Matrix3 &operator*=(double s);
                        Matrix3 &operator+=(const Matrix3 &b);
                        Matrix3 &operator-=(const Matrix3 &b);
                        
                        Matrix3 &equals_multiple_of_identity(double s);
                        Matrix3 &add_multiple_of_identity(double s);
                        
                        void eigen(Vector3 &evals, Matrix3 &evects);
                        
                private:
                        double e11, e12, e13, e21, e22, e23, e31, e32, e33;
                        
                        Matrix3(double e11_, double e12_, double e13_,
                                double e21_, double e22_, double e23_,
                                double e31_, double e32_, double e33_);
                        
                        friend inline Matrix3 operator*(double s, const Matrix3 &a);
                        friend inline Matrix3 operator+(const Matrix3 &a, const Matrix3 &b);
                        friend inline Matrix3 operator-(const Matrix3 &a, const Matrix3 &b);
                        friend inline Vector3 operator*(const Matrix3 &matrix,
                                const Vector3 &vector);
                        friend inline Matrix3 operator*(const Matrix3 &a, const Matrix3 &b);
                        friend inline Matrix3 inverse(const Matrix3 &matrix);
                        friend inline Matrix3 transpose(const Matrix3 &matrix);
                        friend inline Matrix3 deviator(const Matrix3 &matrix);
                        friend double trace(const Matrix3 &matrix);
                        friend double matrix_one_norm(const Matrix3 &matrix);
                        friend double matrix_infinity_norm(const Matrix3 &matrix);
                        friend inline istream &operator>>(istream &is, Matrix3 &matrix);
                        friend inline ostream &operator<<(ostream &os, const Matrix3 &matrix);
                };
                
                inline Matrix3 operator*(double s, const Matrix3 &a);
                inline Matrix3 operator+(const Matrix3 &a, const Matrix3 &b);
                inline Matrix3 operator-(const Matrix3 &a, const Matrix3 &b);
                inline Vector3 operator*(const Matrix3 &matrix, const Vector3 &vector);
                inline Matrix3 operator*(const Matrix3 &a, const Matrix3 &b);
                
                inline Matrix3 inverse(const Matrix3 &matrix);
                inline Matrix3 transpose(const Matrix3 &matrix);
                inline double trace(const Matrix3 &matrix);
                
                double matrix_one_norm(const Matrix3 &matrix);
                double matrix_infinity_norm(const Matrix3 &matrix);
                
                inline istream &operator>>(istream &is, Matrix3 &matrix);
                inline ostream &operator<<(ostream &os, const Matrix3 &matrix);
                
#ifndef Vector3_NO_CHECKING

inline void
        Matrix3::range_check(int row, int col) {
    if (row < 0 || row >= Vector3::n_comps())
        THROW(RangeViolation3());
    if (col < 0 || col >= Vector3::n_comps())
        THROW(RangeViolation3());
}

#else /* ! Vector3_NO_CHECKING */

inline void
        Matrix3::range_check(int /* row */, int /* col */) {
}

#endif /* Vector3_NO_CHECKING */

inline
        Matrix3::Matrix3(void) {
    
    
}

inline
        Matrix3::Matrix3(const Matrix3 &matrix)
        : e11(matrix.e11),
                e12(matrix.e12),
                e13(matrix.e13),
                e21(matrix.e21),
                e22(matrix.e22),
                e23(matrix.e23),
                e31(matrix.e31),
                e32(matrix.e32),
                e33(matrix.e33) {
            
            
            
        }
        
        inline
                Matrix3::Matrix3(double e11_, double e12_, double e13_,
                double e21_, double e22_, double e23_,
                double e31_, double e32_, double e33_)
                : e11(e11_),
                        e12(e12_),
                        e13(e13_),
                        e21(e21_),
                        e22(e22_),
                        e23(e23_),
                        e31(e31_),
                        e32(e32_),
                        e33(e33_) {
                    
                }
                
                inline
                        Matrix3::~Matrix3(void) {
                    
                }
                
                inline Matrix3 &
                        Matrix3::operator=(const Matrix3 &matrix) {
                    e11 = matrix.e11;
                    e12 = matrix.e12;
                    e13 = matrix.e13;
                    e21 = matrix.e21;
                    e22 = matrix.e22;
                    e23 = matrix.e23;
                    e31 = matrix.e31;
                    e32 = matrix.e32;
                    e33 = matrix.e33;
                    
                    return *this;
                }
                
                inline Matrix3 &
                        Matrix3::zero(void) {
                    e11 = 0.;
                    e12 = 0.;
                    e13 = 0.;
                    e21 = 0.;
                    e22 = 0.;
                    e23 = 0.;
                    e31 = 0.;
                    e32 = 0.;
                    e33 = 0.;
                    return *this;
                }
                
                inline double &
                        Matrix3::operator()(int row, int col) {
                    range_check(row, col);
                    return (row == 0) ? ((col == 0) ? e11 : ((col == 1) ? e12 : e13))
                    : ((row == 1) ? ((col == 0) ? e21 : ((col == 1) ? e22 : e23))
                    : ((col == 0) ? e31 : ((col == 1) ? e32 : e33)));
                    
                }
                
                inline double
                        Matrix3::operator()(int row, int col) const {
                    range_check(row, col);
                    return (row == 0) ? ((col == 0) ? e11 : ((col == 1) ? e12 : e13))
                    : ((row == 1) ? ((col == 0) ? e21 : ((col == 1) ? e22 : e23))
                    : ((col == 0) ? e31 : ((col == 1) ? e32 : e33)));
                    
                }
                
                inline Vector3
                        Matrix3::row(int i) {
                    Vector3::range_check(i);
                    if (i == 0)
                        return Vector3(e11, e12, e13);
                    else if (i == 1)
                        return Vector3(e21, e22, e23);
                    else
                        return Vector3(e31, e32, e33);
                }
                
                inline Vector3
                        Matrix3::column(int j) {
                    Vector3::range_check(j);
                    if (j == 0)
                        return Vector3(e11, e21, e31);
                    else if (j == 1)
                        return Vector3(e12, e22, e32);
                    else
                        return Vector3(e13, e23, e33);
                }
                
                inline Matrix3 &
                        Matrix3::operator*=(double s) {
                    e11 *= s;
                    e12 *= s;
                    e13 *= s;
                    e21 *= s;
                    e22 *= s;
                    e23 *= s;
                    e31 *= s;
                    e32 *= s;
                    e33 *= s;
                    return *this;
                }
                
                inline Matrix3 &
                        Matrix3::operator+=(const Matrix3 &b) {
                    e11 += b.e11;
                    e12 += b.e12;
                    e13 += b.e13;
                    e21 += b.e21;
                    e22 += b.e22;
                    e23 += b.e23;
                    e31 += b.e31;
                    e32 += b.e32;
                    e33 += b.e33;
                    return *this;
                }
                
                inline Matrix3 &
                        Matrix3::operator-=(const Matrix3 &b) {
                    e11 -= b.e11;
                    e12 -= b.e12;
                    e13 -= b.e13;
                    e21 -= b.e21;
                    e22 -= b.e22;
                    e23 -= b.e23;
                    e31 -= b.e31;
                    e32 -= b.e32;
                    e33 -= b.e33;
                    return *this;
                }
                
                inline Matrix3 &
                        Matrix3::equals_multiple_of_identity(double s) {
                    e11 = s;
                    e12 = 0.;
                    e13 = 0.;
                    e21 = 0.;
                    e22 = s;
                    e23 = 0.;
                    e31 = 0.;
                    e32 = 0.;
                    e33 = s;
                    return *this;
                }
                
                inline Matrix3 &
                        Matrix3::add_multiple_of_identity(double s) {
                    e11 += s;
                    e22 += s;
                    e33 += s;
                    return *this;
                }
                
                inline void
                        Matrix3::eigen(Vector3 &eigenValues /* evals */, Matrix3 &eigenVectors /* evects */) { //TODO Fix this function to use Lapack to calculate eigenvalues
                    
#ifndef LAPACKLINK
                    
                    
//    eigen_rl(3, *this,eigenValues, eigenVectors);
                    
                    
#else
                   
 cerr << "Matrix3::eigen() not yet implemented\n";
exit(-1);


#endif
                }
                
                inline Matrix3
                        operator*(double s, const Matrix3 &a) {
                    return Matrix3(s*a.e11, s*a.e12, s*a.e13,
                            s*a.e21, s*a.e22, s*a.e23,
                            s*a.e31, s*a.e32, s*a.e33);
                }
                
                inline Matrix3
                        operator+(const Matrix3 &a, const Matrix3 &b) {
                    return Matrix3(a.e11 + b.e11, a.e12 + b.e12, a.e13 + b.e13,
                            a.e21 + b.e21, a.e22 + b.e22, a.e23 + b.e23,
                            a.e31 + b.e31, a.e32 + b.e32, a.e33 + b.e33);
                }
                
                inline Matrix3
                        operator-(const Matrix3 &a, const Matrix3 &b) {
                    return Matrix3(a.e11 - b.e11, a.e12 - b.e12, a.e13 - b.e13,
                            a.e21 - b.e21, a.e22 - b.e22, a.e23 - b.e23,
                            a.e31 - b.e31, a.e32 - b.e32, a.e33 - b.e33);
                }
                
                inline Vector3
                        operator*(const Matrix3 &matrix, const Vector3 &vector) {
                    return Vector3(
                            matrix.e11*vector.v1 + matrix.e12*vector.v2 + matrix.e13*vector.v3,
                            matrix.e21*vector.v1 + matrix.e22*vector.v2 + matrix.e23*vector.v3,
                            matrix.e31*vector.v1 + matrix.e32*vector.v2 + matrix.e33*vector.v3);
                }
                
                inline Matrix3
                        operator*(const Matrix3 &a, const Matrix3 &b) {
                    return Matrix3(a.e11*b.e11 + a.e12*b.e21 + a.e13*b.e31,
                            a.e11*b.e12 + a.e12*b.e22 + a.e13*b.e32,
                            a.e11*b.e13 + a.e12*b.e23 + a.e13*b.e33,
                            a.e21*b.e11 + a.e22*b.e21 + a.e23*b.e31,
                            a.e21*b.e12 + a.e22*b.e22 + a.e23*b.e32,
                            a.e21*b.e13 + a.e22*b.e23 + a.e23*b.e33,
                            a.e31*b.e11 + a.e32*b.e21 + a.e33*b.e31,
                            a.e31*b.e12 + a.e32*b.e22 + a.e33*b.e32,
                            a.e31*b.e13 + a.e32*b.e23 + a.e33*b.e33);
                }
                
                inline Matrix3
                        inverse(const Matrix3 &matrix) {
                    double invdet = 1./(matrix.e11*matrix.e22*matrix.e33
                            + matrix.e12*matrix.e23*matrix.e31
                            + matrix.e13*matrix.e21*matrix.e32
                            - (matrix.e13*matrix.e22*matrix.e31
                            + matrix.e23*matrix.e32*matrix.e11
                            + matrix.e33*matrix.e12*matrix.e21));
                    return Matrix3( invdet*(matrix.e22*matrix.e33-matrix.e23*matrix.e32),
                            -invdet*(matrix.e12*matrix.e33-matrix.e13*matrix.e32),
                            invdet*(matrix.e12*matrix.e23-matrix.e13*matrix.e22),
                            -invdet*(matrix.e21*matrix.e33-matrix.e23*matrix.e31),
                            invdet*(matrix.e11*matrix.e33-matrix.e13*matrix.e31),
                            -invdet*(matrix.e11*matrix.e23-matrix.e13*matrix.e21),
                            invdet*(matrix.e21*matrix.e32-matrix.e22*matrix.e31),
                            -invdet*(matrix.e11*matrix.e32-matrix.e12*matrix.e31),
                            invdet*(matrix.e11*matrix.e22-matrix.e12*matrix.e21) );
                }
                
                inline Matrix3
                        transpose(const Matrix3 &matrix) {
                    return Matrix3(matrix.e11, matrix.e21, matrix.e31,
                            matrix.e12, matrix.e22, matrix.e32,
                            matrix.e13, matrix.e23, matrix.e33);
                }
                
                inline Matrix3
                        deviator(const Matrix3 &matrix) {
                    Matrix3 M(matrix);
                    M.add_multiple_of_identity(-trace(M)/3.);
                    return M;
                }
                
                inline double
                        trace(const Matrix3 &matrix) {
                    return matrix.e11 + matrix.e22 + matrix.e33;
                }
                
                inline double
                        matrix_one_norm(const Matrix3 &matrix) {
                    double norm1, norm2, norm3;
                    norm1 = fabs(matrix.e11) + fabs(matrix.e21) + fabs(matrix.e31);
                    norm2 = fabs(matrix.e12) + fabs(matrix.e22) + fabs(matrix.e32);
                    norm3 = fabs(matrix.e13) + fabs(matrix.e23) + fabs(matrix.e33);
                    norm1 = (norm1 >= norm2) ? norm1 : norm2;
                    norm1 = (norm1 >= norm3) ? norm1 : norm3;
                    return norm1;
                }
                
                inline double
                        matrix_infinity_norm(const Matrix3 &matrix) {
                    double norm1, norm2, norm3;
                    norm1 = fabs(matrix.e11) + fabs(matrix.e12) + fabs(matrix.e13);
                    norm2 = fabs(matrix.e21) + fabs(matrix.e22) + fabs(matrix.e23);
                    norm3 = fabs(matrix.e31) + fabs(matrix.e32) + fabs(matrix.e33);
                    norm1 = (norm1 >= norm2) ? norm1 : norm2;
                    norm1 = (norm1 >= norm3) ? norm1 : norm3;
                    return norm1;
                }
                
                inline istream &
                        operator>>(istream &is, Matrix3 &matrix) {
                    is >> matrix.e11 >> matrix.e12 >> matrix.e13;
                    is >> matrix.e21 >> matrix.e22 >> matrix.e23;
                    is >> matrix.e31 >> matrix.e32 >> matrix.e33;
                    return is;
                }
                
                inline ostream &
                        operator<<(ostream &os, const Matrix3 &matrix) {
                    os << matrix.e11 << " " << matrix.e12 << " " << matrix.e13 << "\n";
                    os << matrix.e21 << " " << matrix.e22 << " " << matrix.e23 << "\n";
                    os << matrix.e31 << " " << matrix.e32 << " " << matrix.e33 << "\n";
                    return os;
                }
                
                class FactorMatrix3 {
                public:
                        FactorMatrix3(void);
                        ~FactorMatrix3(void);
                        FactorMatrix3(const FactorMatrix3 &F);
                        FactorMatrix3 &operator=(const FactorMatrix3 &F);
                        
                private:
                        Matrix3 inverse;	// Unlike FactorMatrixN, FactorMatrix3 holds
                        // inverse matrix only.
                        
                        FactorMatrix3(const Matrix3 &matrix);	// Copies matrix.
                        // (It does not invert it.)
                        
                        friend FactorMatrix3 factor(const Matrix3 &matrix);
                        friend Vector3 solve(const FactorMatrix3 &F, const Vector3 &vector);
                        friend Matrix3 solve(const FactorMatrix3 &F, const Matrix3 &matrix);
                        friend inline ostream &operator<<(ostream &os, const FactorMatrix3 &F);
                };
                
                FactorMatrix3 factor(const Matrix3 &matrix);
                Vector3 solve(const FactorMatrix3 &F, const Vector3 &vector);
                Matrix3 solve(const FactorMatrix3 &F, const Matrix3 &matrix);
                
                inline ostream &operator<<(ostream &os, const FactorMatrix3 &F);
                
                inline
                        FactorMatrix3::FactorMatrix3(void)
                        : inverse() {
                        }
                        
                        inline FactorMatrix3::~FactorMatrix3(void) {
                        }
                        
                        inline
                                FactorMatrix3::FactorMatrix3(const FactorMatrix3 &F)
                                : inverse(F.inverse) {
                                }
                                
                                inline FactorMatrix3 &
                                        FactorMatrix3::operator=(const FactorMatrix3 &F) {
                                    inverse = F.inverse;
                                    return *this;
                                }
                                
                                inline
                                        FactorMatrix3::FactorMatrix3(const Matrix3 &matrix)
                                        : inverse(matrix) {
                                        }
                                        
                                        inline FactorMatrix3
                                                factor(const Matrix3 &matrix) {
                                            return FactorMatrix3(::inverse(matrix));
                                        }
                                        
                                        inline Vector3
                                                solve(const FactorMatrix3 &F, const Vector3 &vector) {
                                            return F.inverse * vector;
                                        }
                                        
                                        inline Matrix3
                                                solve(const FactorMatrix3 &F, const Matrix3 &matrix) {
                                            return F.inverse * matrix;
                                        }
                                        
                                        inline ostream &
                                                operator<<(ostream &os, const FactorMatrix3 &F) {
                                            os << F.inverse;
                                            return os;
                                        }
                                        
#endif /* _Vector3_h */
