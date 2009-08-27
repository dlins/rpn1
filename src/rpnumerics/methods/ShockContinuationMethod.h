/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ShockContinuationMethod.h
 */

#ifndef _ShockContinuationMethod_H
#define _ShockContinuationMethod_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "ContinuationShockFlow.h"
#include "LSODEProfile.h"
#include "LSODE.h"
#include <vector>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ShockContinuationMethod {
private:

    int shockinit(int n, double Um[], int family, int increase, double Unext[]);

    ODESolver * solver_;


public:

    ShockContinuationMethod(const ODESolver &);

    virtual ~ShockContinuationMethod();

    void curve(const RealVector &, int direction, vector<RealVector> &);

};

#endif //! _ShockContinuationMethod_H
