#ifndef _Vector1Array_h
#define _Vector1Array_h
#ifdef __GNUC__
#pragma interface
#endif

#include "Vector1.h"
#include "ScalarArray.h"

#define VectorN Vector1
#define VectorNArray Vector1Array
#define MatrixN Matrix1
#define MatrixNArray Matrix1Array
#define MatrixNDerivative Matrix1Derivative
#define MatrixNDerivativeArray Matrix1DerivativeArray
#define SizeMismatchN SizeMismatch1

#include "VectorNArray.hP"

#undef VectorN
#undef VectorNArray
#undef MatrixN
#undef MatrixNArray
#undef MatrixNDerivative
#undef MatrixNDerivativeArray
#undef SizeMismatchN

#endif /* _Vector1Array_h */
