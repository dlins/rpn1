/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HessianMatrix.h
 */

#ifndef _HessianMatrix_H
#define _HessianMatrix_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Vector.h"
#include "except.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class HessianMatrix
{
private:
	int n_comps_, size_;
	Vector v_;

	class RangeViolation : public exception { };
public:
	HessianMatrix(void);
	HessianMatrix(const int n_comps);
	HessianMatrix(const HessianMatrix & hessianMatrix);

	int n_comps(void) const;
	void resize(int n_comps);
	void range_check(int comp) const;
	HessianMatrix &zero(void);

	double * operator()(void) ;
	double operator()(int i, int j, int k) const ;
	void operator()(int i, int j, int k, double value);

};


#endif //! _HessianMatrix_H
