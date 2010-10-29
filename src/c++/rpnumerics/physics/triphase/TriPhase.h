/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Triphase.h
 */

#ifndef _Triphase_H
#define _Triphase_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include  "CapilParams.h"
#include  "PermParams.h"
#include "ViscosityParams.h"
#include "TriPhaseParams.h"
#include  "TriPhaseFluxFunction.h"
#include "TriPhaseAccumulationFunction.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TriPhase : public SubPhysics {
private:

    FluxFunction * fluxFunction_;
    Boundary * boundary_;
    AccumulationFunction * accFunction_;



public:

    TriPhase(const TriPhaseParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);

    TriPhase(const TriPhase &);

    virtual ~TriPhase();

    SubPhysics * clone()const;


    Boundary * defaultBoundary()const;

};

#endif //! _Triphase_H
