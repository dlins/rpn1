#ifndef _BlockTriInf2_h
#define _BlockTriInf2_h
#ifdef __GNUC__
#pragma interface
#endif

#include "Vector2Array.h"

typedef Vector2Array BlockTriInfVector2;

#define BlockTriInfVector BlockTriInfVector2
#define BlockTriInfMatrix BlockTriInfMatrix2
#define MatrixNArray Matrix2Array

#include "BlockTriInfN.hP"

#undef BlockTriInfVector
#undef BlockTriInfMatrix
#undef MatrixNArray

#endif /* _BlockTriInf2_h */
