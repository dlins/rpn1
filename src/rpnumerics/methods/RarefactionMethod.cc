/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionMethod.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RarefactionMethod.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

RarefactionMethod::RarefactionMethod() {
}

RpCurve RarefactionMethod::curve() {
	return curve_;
}
