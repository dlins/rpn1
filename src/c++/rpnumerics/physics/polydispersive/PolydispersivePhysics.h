/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PolydispersivePhysics.h
 */

#ifndef _PolydispersivePhysics_H
#define _PolydispersivePhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Polydispersive.h"
#include "SubPhysics.h"
#include "RectBoundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


//  SubPhysics(const FluxFunction &,const AccumulationFunction &,const Boundary &,const Space &,const char *,int );

class PolydispersivePhysics:public SubPhysics {

private:
 Boundary * defaultBoundary() const;


public:

    PolydispersivePhysics(const Polydispersive &, const Boundary &, const Space &);

      PolydispersivePhysics();

    PolydispersivePhysics(const PolydispersivePhysics &);
    virtual ~PolydispersivePhysics();

    SubPhysics * clone()const;


};


inline Boundary * PolydispersivePhysics::defaultBoundary() const{

    RealVector min(2);

    min.component(0) = -10.0;
    min.component(1) = -10.0;

    RealVector max(2);

    max.component(0) = 10.0;
    max.component(1) = 10.0;

    return new RectBoundary(min, max);
}

#endif //! _PolydispersivePhysics_H
