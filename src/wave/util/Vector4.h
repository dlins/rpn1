#ifndef _Vector4_h
#define _Vector4_h
#ifdef __GNUC__
#pragma interface
#endif

#define VectorN_N_COMPS 4
#define VectorN Vector4
#define MatrixN Matrix4
#define FactorMatrixN FactorMatrix4
#define RangeViolationN RangeViolation4

#include <VectorN.hP>

#undef VectorN
#undef MatrixN
#undef FactorMatrixN

#endif /* _Vector4_h */
