/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) SubPhysics.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

SubPhysics::SubPhysics(const FluxFunction & fluxFunction, const AccumulationFunction & accumulationFunction, const Boundary & boundary, const Space & space, const char * id, int type) : fluxFunction_((FluxFunction *) fluxFunction.clone()),
accumulationFunction_((AccumulationFunction*) accumulationFunction.clone()),
boundary_(boundary.clone()),
space_(new Space(space)),
ID_(id),
type_(type) {

}


 SubPhysics::SubPhysics(const Boundary & boundary, const Space & space, const char * name, int type):boundary_(boundary.clone()),
space_(new Space(space)),
ID_(name),
type_(type) {

 }

const Space &SubPhysics::domain() const {
    return *space_;
}

const char * SubPhysics::ID() const {
    return ID_;
}

const Boundary & SubPhysics::boundary() const {
    return *boundary_;
}

const int SubPhysics::type() const {
    return type_;
}

void SubPhysics::boundary(const Boundary & newBoundary) {

    delete boundary_;
    boundary_ = newBoundary.clone();

}

void SubPhysics::setParams(vector<string> paramsVector) {

    for (int i =0; paramsVector.size();i++){
        cout<<"Param "<<i<<" :"<<paramsVector.at(i)<<endl;

    }


}




HugoniotFunctionClass * SubPhysics::getHugoniotFunction()const {
    return hugoniotFunction_;
}

void SubPhysics::setHugoniotFunction(HugoniotFunctionClass *hf) {

    hugoniotFunction_ = hf;
}

const FluxFunction & SubPhysics::fluxFunction() const {
    return *fluxFunction_;
}

const AccumulationFunction & SubPhysics::accumulation() const {
    return *accumulationFunction_;
}

void SubPhysics::fluxParams(const FluxParams & newFluxParams) {
    fluxFunction_->fluxParams(newFluxParams);
}

void SubPhysics::accumulationParams(const AccumulationParams & newAccumulationParams) {

    accumulationFunction_->accumulationParams(newAccumulationParams);
}

SubPhysics::~SubPhysics() {

    delete hugoniotFunction_;
    delete space_;
    
    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;



}

void SubPhysics::preProcess(RealVector &) {
}

void SubPhysics::postProcess(vector<RealVector> &) {
}
