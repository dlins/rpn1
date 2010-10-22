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
#include "AccumulationFunction.h"
#include "Multid.h"
#include "StoneFluxFunction.h"

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

    //Stone(const StoneParams &);
    Stone(const Stone &);
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


inline const FluxFunction & Stone::fluxFunction() const{
    return *fluxFunction_;
}

inline void Stone::fluxParams (const FluxParams & params){
    StoneParams newParams(params.params()); 
    fluxFunction_->fluxParams(newParams);
}

inline const Boundary & Stone::boundary() const {
    return *boundary_;
}

inline const AccumulationFunction & Stone::accumulation() const {
    return *accFunction_;
}

inline void Stone::accumulationParams(const AccumulationParams & params){
    accFunction_->accumulationParams(params);
}

inline const Space & Stone::domain(void) const {
    return Multid::PLANE;
}


#endif //! _Stone_H
