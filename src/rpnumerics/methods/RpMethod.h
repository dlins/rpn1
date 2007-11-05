/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpMethod.h
 **/

#ifndef _RpMethod_H
#define _RpMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpCurve.h"


class RpMethod {

public:
	virtual RpCurve curve() const = 0;
	virtual ~RpMethod();

};

inline RpMethod::~RpMethod() { };

#endif //_RpMethod_H
