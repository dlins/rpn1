/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RarefactionFlow.h
 **/

#ifndef _RarefactionFlow_H
#define _RarefactionFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"
#include "PhasePoint.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionFlow : WaveFlow {

private:
	static const int FIRST = 1;
	static const int familyIndex_ = 1;
	PhasePoint referenceVector_;

public:
	RarefactionFlow(void);
	RarefactionFlow(const PhasePoint & phasePoint);
	RarefactionFlow(const RealVector phaseCoods);

	int f(const RealVector & u, RealVector & v);
	int df(const RealVector & u, JacobianMatrix & v);
	int d2f(const RealVector & u, HessianMatrix & v);

	// These functions just create aliases to f, df, d2f functions
	int flux(const RealVector & u, RealVector & v)      { return f(u, v); }
	int dflux(const RealVector & u, JacobianMatrix & v) { return df(u, v); }
	int d2flux(const RealVector & u, HessianMatrix & v) { return d2f(u, v); }

	const PhasePoint & referenceVector(void);

};

inline RarefactionFlow::RarefactionFlow() :
	referenceVector_(PhasePoint(RealVector()))
{
}

inline RarefactionFlow::RarefactionFlow(const PhasePoint & phasePoint) :
	referenceVector_(phasePoint)
{
}

inline RarefactionFlow::RarefactionFlow(const RealVector phaseCoods) :
	referenceVector_(PhasePoint(phaseCoods))
{
}

inline int RarefactionFlow::f(const RealVector & u, RealVector & v)
{
	// To be implemented by external C or Fortran code
	v = u;
	return OK;
}

inline int RarefactionFlow::df(const RealVector & u, JacobianMatrix & v)
{
	// To be implemented by external C or Fortran code
	//v.equals_multiple_of_identity(1.);
	return OK;
}

inline int RarefactionFlow::d2f(const RealVector & u, HessianMatrix & v)
{
	// To be implemented by external C or Fortran code
	//v = * new HessianMatrix(u.size());
	return OK;
}


inline const PhasePoint & RarefactionFlow::referenceVector(void)
{
	return referenceVector_;
}

#endif //! _RarefactionFlow_H
