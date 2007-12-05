/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) DefaultAccumulationFunction.h
 */

#ifndef _DefaultAccumulationFunction_H
#define _DefaultAccumulationFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
//#include "RealVector.h"
//#include "JacobianMatrix.h"
//#include "HessianMatrix.h"
#include "AccumulationFunction.h"
#include "DefaultAccumulationParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class DefaultAccumulationFunction : AccumulationFunction {

private:
	DefaultAccumulationParams accumulationParams_;

public:
	DefaultAccumulationFunction(void);

	int f(const RealVector & u, RealVector & v);
	int df(const RealVector & u, JacobianMatrix & v);
	int d2f(const RealVector & u, HessianMatrix & v);
	
	// These functions just create aliases f, df, d2f functions
	int h(const RealVector & u, RealVector & v)      { return f(u, v); }
	int dh(const RealVector & u, JacobianMatrix & v) { return df(u, v); }
	int d2h(const RealVector & u, HessianMatrix & v) { return d2f(u, v); }

	const DefaultAccumulationParams & accumulationParams(void);
	void accumulationParams(const DefaultAccumulationParams & accumulationParams);

};

#endif //! _DefaultAccumulationFunction_H
