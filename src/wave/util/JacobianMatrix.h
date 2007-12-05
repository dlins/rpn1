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
//#include "Vector.h"
#include "except.h"
#include "RealMatrix2.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class JacobianMatrix: public RealMatrix2
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


#endif //! _JacobianMatrix_H
