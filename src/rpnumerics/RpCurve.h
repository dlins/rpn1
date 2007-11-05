/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpCurve.h
 **/

#ifndef _RpCurve_H
#define _RpCurve_H

//! Definition of class RpCurve.
/*!
        The RpCurve class will hold the operations to be
realized upon a curve.

 TODO:
 NOTE :

 @ingroup rpnumerics
 */

class RpCurve {

private:

public:
	const RpCurve cat(const RpCurve & rpCurve);

};

inline const RpCurve RpCurve::cat(const RpCurve & rpCurve)
{
	// TODO: This method is empty!
	return rpCurve;
}

#endif //_RpCurve_H
