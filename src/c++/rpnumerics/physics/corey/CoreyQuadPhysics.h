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

    SubPhysics * clone()const;


};


inline Boundary * CoreyQuadPhysics::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = 0.0;
    min.component(1) = 0.0;

    RealVector max(2);

    max.component(0) = 1.0;
    max.component(1) = 1.0;

    return new Three_Phase_Boundary(min, max);
}

#endif //! _CoreyQuadPhysics_H
