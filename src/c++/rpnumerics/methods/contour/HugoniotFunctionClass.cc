#include "HugoniotFunctionClass.h"

HugoniotFunctionClass::HugoniotFunctionClass(const FluxFunction &fluxFunction) : fluxFunction_((FluxFunction *)fluxFunction.clone()) {

}

HugoniotFunctionClass::HugoniotFunctionClass() {

}


void HugoniotFunctionClass::setFluxFunction(FluxFunction * flux){
    fluxFunction_=flux;
}

HugoniotFunctionClass::~HugoniotFunctionClass() {
    delete fluxFunction_;
    delete uRef_;
}
const FluxFunction & HugoniotFunctionClass::getFluxFunction()const {
    return *fluxFunction_;
}

void HugoniotFunctionClass::completeCurve(std::vector<RealVector> & curve) {
    return;
}

RealVector & HugoniotFunctionClass::getReferenceVector() {
    return *uRef_;

}

void HugoniotFunctionClass::setReferenceVector(const RealVector & refVec) {


    uRef_ = new RealVector(refVec.size());

    for (int i = 0; i < refVec.size(); i++) {

        uRef_->component(i) = refVec.component(i);


    }




}