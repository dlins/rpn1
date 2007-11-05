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
	RealVector minimums_;
	RealVector maximums_;
	int size_;

public:

	RectBoundary(void);
	RectBoundary(RealVector minimums, RealVector maximums);

	double coordinateSpan(int i);
	bool inside(const RealVector &y) const;
	const RealVector minimums(void) const;
	const RealVector maximums(void) const;
	const RealVector intersect(RealVector & y1, RealVector & y2) const;
};


inline RectBoundary::RectBoundary(void)
{
	// Default Rectangular boundary has the following values for
	// minimums and maximums.
	double mins[2] = { -5, -5 };
	double maxs[2] = {  5,  5 };

	minimums_ = RealVector(2, mins);
	maximums_ = RealVector(2, maxs);
	size_ = 2;
}

inline RectBoundary::RectBoundary(RealVector minimums, RealVector maximums)
	: minimums_(minimums),
	  maximums_(maximums),
	  size_(minimums.size())
{
}

inline double RectBoundary::coordinateSpan(int i)
{
	return maximums_.component(i) - minimums_.component(i);
}

inline bool RectBoundary::inside(const RealVector & y) const
{
	// true if y inside rectangular boundary
	bool result = true;
	for (int i = 0; i < size_; i++)
		if ((y.component(i) < minimums_.component(i)) || (y.component(i) > maximums_.component(i)))
			result = false;

	return result;
}


inline const RealVector RectBoundary::minimums(void) const
{
	return minimums_;
}

inline const RealVector RectBoundary::maximums(void) const
{
	return maximums_;
}

inline const RealVector RectBoundary::intersect(RealVector & y1, RealVector & y2) const
{
	// returns a point for intersection of [y1,y2] segment with the boundary
	double ratio;
	RealVector vec1(size_);
	RealVector vec2(size_);
	RealVector result;

	for (int i = 0; i < size_; i++)
		if (y1.component(i) != y2.component(i)) {
			ratio = (minimums_.component(i) - y1.component(i)) / \
				(y2.component(i) - y1.component(i));

			if ((ratio >= 0) && (ratio <= 1)) {
				//vec1.set(y1);
				vec1 = y1;
				//
				// TODO
				// RealVector doesn't have 'scale', 'add' and 'setElement' methods.
				// In JAVA code RealVector is derivated from GVector from 'vecmath' package!
				// daniel@impa.br
				//
				//vec1.scale(1d- ratio);
				// vec2.set(y2);
				vec2 = y2;
				// vec2.scale(ratio);
				// vec2.add(vec1);
				// vec2.setElement(i, minimums_.component(i));
				if (inside(vec2))
					result = vec2;
			}

			ratio = (maximums_.component(i) - y1.component(i)) / \
				(y2.component(i) - y1.component(i));

			if ((ratio >= 0) && (ratio <= 1)) {
				//vec1.set(y1);
				vec1 = y1;
				//
				// TODO
				// RealVector doesn't have 'scale', 'add' and 'setElement' methods.
				// In JAVA code RealVector is derivated from GVector from 'vecmath' package!
				// daniel@impa.br
				//
				//vec1.scale(1d- ratio);
				//vec2.set(y2);
				vec2 = y2;
				//vec2.scale(ratio);
				//vec2.add(vec1);
				//vec2.setElement(i, maximums_.component(i));
				if (inside(vec2))
					result = vec2;
			}
		}
	return result;
}


#endif //! _RectBoundary_H
