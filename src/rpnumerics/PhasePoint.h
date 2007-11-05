/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PhasePoint.h
 */

#ifndef _PhasePoint_H
#define _PhasePoint_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class PhasePoint : public RealVector {

public:
	PhasePoint(const PhasePoint & phasePoint);
	PhasePoint(const RealVector & phaseCoods);

	RealVector operator()(void);

};

inline PhasePoint::PhasePoint(const PhasePoint & copy)
	: RealVector(copy)
{
}


inline PhasePoint::PhasePoint(const RealVector & phaseCoords)
	: RealVector(phaseCoords)
{
}

inline RealVector PhasePoint::operator()(void)
{
	return * this;
}



#endif //! _PhasePoint_H
