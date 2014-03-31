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
#include "Secondary_Bifurcation_Interface.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

SubPhysics::SubPhysics(const FluxFunction & fluxFunction, const AccumulationFunction & accumulationFunction, const Boundary & boundary, const Space & space, const char * id, int type) : fluxFunction_((FluxFunction *) fluxFunction.clone()),
accumulationFunction_((AccumulationFunction*) accumulationFunction.clone()), hugoniotArray_(new map<string, Hugoniot_Locus *>()),secondaryBifurcationArray_(new map<string, Secondary_Bifurcation_Interface *>()),
boundary_(boundary.clone()),
space_(new Space(space)),
ID_(id),
type_(type) {
    setHugoniotContinuationMethod(new HugoniotContinuation2D2D(fluxFunction_, accumulationFunction_, &getBoundary()));
    //    setShockMethod(new Shock());
}

SubPhysics::SubPhysics(const Boundary & boundary, const Space & space, const char * name, int type) : boundary_(boundary.clone()),
space_(new Space(space)), hugoniotArray_(new map<string, Hugoniot_Locus *>()),secondaryBifurcationArray_(new map<string, Secondary_Bifurcation_Interface *>()),
ID_(name),
type_(type) {
    setHugoniotContinuationMethod(new HugoniotContinuation2D2D(fluxFunction_, accumulationFunction_, &getBoundary()));
    //    setShockMethod(new Shock());
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
    preProcessedBoundary_ = newBoundary.clone();

}

void SubPhysics::setParams(vector<string> paramsVector) {

    for (int i = 0; i < paramsVector.size(); i++) {
        if (Debug::get_debug_level() == 5) {
            cout << "Param " << i << " :" << paramsVector[i] << endl;
        }

    }

}

Hugoniot_Locus * SubPhysics::getHugoniotMethod(const string & gridName) {

    if (hugoniotArray_->count(gridName) == 1) {
        return hugoniotArray_->operator [](gridName);
    } else {

        cerr<<"Method not implemented "<<endl;
    }

}



Secondary_Bifurcation_Interface * SubPhysics::getSecondaryBifurcationMethod(const string& methodName) {

    if (secondaryBifurcationArray_->count(methodName) == 1) {
        return secondaryBifurcationArray_->operator [](methodName);
    } else {

        cerr<<"Method not implemented "<<endl;
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

Viscosity_Matrix * SubPhysics::getViscosityMatrix() const {
    return viscosityMatrix_;
}

void SubPhysics::setViscosityMatrix(Viscosity_Matrix * newViscosityMatrix) {

    viscosityMatrix_ = newViscosityMatrix;
}

void SubPhysics::setDoubleContactFunction(Double_Contact_Function *dcf) {
    doubleContactFunction_ = dcf;
}

void SubPhysics::setShockMethod(ShockMethod * shockMethod) {
    shock_method_ = shockMethod;
}

ShockMethod * SubPhysics::getShockMethod() {
    return shock_method_;
}

HugoniotContinuation * SubPhysics::getHugoniotContinuationMethod() {
    return hugoniot_continuation_method_;
}

void SubPhysics::setHugoniotContinuationMethod(HugoniotContinuation * hugoniotContinuation) {

    hugoniot_continuation_method_ = hugoniotContinuation;

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
    delete hugoniotArray_;


}

void SubPhysics::preProcess(RealVector &) {
}

void SubPhysics::postProcess(vector<RealVector> &) {
}

void SubPhysics::postProcess(RealVector &) {
}
