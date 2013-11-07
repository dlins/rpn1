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
#include "physics/tpcw/Hugoniot_TP.h"
#include "ThreeImplicitFunctions.h"
#include "Debug.h"

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

SubPhysics::SubPhysics(const Boundary & boundary, const Space & space, const char * name, int type) : boundary_(boundary.clone()),
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

const Boundary & SubPhysics::getBoundary() const {
    return *boundary_;
}

const int SubPhysics::type() const {
    return type_;
}

void SubPhysics::boundary(const Boundary & newBoundary) {

    delete boundary_;
    delete preProcessedBoundary_;
    boundary_ = newBoundary.clone();
    preProcessedBoundary_= newBoundary.clone();

}

void SubPhysics::setParams(vector<string> paramsVector) {

    for (int i = 0; i < paramsVector.size(); i++) {
    if ( Debug::get_debug_level() == 5 ) {
        cout << "Param " << i << " :" << paramsVector[i] << endl;
    }

    }


}

const Boundary * SubPhysics::getPreProcessedBoundary()const {
    return preProcessedBoundary_;
}

Hugoniot_Locus * SubPhysics::getHugoniotFunction()const {
    return hugoniotFunction_;
}

void SubPhysics::setHugoniotFunction(Hugoniot_Locus *hf) {

    hugoniotFunction_ = hf;
}


Viscosity_Matrix * SubPhysics::getViscosityMatrix() const{
    return viscosityMatrix_;
}
    
void SubPhysics::setViscosityMatrix(Viscosity_Matrix * newViscosityMatrix){

    viscosityMatrix_=newViscosityMatrix;
}


void SubPhysics::setDoubleContactFunction(Double_Contact_Function *dcf) {
    doubleContactFunction_ = dcf;
}

void SubPhysics::setShockMethod(HugoniotContinuation * shockMethod) {
    shock_method_ = shockMethod;
}

HugoniotContinuation * SubPhysics::getShockMethod() {
    return shock_method_;
}

Double_Contact_Function * SubPhysics::getDoubleContactFunction() {
    return doubleContactFunction_;
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
    delete doubleContactFunction_;
    delete space_;

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
    delete preProcessedBoundary_;
    
    delete viscosityMatrix_;


}

void SubPhysics::preProcess(RealVector &) {
}

void SubPhysics::postProcess(vector<RealVector> &) {
}

void SubPhysics::postProcess(RealVector &) {
}
