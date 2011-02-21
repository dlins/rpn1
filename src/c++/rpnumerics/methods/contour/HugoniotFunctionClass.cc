#include "HugoniotFunctionClass.h"

void HugoniotFunctionClass::completeCurve(std::vector<RealVector> & curve) {
    return;
}

RealVector & HugoniotFunctionClass::getReferenceVector(){
    return *uRef_;

}
void HugoniotFunctionClass::setReferenceVector(const RealVector & refVec) {


    uRef_=new RealVector(refVec.size());

    for (int i=0; i< refVec.size();i++){

        uRef_->component(i)=refVec.component(i);


    }


}