/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad4_H
#define _Quad4_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Quad4FluxFunction.h"
#include "Quad4AccumulationFunction.h"
#include "Quad4FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad4 : public SubPhysics {
private:



    char * FLUX_ID;

    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;


public:
    Quad4(const Quad4FluxParams &);

    Quad4(const Quad4&);

    virtual ~Quad4();

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;


};

inline SubPhysics * Quad4::clone()const {
    return new Quad4(*this);
}


inline Boundary * Quad4::defaultBoundary()const {

    RealVector min(4);

    min.component(0) = -0.5;
    min.component(1) = -0.5;
    min.component(2) = -0.5;
    min.component(3) = -0.5;

    RealVector max(4);

    max.component(0) = 0.5;
    max.component(1) = 0.5;
    max.component(2) = 0.5;
    max.component(3) = 0.5;

    return new RectBoundary(min, max);

}
#endif //! _Quad4_H
