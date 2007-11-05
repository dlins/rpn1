
#include "Quad2FluxParams.h"

const double DEFAULT_A[2] = { 0., 0. };
const double DEFAULT_B[2][2] = { { 0., .1 }, { -.1, 0. } };
const double DEFAULT_C[2][2][2] = { { { -1., 0. }, { 0., 1. } }, { { 0., 1. }, { 1., 0. } } };


Quad2FluxParams::Quad2FluxParams(void) :
	FluxParams(RealVector()) 
{
}

Quad2FluxParams::Quad2FluxParams(const RealVector & params) :
	FluxParams(params) 
{
}

Quad2FluxParams::Quad2FluxParams(const Quad2FluxParams & copy) :
	FluxParams(copy)
{
}	
