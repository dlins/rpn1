#ifndef _BlockTriInf3_h
#define _BlockTriInf3_h
#ifdef __GNUC__
#pragma interface
#endif

#include "Vector3Array.h"

typedef Vector3Array BlockTriInfVector3;

#define BlockTriInfVector BlockTriInfVector3
#define BlockTriInfMatrix BlockTriInfMatrix3
#define MatrixNArray Matrix3Array

#include "BlockTriInfN.hP"

#undef BlockTriInfVector
#undef BlockTriInfMatrix
#undef MatrixNArray

#endif /* _BlockTriInf3_h */
