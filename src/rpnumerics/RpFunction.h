/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpFunction.h
 **/

#ifndef RPFUNCTION_H
#define RPFUNCTION_H


#define RP_SUCCESSFUL 1

/*
** ---------------------------------------------------------------
** Includes:
*/
#include "RealVector.h"
#include "RealMatrix.h"

/*
** ---------------------------------------------------------------
** Definitions:
*/

//! Definition of class RpFunction. 
/*!
	The RpFunction class defines a generic function prototype. 
	
	f:u C Rm -> v C Rn 

TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunction {

public:

	//! m coordinates function evaluation at u
	virtual int f(const RealVector &u,RealVector &v);
};

//! Definition of class RpFunctionDeriv. 
/*!
	The RpFunctionDeriv class defines a first order derivative for generic porpouses. We will
assume that a function with its first order derivative must have an f function obviously.


TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunctionDeriv : RpFunction {

public:

	//! first order derivative at u
	virtual int df(const RealVector &u,Jacobian &v) = 0;

};


//! Definition of class RpFunctionDeriv2. 
/*!
	The RpFunctionDeriv2 class defines a second order derivative for generic porpouses. We will
assume that a function with its second order derivative must have its first order derivative too.

TODO:
NOTE : 

@ingroup rpnumerics
*/
class RpFunctionDeriv2 : RpFunctionDeriv {

public:

	//! second order derivative at u
	virtual int d2f(const RealVector &u,Hessian &v) = 0;

};

#endif //RPFUNCTION_H
