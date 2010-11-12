/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Corey.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Corey.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */




Corey::Corey(const CoreyParams & params, const PermParams & permParams,
        const CapilParams & capilParams, const ViscosityParams & viscParams) : SubPhysics(CoreyFluxFunction(params, permParams, capilParams, viscParams), CoreyAccumulationFunction(), *defaultBoundary(),Multid::PLANE,"Corey") {




}

Boundary * Corey::defaultBoundary()const {

    RealVector A(2);

    A.component(0) = 0;
    A.component(1) = 0;

    RealVector B(2);

    B.component(0) = 0;
    B.component(1) = 1;

    RealVector C(2);

    C.component(0) = 1;
    C.component(1) = 0;


    return new IsoTriang2DBoundary(A, B, C);


}

Corey::Corey(const Corey & copy):SubPhysics(copy.fluxFunction(),copy.accumulation(),copy.boundary(),copy.domain(),"Corey") {

}

SubPhysics * Corey::clone()const {
    return new Corey(*this);
}

Corey::~Corey() {


}




