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
#include "LSODESolver.h"
#include <vector>

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ShockContinuationMethod {
private:

    int shockinit(int n, double Um[], int family, int increase, double Unext[]);
    void fill_with_jet(const FluxFunction & flux_object, int n, double *in, int degree, double *F, double *J, double *H);

    ODESolver * solver_;
    Boundary * boundary_;
    int family_;


public:

    ShockContinuationMethod(const ODESolver &,const Boundary &,int );

    virtual ~ShockContinuationMethod();

    void curve(const RealVector &, int direction, vector<RealVector> &);

};

#endif //! _ShockContinuationMethod_H
