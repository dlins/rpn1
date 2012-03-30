/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydisperseHugoniotFunction.h
 */

#ifndef _PolydisperseHugoniotFunction_H
#define _PolydisperseHugoniotFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Polydisperse.h"
#include "HugoniotFunctionClass.h"
#include <vector>
#include "eigen.h" // TODO: Find the place

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class PolydisperseHugoniotFunction : public HugoniotFunctionClass {
private:

    RealVector Uref;
    JetMatrix UrefJetMatrix;

    std::vector<eigenpair> ve_uref;

    bool Uref_is_elliptic;


public:


    PolydisperseHugoniotFunction(const RealVector &U, const Polydisperse &);
    void setReferenceVector(const RealVector & refVec);
    ~PolydisperseHugoniotFunction();

    double HugoniotFunction(const RealVector &u);



};

#endif //! _PolydisperseHugoniotFunction_H
