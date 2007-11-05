/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2FluxFunction.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Quad2FluxFunction.h"
#include <math.h>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

Quad2FluxFunction::Quad2FluxFunction(const Quad2FluxParams & params) :
	params_(params)
{
}

int Quad2FluxFunction::jet(const int deriv, const WaveState & s, JetMatrix & m) 
{
	return OK;
}

FluxParams Quad2FluxFunction::fluxParams(void) 
{
	return params_;
}


