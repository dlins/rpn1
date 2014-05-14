/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyQuadPhysics.h
 */

#ifndef _CoreyQuadPhysics_H
#define _CoreyQuadPhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CoreyQuad.h"
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneAccumulation.h"
#include "ShockMethod.h"
#include "Hugoniot_Curve.h"
#include "Double_Contact.h"
//#include "Shock.h"
#include "Stone_Explicit_Bifurcation_Curves.h"
#include "StoneFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CoreyQuadPhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;
Stone_Explicit_Bifurcation_Curves * stoneExplicitBifurcation_;

public:

    CoreyQuadPhysics();

    CoreyQuadPhysics(const CoreyQuadPhysics &);

    ~CoreyQuadPhysics();

    void setParams(vector<string>);
    vector<double> *  getParams();

    SubPhysics * clone()const;


};



#endif //! _CoreyQuadPhysics_H
