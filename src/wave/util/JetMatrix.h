/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.h
 **/

#ifndef JETMATRIX_H
#define JETMATRIX_H


/*
** ---------------------------------------------------------------
** Includes:
*/

/*
** ---------------------------------------------------------------
** Definitions:
*/

//! Definition of class JetMatrix. 
/*!
	The JetMatrix class defines a generic n dimension data structure (rows, columns and...)
TODO:
NOTE : 

@ingroup util
*/
class JetMatrix {

public:
    
	//! the value accessor at vindx index
	virtual void setVal(int vindx [],double val)=0;
	//! the value mutator at vindx index
	virtual double getVal(int vindex []) const=0;

};


#endif //JETMATRIX_H
