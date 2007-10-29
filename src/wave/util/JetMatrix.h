/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JetMatrix.h
 */

#ifndef _JetMatrix_H
#define _JetMatrix_H

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


class JetMatrix
{
private:
	int n_comps_, size_;
	Vector v_;

	class RangeViolation : public exception { };
public:
	JetMatrix(void);
	JetMatrix(const int n_comps);
	JetMatrix(const JetMatrix & jetMatrix);

	int n_comps(void) const;
	void resize(int n_comps);
	void range_check(int comp) const;
	JetMatrix &zero(void);

	double * operator()(void);
	double operator()(int i);
	double operator()(int i, int j);
	double operator()(int i, int j, int k);

	void operator()(int i, double value);
	void operator()(int i, int j, double value);
	void operator()(int i, int j, int k, double value);

};

inline JetMatrix::JetMatrix(void) : 
	n_comps_(1),
	size_(3),
	v_(size_)
{
}

inline JetMatrix::JetMatrix(const int n_comps) : 
	n_comps_(n_comps),
	size_(n_comps * (1 + n_comps * (1 + n_comps))),
	v_(size_)
{
}

inline JetMatrix::JetMatrix(const JetMatrix & jetMatrix) : 
	n_comps_(jetMatrix.n_comps_),
	size_(n_comps_ * (1 + n_comps_ * (1 + n_comps_))),
	v_(jetMatrix.v_)
{
}

inline int JetMatrix::n_comps(void) const
{
	return n_comps_;
}

inline void JetMatrix::resize(int n_comps)
{
	v_.resize(n_comps * (1 + n_comps * (1 + n_comps)));
	n_comps_ = n_comps;
}

inline void JetMatrix::range_check(int comp) const
{
	if (comp < 0 || comp >= n_comps())
		THROW(JetMatrix::RangeViolation());
}

inline JetMatrix & JetMatrix::zero(void)
{
	v_.zero();
	return *this;
}

inline double * JetMatrix::operator()(void)
{
	return v_.components();
}

inline double JetMatrix::operator()(int i)
{
	range_check(i);
	return v_.component(i);
}

inline double JetMatrix::operator()(int i, int j)
{
	range_check(i);
	range_check(j);
	return v_.component((n_comps_) + (i*n_comps_ + j));
}

inline double JetMatrix::operator()(int i, int j, int k)
{
	range_check(i);
	range_check(j);
	range_check(k);
	return v_.component((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
}

inline void JetMatrix::operator()(int i, double value)
{
	range_check(i);
	double * value_ = & v_.component(i);
	*value_ = value;
}

inline void JetMatrix::operator()(int i, int j, double value)
{
	range_check(i);
	range_check(j);
	double * value_ = & v_.component((n_comps_) + (i*n_comps_ + j));
	*value_ = value;
}

inline void JetMatrix::operator()(int i, int j, int k, double value)
{
	range_check(i);
	range_check(j);
	range_check(k);
	double * value_ = & v_.component((n_comps_ * (1 + n_comps_)) + (i*n_comps_*n_comps_ + j*n_comps_ + k));
	*value_ = value;
}

#endif //! _JetMatrix_H
