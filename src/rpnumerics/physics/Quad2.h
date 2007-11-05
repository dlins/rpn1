/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad2_H
#define _Quad2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "Quad2FluxFunction.h"
//#include "AccumulationFunction.h"
#include "Quad2FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad2 : Physics {

private:
	Quad2FluxParams params_;
	Quad2FluxFunction fluxFunction_;
	//AccumulationFunction accumulationFunction_;
	Boundary * boundary_;

public:
	Quad2(const Quad2FluxParams &);

	const char * FLUX_ID;
	const char * DEFAULT_SIGMA;
	const char * DEFAULT_XZERO;
	const char * ID(void);

	const FluxParams & params(void) const;
	const FluxFunction & fluxFunction(void) const;
        //const AccumulationFunction & accumulation() const;
	const Space & domain(void) const;
	const Boundary & boundary(void) const;
	void boundary (const Boundary & boundary);
};

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

#endif //! _Quad2_H
