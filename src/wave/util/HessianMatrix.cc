/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HessianMatrix.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "HessianMatrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

inline HessianMatrix::HessianMatrix(void) : 
	n_comps_(1),
	size_(1),
	v_(1)
{
}

inline HessianMatrix::HessianMatrix(const int n_comps) : 
	n_comps_(n_comps),
	size_(n_comps * n_comps * n_comps),
	v_(size_)
{
}

inline HessianMatrix::HessianMatrix(const HessianMatrix & hessianMatrix) : 
	n_comps_(hessianMatrix.n_comps_),
	size_(n_comps_ * n_comps_ * n_comps_),
	v_(hessianMatrix.v_)
{
}

inline  int HessianMatrix::n_comps(void) const
{
	return n_comps_;
}

inline void HessianMatrix::resize(int n_comps)
{
	v_.resize(n_comps * n_comps * n_comps);
	n_comps_ = n_comps;
}

inline void HessianMatrix::range_check(int comp) const
{
	if (comp < 0 || comp >= n_comps())
		THROW(HessianMatrix::RangeViolation());
}

inline HessianMatrix & HessianMatrix::zero(void)
{
	v_.zero();
	return *this;
}

inline double * HessianMatrix::operator()(void)
{
	return v_.components();
}

double HessianMatrix::operator()(int i, int j, int k) const 
{
	range_check(i);
	range_check(j);
	range_check(k);
	return v_.component(i * n_comps_ * n_comps_ + j * n_comps_ + k);
}

inline void HessianMatrix::operator()(int i, int j, int k, double value)
{
	range_check(i);
	range_check(j);
	range_check(k);
	double * value_ = & v_.component(i * n_comps_ * n_comps_ + j * n_comps_ + k);
	*value_ = value;
}
