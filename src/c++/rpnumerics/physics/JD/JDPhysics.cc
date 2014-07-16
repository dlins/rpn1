/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JDPhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "JDPhysics.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */



JDPhysics::JDPhysics() : SubPhysics(JDFluxFunction(), JDAccumulationFunction(), *defaultBoundary(), Multid::PLANE, "JDPhysics", _SIMPLE_ACCUMULATION_) {



    hugoniotCurveArray_->operator []("IMPLICIT") = new ImplicitHugoniotCurve(&fluxFunction(), &accumulation(), &getBoundary());

    coincidenceMethod_ = new CoincidenceJD((const JDFluxFunction*) &fluxFunction(), (const JDAccumulationFunction*) &accumulation());
    JDEvap_Extension * ext = new JDEvap_Extension((const JDFluxFunction*) &fluxFunction(), coincidenceMethod_);


    compositeCurve_ = new JDEvaporationCompositeCurve(&accumulation(),&fluxFunction(),  &getBoundary(), ext);

    hugoniot_continuation_method_ = new HugoniotContinuation2D2D(&fluxFunction(), &accumulation(), &getBoundary());

    shockCurve_ = new ShockCurve(getHugoniotContinuationMethod());

    setDoubleContactFunction(new Double_Contact());
    setViscosityMatrix(new Viscosity_Matrix());
    preProcessedBoundary_ = defaultBoundary();

}

SubPhysics * JDPhysics::clone()const {

    return new JDPhysics(*this);
}



JDPhysics::~JDPhysics() {

}

void JDPhysics::setParams(vector<string> newParams) {


    RealVector fluxParamVector(1);
    double paramValue;
    //Flux params
    for (int i = 0; i < fluxParamVector.size(); i++) {
        std::stringstream stream(newParams[i]);
        stream >> paramValue;


        fluxParamVector.component(i) = paramValue;


    }

    JDFluxFunction * jdFlux= (JDFluxFunction *)fluxFunction_;
    FluxParams newCoreyQuadParams(fluxParamVector);
    fluxFunction_->fluxParams(newCoreyQuadParams);
    jdFlux->set_epsilon(fluxParamVector.component(0));

}

vector<double> * JDPhysics::getParams() {


    vector<double> * paramsVector = new vector<double>();

    for (int i = 0; i < fluxFunction_->fluxParams().params().size(); i++) {
        paramsVector->push_back(fluxFunction_->fluxParams().params().component(i));

    }

    return paramsVector;

}

Boundary * JDPhysics::defaultBoundary() const {

    RealVector min(2);

    min.component(0) = 0.1;
    min.component(1) = 0.0;

    RealVector max(2);

    max=min+ 5.0;


    return new RectBoundary(min, max);
}
