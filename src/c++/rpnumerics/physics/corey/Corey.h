/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Corey.h
 */

#ifndef _Corey_H
#define _Corey_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "IsoTriang2DBoundary.h"
#include  "CapilParams.h"
#include  "PermParams.h"
#include "ViscosityParams.h"
#include "CoreyParams.h"
#include  "CoreyFluxFunction.h"
#include "CoreyAccumulationFunction.h"
#include  "Multid.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class Corey : public SubPhysics {
private:

//    FluxFunction * fluxFunction_;
//    Boundary * boundary_;
//    AccumulationFunction * accFunction_;
//
//    IsoTriang2DBoundary * defaultBoundary();

//    char * FLUX_ID;



public:

    Corey(const CoreyParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);

    Corey(const Corey &);

    virtual ~Corey();

    Physics * clone()const;

    const char * ID() const;

    const Space & domain() const;
//
//    const AccumulationFunction & accumulation() const;
//
//    void accumulationParams(const AccumulationParams &);
//
//    const Boundary & boundary() const;
//
//    void boundary(const Boundary & boundary);
//
//    const FluxFunction & fluxFunction() const;
//
//    void fluxParams (const FluxParams &);
//



};

//inline const FluxFunction & Corey::fluxFunction() const{
//    return *fluxFunction_;
//}
//
//inline  void Corey::fluxParams (const FluxParams & params){
//
//    CoreyParams newParams(params.params(),0); //TODO index ???
//
//    fluxFunction_->fluxParams(newParams);
//}
//
//inline const Boundary & Corey::boundary() const {
//    return *boundary_;
//}
//
//inline const AccumulationFunction & Corey::accumulation() const {
//    return *accFunction_;
//}
//
//inline void Corey::accumulationParams(const AccumulationParams & params){
//    accFunction_->accumulationParams(params);
//
//}
//


inline const Space & Corey::domain(void) const {
    return Multid::PLANE;
}




#endif //! _Corey_H
