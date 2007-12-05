/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) DefaultAccumulationFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "DefaultAccumulationFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


inline DefaultAccumulationFunction::DefaultAccumulationFunction(void)
	: accumulationParams_(DefaultAccumulationParams())
{
}

inline int DefaultAccumulationFunction::f(const RealVector & u, RealVector & v)
{
	v = u;
	return OK;
}

inline int DefaultAccumulationFunction::df(const RealVector & u, JacobianMatrix & v)
{
	// TODO: To be implemented - daniel@impa.br
	//
	// JAVA CODE: daniel@impa.br
	//return new RealMatrix(U.size(),U.size());
	//v.equals_multiple_of_identity(1.);
	return OK;
}

inline int DefaultAccumulationFunction::d2f(const RealVector & u, HessianMatrix & v)
{
	v = * new HessianMatrix(u.size());
	return OK;
}

inline const DefaultAccumulationParams & DefaultAccumulationFunction::accumulationParams(void)
{
	return accumulationParams_;
}

inline void DefaultAccumulationFunction::accumulationParams(const DefaultAccumulationParams & accumulationParams)
{
	accumulationParams_ = accumulationParams;
}
