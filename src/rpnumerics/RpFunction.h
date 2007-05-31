/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpFunction.h
 **/

#ifndef RPFUNCTION_H
#define RPFUNCTION_H


/*
** ---------------------------------------------------------------
** Includes:
*/
#include "Polygon.h"
#include "Vector3.h"
#include "Vector4.h"

/*
** ---------------------------------------------------------------
** Definitions:
*/

//! Definition of class RpFunction. 
/*!
	The RpFunction class defines a generic derivable function prototype. Derived classes
can choose the proper class to fit as a property of derivative order.

TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunction {

public:

	//! function evaluation at U
	int f(const RealVector &U,RealVector &out);

};

//! Definition of class RpFunctionDeriv. 
/*!
	The RpFunctionDeriv class defines a first order derivative for generic porpouses.

TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunctionDeriv {

public:

	//! first order derivative at U
	int df(const RealVector &U,Jacobian &out);

};


//! Definition of class RpFunctionDeriv2. 
/*!
	The RpFunctionDeriv2 class defines a second order derivative for generic porpouses.

TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunctionDeriv2 {

public:

	//! second order derivative at U
	int d2f(const RealVector &U,Hessian &out);

};

#endif //RPFUNCTION_H
