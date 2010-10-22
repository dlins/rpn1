/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Stone.h
 */

#ifndef _Stone_H
#define _Stone_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "IsoTriang2DBoundary.h"
#include "StoneParams.h"
/*
 * ---------------------------------------------------------------
 * Definitions:
 */



class Stone:public Physics {
private:
    FluxFunction * fluxFunction_;
    Boundary * boundary_;
    AccumulationFunction * accFunction_;

    IsoTriang2DBoundary * defaultBoundary();

    char * FLUX_ID;

public:

    Stone(const StoneParams &);
    Stone();

    virtual ~Stone();

    Physics * clone()const;

    const char * ID() const;

    const AccumulationFunction & accumulation() const;

    void accumulationParams(const AccumulationParams &);

    const Boundary & boundary() const;

    void boundary(const Boundary & boundary);

    const FluxFunction & fluxFunction() const;

    void fluxParams(const FluxParams &);

    const Space & domain() const;

};

#endif //! _Stone_H
