/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Corey.h
 */

#ifndef _Corey_H
#define _Corey_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "IsoTriang2DBoundary.h"
#include  "CapilParams.h"
#include  "PermParams.h"
#include "ViscosityParams.h"
#include "CoreyParams.h"
#include  "CoreyFluxFunction.h"
#include "CoreyAccumulationFunction.h"
#include  "Multid.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Corey : public SubPhysics {
private:

public:

    Corey(const CoreyParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);

    Corey(const Corey &);

    virtual ~Corey();

    SubPhysics * clone()const;

   Boundary * defaultBoundary()const;

};

#endif //! _Corey_H
