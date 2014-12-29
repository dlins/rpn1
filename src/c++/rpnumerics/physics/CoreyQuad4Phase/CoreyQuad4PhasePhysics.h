/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyQuad4PhasePhysics.h
 */

#ifndef _CoreyQuad4PhasePhysics_H
#define _CoreyQuad4PhasePhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CoreyQuad4Phase.h"
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneAccumulation.h"
#include "ShockMethod.h"
#include "Double_Contact.h"
//#include "Shock.h"
#include "StoneFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class CoreyQuad4PhasePhysics:public SubPhysics {

private:
    Boundary * defaultBoundary() const;

public:

    CoreyQuad4PhasePhysics();
    CoreyQuad4PhasePhysics(const CoreyQuad4PhasePhysics &);

    ~CoreyQuad4PhasePhysics();

    void setParams(vector<string>);
    vector<double> * getParams();

    SubPhysics * clone()const;
};

#endif //! _CoreyQuad4PhasePhysics_H
