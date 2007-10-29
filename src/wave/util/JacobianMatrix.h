/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JacobianMatrix.h
 */

#ifndef _JacobianMatrix_H
#define _JacobianMatrix_H

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


class JacobianMatrix
{
private:
	int n_comps_, size_;
	Vector v_;

	class RangeViolation : public exception { };
public:
	JacobianMatrix(void);
	JacobianMatrix(const int n_comps);
	JacobianMatrix(const JacobianMatrix & jacobianMatrix);

	int n_comps(void) const;
	void resize(int n_comps);
	void range_check(int comp) const;
	JacobianMatrix &zero(void);

	double * operator()(void);
	double operator()(int i, int j);
	void operator()(int i, int j, double value);

};

inline JacobianMatrix::JacobianMatrix(void) : 
	n_comps_(1),
	size_(1),
	v_(1)
{
}

inline JacobianMatrix::JacobianMatrix(const int n_comps) : 
	n_comps_(n_comps),
	size_(n_comps * n_comps),
	v_(size_)
{
}

inline JacobianMatrix::JacobianMatrix(const JacobianMatrix & jacobianMatrix) : 
	n_comps_(jacobianMatrix.n_comps_),
	size_(n_comps_ * n_comps_),
	v_(jacobianMatrix.v_)
{
}

inline int JacobianMatrix::n_comps(void) const
{
	return n_comps_;
}

inline void JacobianMatrix::resize(int n_comps)
{
	v_.resize(n_comps * n_comps);
	n_comps_ = n_comps;
}

inline void JacobianMatrix::range_check(int comp) const
{
	if (comp < 0 || comp >= n_comps())
		THROW(JacobianMatrix::RangeViolation());
}

inline JacobianMatrix & JacobianMatrix::zero(void)
{
	v_.zero();
	return *this;
}

inline double * JacobianMatrix::operator()(void)
{
	return v_.components();
}

inline double JacobianMatrix::operator()(int i, int j)
{
	range_check(i);
	range_check(j);
	return v_.component(i * n_comps_ + j);
}

inline void JacobianMatrix::operator()(int i, int j, double value)
{
	range_check(i);
	range_check(j);
	double * value_ = & v_.component(i * n_comps_ + j);
	*value_ = value;
}

#endif //! _JacobianMatrix_H
