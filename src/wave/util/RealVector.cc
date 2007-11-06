#include "RealVector.h"

RealVector::RealVector(void) :
	Vector(2)
{
}

RealVector::RealVector(int size) :
	Vector(size)
{
}

RealVector::RealVector(int size, double * coords) :
	Vector(size, coords)
{
}

bool RealVector::operator ==(const RealVector &test)
{
        int i ;
        for (i=0;i < size();i++)
		if (test.component(i)!=component(i)) return false;
        return true;
}

//
// No need for accessor and mutator methods! Use 'Vector' methods to access components.
//
//void RealVector::setVal(int vindx [], double val){
//}
//
//double RealVector::getVal(int vindex []) const{
//}
