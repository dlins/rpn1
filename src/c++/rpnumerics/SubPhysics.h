/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) SubPhysics.h
 */

#ifndef _SubPhysics_H
#define _SubPhysics_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "FluxFunction.h"
#include "AccumulationFunction.h"
#include "Boundary.h"
#include "Space.h"
#include  "Multid.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class SubPhysics {

private:

    FluxFunction * fluxFunction_;
    AccumulationFunction * accumulationFunction_;
    Boundary * boundary_;
    Space * space_;
    const char * ID_;

public:

    SubPhysics(const FluxFunction &,const AccumulationFunction &,const Boundary &,const Space &,const char *);

    void fluxParams(const FluxParams &);

    void accumulationParams(const AccumulationParams &);

    const AccumulationFunction & accumulation() const;

    const Boundary & boundary() const;

    const FluxFunction & fluxFunction() const;

    const Space & domain() const;

    const char * ID() const;

    virtual SubPhysics * clone()const=0;

    virtual Boundary * defaultBoundary()const = 0;




};



#endif //! _SubPhysics_H
