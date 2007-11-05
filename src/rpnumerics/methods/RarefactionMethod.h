/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.h
 **/

#ifndef _RarefactionMethod_H
#define _RarefactionMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpMethod.h"
#include "RpCurve.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionMethod : RpMethod{

private:
	RpCurve curve_;

public:
	RarefactionMethod();
	RpCurve curve();

};

#endif //! _RarefactionMethod_H
