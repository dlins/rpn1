#ifndef _Vector2Array_h
#define _Vector2Array_h
#ifdef __GNUC__
#pragma interface
#endif

#include "Vector2.h"
#include "ScalarArray.h"

#define VectorN Vector2
#define VectorNArray Vector2Array
#define MatrixN Matrix2
#define MatrixNArray Matrix2Array
#define MatrixNDerivative Matrix2Derivative
#define MatrixNDerivativeArray Matrix2DerivativeArray
#define SizeMismatchN SizeMismatch2

#include "VectorNArray.hP"

#undef VectorN
#undef VectorNArray
#undef MatrixN
#undef MatrixNArray
#undef MatrixNDerivative
#undef MatrixNDerivativeArray
#undef SizeMismatchN

#endif /* _Vector2Array_h */
