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
    void fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);
    int cdgeev(int n, double *A, struct eigen *e) const;

    ODESolver * solver_;


public:

    ShockContinuationMethod(const ODESolver &);

    virtual ~ShockContinuationMethod();

    void curve(const RealVector &, int direction, vector<RealVector> &);

};

#endif //! _ShockContinuationMethod_H
