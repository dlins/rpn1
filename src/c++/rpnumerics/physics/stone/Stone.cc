/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Stone.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Stone.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Boundary * Stone::defaultBoundary() const {

    RealVector A(2);

    A.component(0) = 0;
    A.component(1) = 0;

    RealVector B(2);

    B.component(0) = 0;
    B.component(1) = 1;

    RealVector C(2);

    C.component(0) = 1;
    C.component(1) = 0;
    //

    //Saturacoes negativas

    //     A.component(0) = -1;
    //    A.component(1) = 2;
    //
    //    RealVector B(2);
    //
    //    B.component(0) = -1;
    //    B.component(1) = 2;
    //
    //    RealVector C(2);
    //
    //    C.component(0) = 2;
    //    C.component(1) = -1;



    //
    //      A.component(0) = -1;
    //    A.component(1) = -1;
    //
    //    RealVector B(2);
    //
    //    B.component(0) = -1;
    //    B.component(1) = 2;
    //
    //    RealVector C(2);
    //
    //    C.component(0) = 2;
    //    C.component(1) = -1;



    return new IsoTriang2DBoundary(A, B, C);


}

void Stone::setParams(vector<string> params) {


    RealVector fluxParamVector(7);

    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        double paramValue = atof(params[i].c_str());

        fluxParamVector.component(i) = paramValue;


    }

//    fluxParams(StoneParams(fluxParamVector));


    StoneFluxFunction & stoneFlux = (StoneFluxFunction&) fluxFunction();

    stoneFlux.fluxParams(StoneParams(fluxParamVector));

    RealVector permVector(13);

    cout << "Param de fluxo em stone" << fluxParamVector << endl;


    //Perm params
    for (int i = 7; i < params.size(); i++) {

        double paramValue = atof(params[i].c_str());

        permVector.component(i-7) = paramValue;


    }
    StonePermParams permParams(permVector);

    stoneFlux.setPermParams(permParams);


    cout << "Param de permeabilidade em stone" <<  permVector << endl;

}

Stone::Stone() : SubPhysics(StoneFluxFunction(StoneParams(), StonePermParams()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "Stone", _SIMPLE_ACCUMULATION_) {


    RealVector refVec(2);

    StoneHugoniotFunctionClass * stoneHugoniotFunction = new StoneHugoniotFunctionClass(refVec, (StoneFluxFunction(StoneParams(), StonePermParams())));


    setHugoniotFunction(stoneHugoniotFunction);

}

Stone::Stone(const Stone & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.boundary(), Multid::PLANE, "Stone", _SIMPLE_ACCUMULATION_) {
    RealVector refVec(2);
    StoneHugoniotFunctionClass * stoneHugoniotFunction = new StoneHugoniotFunctionClass(refVec, (StoneFluxFunction(StoneParams(), StonePermParams())));
    setHugoniotFunction(stoneHugoniotFunction);
}

SubPhysics * Stone::clone()const {
    return new Stone(*this);
}

Stone::~Stone() {
}


