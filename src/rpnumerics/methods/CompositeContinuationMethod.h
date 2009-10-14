/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CompositeContinuationMethod.h
 */

#ifndef _CompositeContinuationMethod_H
#define _CompositeContinuationMethod_H

#define INITIAL_CURVE (1) //TODO

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "LSODE.h"
#include "CompositeFlow.h"
#include "ContinuationRarefactionFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class CompositeContinuationMethod {
private:

    ODESolver * solver_;

public:

    CompositeContinuationMethod(const ODESolver &);

    void curve(const RealVector &, int, vector<RealVector> &);

};

#endif //! _CompositeContinuationMethod_H
