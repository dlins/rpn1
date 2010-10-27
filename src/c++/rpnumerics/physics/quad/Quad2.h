/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad2_H
#define _Quad2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Quad2FluxFunction.h"
#include "Quad2AccumulationFunction.h"
#include "Quad2FluxParams.h"
#include "RectBoundary.h"

class Quad2 : public SubPhysics {
private:

    const char * DEFAULT_SIGMA;
    
    const char * DEFAULT_XZERO;

public:
    Quad2(const Quad2FluxParams &);

    Quad2(const Quad2&);

    virtual ~Quad2();

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;

};

inline SubPhysics * Quad2::clone()const {
    return new Quad2(*this);
}


inline Boundary * Quad2::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = -10.0;
    min.component(1) = -10.0;

    RealVector max(2);

    max.component(0) = 10.0;
    max.component(1) = 10.0;

    return new RectBoundary(min, max);

}


#endif //! _Quad2_H
