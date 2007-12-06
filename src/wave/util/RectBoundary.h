/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RectBoundary.h
 */

#ifndef _RectBoundary_H
#define _RectBoundary_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Boundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RectBoundary : public Boundary {

private:
	RealVector * minimums_;
	RealVector * maximums_;
	int size_;

public:

	RectBoundary(void);
        RectBoundary(const RectBoundary &);
        virtual ~RectBoundary();
        
	RectBoundary(const RealVector & minimums, const RealVector & maximums);
        RectBoundary & operator =(const RectBoundary &);
        

	double coordinateSpan(int i);
	bool inside(const RealVector &y) const;
	const RealVector & minimums(void) const;
	const RealVector & maximums(void) const;
	const RealVector intersect(RealVector & y1, RealVector & y2) const;
};




#endif //! _RectBoundary_H
