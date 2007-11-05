/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WavePoint.h
 */

#ifndef _WavePoint_H
#define _WavePoint_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

/*!

TODO:
	To implement an exception in 'operator=()' method.
NOTE : 

@ingroup rpnumerics
*/

class WavePoint : public RealVector{

private:
	double sigma_;

public:
	WavePoint(const int size, const double sigma);
	WavePoint(const RealVector coords, const double sigma);
	WavePoint(const int size, double * coords, const double sigma);
	WavePoint(const WavePoint & copy);

	double sigma(void);
	void sigma(double sigma);
	void operator=(double sigma);
	void operator=(RealVector & coords);
	void operator=(WavePoint & copy);

};


WavePoint::WavePoint(const int size, const double sigma)
	: RealVector(size),
	  sigma_(sigma)
{
}

WavePoint::WavePoint(const RealVector coords, const double sigma)
	: RealVector(coords),
	  sigma_(sigma)
{
}

WavePoint::WavePoint(const int size, double * coords, const double sigma)
	: RealVector(size, coords),
	  sigma_(sigma)
{
}

WavePoint::WavePoint(const WavePoint & copy)
	: RealVector(copy),
	  sigma_(copy.sigma_)
{
}

double WavePoint::sigma(void)
{
	return sigma_;
}

void WavePoint::sigma(double sigma)
{
	sigma_ = sigma;
}

void WavePoint::operator=(double sigma)
{
	sigma_ = sigma;
}

void WavePoint::operator=(RealVector & coords)
{
	copy((Vector)coords);
}

void WavePoint::operator=(WavePoint & copy)
{
	ref(copy);
	sigma_ = copy.sigma();
}

#endif //! _WavePoint_H
