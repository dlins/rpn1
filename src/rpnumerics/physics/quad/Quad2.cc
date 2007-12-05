#include "Quad2.h"


Quad2::Quad2(const Quad2FluxParams & params) 
	: params_(params),
	  fluxFunction_(params),
	  boundary_(new RectBoundary())
	  //accumulationFunction_(new DefaultAccFunction())
{
	FLUX_ID = "QuadraticR2";
	DEFAULT_SIGMA = "-.021";
	DEFAULT_XZERO = ".13 .07";
}

const char * Quad2::ID(void) 
{
	return FLUX_ID;
}

inline const FluxParams & Quad2::params(void) const
{
	return params_;
}

inline const FluxFunction & Quad2::fluxFunction(void) const
{
	return fluxFunction_;
}

/*
inline const AccumulationFunction & accumulation() const
{
	return accumulationFunction_;
}
*/

inline const Space & Quad2::domain(void) const
{
	return Multid::PLANE;
}

inline const Boundary & Quad2::boundary(void) const
{
	return *boundary_;
}

inline void Quad2::boundary(const Boundary & boundary)
{
	//TODO: not working properly - daniel@impa.br
	*boundary_ = boundary;
}

