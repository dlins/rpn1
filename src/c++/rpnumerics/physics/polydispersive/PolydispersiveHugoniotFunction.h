/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersiveHugoniotFunction.h
 */

#ifndef _PolydispersiveHugoniotFunction_H
#define _PolydispersiveHugoniotFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Polydispersive.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class PolydispersiveHugoniotFunction : public HugoniotFunctionClass {
private:

    RealVector Uref;
    JetMatrix UrefJetMatrix;

    std::vector<eigenpair> ve_uref;

    bool Uref_is_elliptic;


public:


    PolydispersiveHugoniotFunction(const RealVector &U, const Polydispersive &);
    void setReferenceVector(const RealVector & refVec);
    ~PolydispersiveHugoniotFunction();

    double HugoniotFunction(const RealVector &u);



};

#endif //! _PolydispersiveHugoniotFunction_H
