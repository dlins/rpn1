/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneNegative.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "NegativeStone.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


Boundary * NegativeStone::defaultBoundary() const {


//    return new Three_Phase_Boundary();



        RealVector pmin(2);
    
        pmin.component(0) = -1;
        pmin.component(1) = 0;
    
        RealVector pmax(2);
    
        pmax.component(0) = 1.0;
        pmax.component(1) = 1.0;

    
    
return     new Three_Phase_Boundary (pmin,pmax);
    
    //    RealVector pmin(2);
    //
    //    pmin.component(0) = 0;
    //    pmin.component(1) = 0;
    //
    //    RealVector pmax(2);
    //
    //    pmax.component(0) = 1.0;
    //    pmax.component(1) = 1.0;

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






}

void NegativeStone::setParams(vector<string> params) {


    RealVector fluxParamVector(7);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {

        std::stringstream stream(params[i]);

        stream >> paramValue;

        fluxParamVector.component(i) = paramValue;

    }

    StoneNegativeFluxFunction & stoneFlux = (StoneNegativeFluxFunction&) fluxFunction();

    stoneFlux.fluxParams(StoneParams(fluxParamVector));

    //Perm params
    RealVector permVector(20);

    for (int i = 7; i < params.size(); i++) {
        std::stringstream stream(params[i]);

        stream >> paramValue;


        permVector.component(i - 7) = paramValue;


    }
    StoneNegativePermParams permParams(permVector);

    stoneFlux.setPermParams(permParams);

}

vector<double> * NegativeStone::getParams() {



    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {


        paramsVector->push_back(fluxFunction_->fluxParams().component(i));


    }
    StoneNegativeFluxFunction & stoneFluxFunction = (StoneNegativeFluxFunction &) fluxFunction();



    for (int i = 0; i < stoneFluxFunction.perm().params().params().size(); i++) {
        paramsVector->push_back(stoneFluxFunction.perm().params().params().component(i));
    }

    return paramsVector;


}

NegativeStone::NegativeStone() : SubPhysics(StoneNegativeFluxFunction(StoneParams(), StoneNegativePermParams()), StoneAccumulation(), *defaultBoundary(), Multid::PLANE, "StoneNegative", _SIMPLE_ACCUMULATION_) {
    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(fluxFunction_, accumulationFunction_, &getBoundary());
    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());
    shockCurve_ = new ShockCurve(hugoniot_continuation_method_);
    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);

  
    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();

}

NegativeStone::NegativeStone(const NegativeStone & copy) : SubPhysics(copy.fluxFunction(), copy.accumulation(), copy.getBoundary(), Multid::PLANE, "StoneNegative", _SIMPLE_ACCUMULATION_) {
    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(fluxFunction_, accumulationFunction_, &getBoundary());
    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());
    shockCurve_ = new ShockCurve(hugoniot_continuation_method_);
    compositeCurve_ = new CompositeCurve(accumulationFunction_, fluxFunction_, &getBoundary(), shockCurve_, 0);


    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(copy.getViscosityMatrix());
    preProcessedBoundary_ = copy.getPreProcessedBoundary()->clone();

}

SubPhysics * NegativeStone::clone()const {
    return new NegativeStone(*this);
}

NegativeStone::~NegativeStone() {
}

