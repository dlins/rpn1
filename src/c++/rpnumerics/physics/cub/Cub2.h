/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Cub2.h
 **/

#ifndef _CUB2_H
#define _CUB2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "SubPhysics.h"
#include "Cub2FluxFunction.h"
#include "Cub2AccumulationFunction.h"
#include "CubHugoniotFunction.h"
#include "Cub2FluxParams.h"
#include "RectBoundary.h"

class Cub2 : public SubPhysics {
private:

    const char * DEFAULT_SIGMA;
    
    const char * DEFAULT_XZERO;

public:
    Cub2(const Cub2FluxParams &);

    Cub2(const Cub2&);

    virtual ~Cub2();

    SubPhysics * clone() const;

    Boundary * defaultBoundary()const;

};

inline SubPhysics * Cub2::clone()const {
    return new Cub2(*this);
}


inline Boundary * Cub2::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = -10.0;
    min.component(1) = -10.0;

    RealVector max(2);

    max.component(0) = 10.0;
    max.component(1) = 10.0;

    return new RectBoundary(min, max);

}


#endif //! _CUB2_H
