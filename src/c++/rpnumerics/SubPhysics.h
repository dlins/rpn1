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
#include "HugoniotFunctionClass.h"
#include "Boundary.h"
#include "Space.h"
#include  "Multid.h"
#include "eigen.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class SubPhysics {

private:

    FluxFunction * fluxFunction_;
    AccumulationFunction * accumulationFunction_;
    HugoniotFunctionClass * hugoniotFunction_;
    Boundary * boundary_;
    Space * space_;
    const char * ID_;
    int type_;

public:

    SubPhysics(const FluxFunction &,const AccumulationFunction &,const Boundary &,const Space &,const char *,int );

    void fluxParams(const FluxParams &);

    void accumulationParams(const AccumulationParams &);

    const AccumulationFunction & accumulation() const;

    const Boundary & boundary() const;

    void boundary(const Boundary &);

    const FluxFunction & fluxFunction() const;

    HugoniotFunctionClass * getHugoniotFunction() const;

    void setHugoniotFunction(HugoniotFunctionClass *);

    const Space & domain() const;

    const char * ID() const;

    virtual SubPhysics * clone()const=0;

    virtual Boundary * defaultBoundary()const = 0;

    virtual ~SubPhysics();

    const int type() const;

};



#endif //! _SubPhysics_H
