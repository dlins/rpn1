/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Triphase.h
 */

#ifndef _Triphase_H
#define _Triphase_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "IsoTriang2DBoundary.h"
#include  "CapilParams.h"
#include  "PermParams.h"
#include "ViscosityParams.h"
#include "TriPhaseParams.h"
#include  "TriPhaseFluxFunction.h"
#include  "Multid.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TriPhase : public Physics {
private:

    FluxFunction * fluxFunction_;
    Boundary * boundary_;
    AccumulationFunction * accFunction_;

    IsoTriang2DBoundary * defaultBoundary();

    char * FLUX_ID;



public:

    TriPhase(const TriPhaseParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);


    TriPhase(const TriPhase &);

    virtual ~TriPhase();

    Physics * clone()const;

    const char * ID() const;

    const AccumulationFunction & accumulation() const;

    const Boundary & boundary() const;

    void boundary(const Boundary & boundary);

    const FluxFunction & fluxFunction() const;
    
    void fluxParams (const FluxParams &);

    const Space & domain() const;


};

inline const FluxFunction & TriPhase::fluxFunction() const{
    return *fluxFunction_;
}

inline  void TriPhase::fluxParams (const FluxParams & params){
    
}

inline const Boundary & TriPhase::boundary() const {
    return *boundary_;
}

inline const AccumulationFunction & TriPhase::accumulation() const {
    return *accFunction_;
}

inline const Space & TriPhase::domain(void) const {
    return Multid::PLANE;
}

#endif //! _Triphase_H
