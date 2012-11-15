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




#endif //! _CUB2_H
