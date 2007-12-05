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
#include "RealVector.h"
#include "JacobianMatrix.h"
#include "HessianMatrix.h"
#include "ReturnCodes.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class RarefactionFlow : WaveFlow {

private:

	static const int FIRST = 1;

	PhasePoint * referenceVector_;
	int familyIndex_;// = 1;

public:

	//! default constructor
	RarefactionFlow(void);

	//! reference vector constructor
	RarefactionFlow(const PhasePoint & phasePoint);

	//! reference vector and family index constructor
	RarefactionFlow(const PhasePoint & phasePoint,int familyIndex);

	//! the jet methods
	virtual int jet(const WaveState &u,JetMatrix &out,int degree);

	//! the reference vector accessor 
        PhasePoint & referenceVector() const;

        RarefactionFlow & operator=(const RarefactionFlow &source);

};


#endif //! _RarefactionFlow_H
