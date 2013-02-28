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



/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CoreyQuadPhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;


public:

    CoreyQuadPhysics();

    CoreyQuadPhysics(const CoreyQuadPhysics &);

    ~CoreyQuadPhysics();

    void setParams(vector<string>);
    vector<double> *  getParams();

    SubPhysics * clone()const;


};



#endif //! _CoreyQuadPhysics_H
