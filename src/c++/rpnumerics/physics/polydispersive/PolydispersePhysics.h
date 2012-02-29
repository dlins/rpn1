/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersePhysics.h
 */

#ifndef _PolydispersePhysics_H
#define _PolydispersePhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Polydisperse.h"
#include "SubPhysics.h"
#include "RectBoundary.h"
#include "StoneAccumulation.h"
#include "PolydisperseHugoniotFunction.h"
#include "Quad2AccumulationFunction.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//  SubPhysics(const FluxFunction &,const AccumulationFunction &,const Boundary &,const Space &,const char *,int );

class PolydispersePhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;


public:

//    PolydispersePhysics(const Polydisperse &, const Boundary &, const Space &);

      PolydispersePhysics();

    PolydispersePhysics(const PolydispersePhysics &);
    virtual ~PolydispersePhysics();

    SubPhysics * clone()const;


};


inline Boundary * PolydispersePhysics::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = 0.0;
    min.component(1) = 0.0;

    RealVector max(2);

    max.component(0) = 1.0;
    max.component(1) = 1.0;

    return new RectBoundary(min, max);
}

#endif //! _PolydispersePhysics_H
