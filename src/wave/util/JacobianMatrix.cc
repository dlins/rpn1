/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JacobianMatrix.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "JacobianMatrix.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


 JacobianMatrix::JacobianMatrix(void) : RealMatrix2(1),
	n_comps_(1),
	size_(1),
	v_(1)
{
}

JacobianMatrix::JacobianMatrix(const int n_comps) : RealMatrix2(n_comps),
	n_comps_(n_comps),
	size_(n_comps * n_comps),
	v_(size_)
{
}

 JacobianMatrix::JacobianMatrix(const JacobianMatrix & jacobianMatrix) : RealMatrix2(jacobianMatrix.n_comps()),
	n_comps_(jacobianMatrix.n_comps_),
	size_(n_comps_ * n_comps_),
	v_(jacobianMatrix.v_)
{
}


