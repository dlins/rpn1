/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) FluxParams.h
 */

#ifndef _FluxParams_H
#define	_FluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class FluxParams {

private:
	RealVector params_;
	RealVector initParams_;

public:
	FluxParams(void);
	FluxParams(const int size, const double *coords);
	FluxParams(const RealVector & params);
	FluxParams(const FluxParams & params);
	virtual ~FluxParams(void);

	const RealVector &params(void);
	const RealVector &operator()(void);
	const RealVector operator()(void) const;

	void params(int size, double * coords);
	void params(const RealVector & params);

	double component(int index);
	void component(int index, double value);

	//virtual FluxParams defaultParams(void) const = 0;

	bool operator==(const FluxParams & fluxParams);
	void reset(void);

};

inline FluxParams::FluxParams(void) :
	params_(),
	initParams_()
{
}

inline FluxParams::FluxParams(const RealVector & params) :
	params_(params),
	initParams_(params)
{
}

inline FluxParams::FluxParams(const FluxParams & params) :
	params_(params()),
	initParams_(params())
{
}

inline FluxParams::~FluxParams()
{
}

inline const RealVector & FluxParams::params(void)
{
	return params_;
}

inline const RealVector & FluxParams::operator()(void)
{
	return params_;
}

inline const RealVector FluxParams::operator()(void) const
{
	return params_;
}

inline void FluxParams::params(int size, double * coords) 
{ 
	RealVector p_(size, coords);
	params_ = p_;
}

inline void FluxParams::params(const RealVector & params)
{
	params_ = params;
}

inline double FluxParams::component(int index)
{
	return params_(index);
}

inline void FluxParams::component(int index, double value)
{
	params_(index) = value;
}

inline bool FluxParams::operator==(const FluxParams & fluxParams)
{ 
	if ( params_ == fluxParams() )
		return true;	
	else
		return false;
}

inline void FluxParams::reset(void)
{
	params_ = initParams_;
}

#endif	//! _FluxParams_H
