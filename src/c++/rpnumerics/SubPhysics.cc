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
accumulationFunction_((AccumulationFunction*) accumulationFunction.clone()),  hugoniotCurveArray_(new map<string, HugoniotCurve *>()),secondaryBifurcationArray_(new map<string, Secondary_Bifurcation_Interface *>()),
boundary_(boundary.clone()), extensionCurveArray_(new map<string, Extension *>()),
space_(new Space(space)),
ID_(id),
type_(type) {
    setHugoniotContinuationMethod(new HugoniotContinuation2D2D(fluxFunction_, accumulationFunction_, &getBoundary()));
    //    setShockMethod(new Shock());
}

SubPhysics::SubPhysics(const Boundary & boundary, const Space & space, const char * name, int type) : boundary_(boundary.clone()),
space_(new Space(space)), hugoniotCurveArray_(new map<string, HugoniotCurve *>()),secondaryBifurcationArray_(new map<string, Secondary_Bifurcation_Interface *>()),
ID_(name),extensionCurveArray_(new map<string, Extension *>()),
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
            //cout << "Param " << i << " :" << paramsVector[i] << endl;
        }

    }

}



HugoniotCurve * SubPhysics::getHugoniotCurve(const string & hugoniotName) {

    if (hugoniotCurveArray_->count(hugoniotName) == 1) {
        return hugoniotCurveArray_->operator [](hugoniotName);
    } else {

        cerr<<"Method not implemented "<<endl;
    }

}



Extension * SubPhysics::getExtensionMethod(const string& methodName) {

    if (extensionCurveArray_->count(methodName) == 1) {
        return extensionCurveArray_->operator [](methodName);
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


CompositeCurve * SubPhysics::getCompositeCurve(){
    return compositeCurve_;
}
const Boundary * SubPhysics::getPreProcessedBoundary()const {
    return preProcessedBoundary_;
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

ShockCurve * SubPhysics::getShockMethod() {
    return shockCurve_;
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


    delete doubleContactFunction_;
    delete space_;

    delete fluxFunction_;
    delete accumulationFunction_;
    delete boundary_;
    delete preProcessedBoundary_;
    delete viscosityMatrix_;

    delete shockCurve_;
    
     for (std::map<string,Extension *>::iterator it=extensionCurveArray_->begin(); it!=extensionCurveArray_->end(); ++it){
        delete it->second ;        
    }
    
    delete extensionCurveArray_;    
    
    
    for (std::map<string,HugoniotCurve *>::iterator it=hugoniotCurveArray_->begin(); it!=hugoniotCurveArray_->end(); ++it){
        delete it->second ;        
    }
    
    delete hugoniotCurveArray_;
    

}

void SubPhysics::preProcess(RealVector &) {
}

void SubPhysics::postProcess(vector<RealVector> &) {
}

void SubPhysics::postProcess(RealVector &) {
}
