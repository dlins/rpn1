#include "Quad2FluxParams.h"


inline Quad2FluxParams Quad2FluxParams::defaultParams(void)
{
	return Quad2FluxParams();
}


const double Quad2FluxParams::DEFAULT_A[2] = { 0., 0. };
const double Quad2FluxParams::DEFAULT_B[2][2] = { { 0., .1 }, { -.1, 0. } };
const double Quad2FluxParams::DEFAULT_C[2][2][2] = { { { -1., 0. }, { 0., 1. } }, { { 0., 1. }, { 1., 0. } } };


Quad2FluxParams::Quad2FluxParams(void) :FluxParams(RealVector(2)) 
{
}

Quad2FluxParams::Quad2FluxParams(const RealVector & params) :FluxParams(params) 
{
}



