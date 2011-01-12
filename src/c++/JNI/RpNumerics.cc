/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RPNUMERICS.cc
 **/


//! Definition of RPNUMERICS.
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

//#include "rpnumerics_RpNumerics.h"
#include <stdlib.h>
#include "rpnumerics_RPNUMERICS.h"
#include "RpNumerics.h"

//-------------------------------------
// PHYSICS
//-------------------------------------

// Quad2
#include "Quad2.h"
#include "Quad2FluxParams.h"
// Quad3
#include "Quad3.h"
#include "Quad3FluxParams.h"
// Quad4
#include "Quad4.h"
#include "Quad4FluxParams.h"
// Auxiliary use by TriPhase and Corey 
#include "CapilParams.h"
#include "PermParams.h"
#include "ViscosityParams.h"
// TriPhase
#include "TriPhase.h"
#include "TriPhaseParams.h"
// Corey
#include "Corey.h"
#include "CoreyParams.h"
//Stone
#include "Stone.h" 
#include "StoneParams.h" 

//TPCW
#include "TPCW.h"

//-------------------------------------


#include "LSODEStopGenerator.h"
#include "LSODEProfile.h"
#include "LSODESolver.h"

#include "JNIDefs.h"
#include <string.h>
#include <iostream>

using namespace std;

Physics * RpNumerics::physics_ = NULL;

double RpNumerics::sigma = 0;


/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setRPnHome
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setRPnHome
  (JNIEnv * env, jclass cls, jstring rpnHome){

    const char *rpnHomeC;

    rpnHomeC = env->GetStringUTFChars(rpnHome, NULL);

    string rpnHomeString (rpnHomeC);

    Physics::setRPnHome(rpnHomeString);

//    cout<<"RPn home path em physics: "<<Physics::getRPnHome()<<endl;

}


/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getFluxParams
 * Signature: ()Lrpnumerics/FluxParams;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getFluxParams
(JNIEnv * env, jclass cls) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);


    jmethodID fluxParamsConstructorID = (env)->GetMethodID(fluxParamsClass, "<init>", "(Lwave/util/RealVector;)V");
    jmethodID realVectorConstructorID = env->GetMethodID(realVectorClass, "<init>", "([D)V");


    const FluxParams & nativeFluxParams = RpNumerics::getPhysics().fluxFunction().fluxParams();
    const RealVector & nativeRealVectorParams = nativeFluxParams.params();

    int paramsSize = nativeRealVectorParams.size();
    double nativeRealVectorArray[paramsSize];

    for (int i = 0; i < paramsSize; i++) {
        nativeRealVectorArray[i] = nativeRealVectorParams(i);
    }

    jdoubleArray realVectorArray = env->NewDoubleArray(paramsSize);
    env->SetDoubleArrayRegion(realVectorArray, 0, paramsSize, nativeRealVectorArray);

    jobject realVector = (env)->NewObject(realVectorClass, realVectorConstructorID, realVectorArray);
    jobject fluxParams = (env)->NewObject(fluxParamsClass, fluxParamsConstructorID, realVector);

    return fluxParams;

}


/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFluxParams
 * Signature: (Lrpnumerics/FluxParams;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFluxParams(JNIEnv * env, jclass cls, jobject fluxParams) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);

    jmethodID getParamsMethodID = (env)->GetMethodID(fluxParamsClass, "getParams", "()Lwave/util/RealVector;");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jobject fluxParamRealVector = env->CallObjectMethod(fluxParams, getParamsMethodID);
    jdoubleArray fluxParamRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(fluxParamRealVector, toDoubleMethodID);

    int fluxParamSize = env->GetArrayLength(fluxParamRealVectorArray);
    double nativeFluxParamArray[fluxParamSize];

    env->GetDoubleArrayRegion(fluxParamRealVectorArray, 0, fluxParamSize, nativeFluxParamArray);
    RealVector nativeFluxParamRealVector(fluxParamSize, nativeFluxParamArray);
    FluxParams newFluxParams(nativeFluxParamRealVector);
    RpNumerics::getPhysics().fluxParams(newFluxParams);

}


/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setBoundary
 * Signature: (Lwave/util/Boundary;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setBoundary
(JNIEnv * env, jclass cls, jobject newBoundary) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass boundaryClass = env->FindClass(BOUNDARY_LOCATION);

    jclass rectBoundaryClass = env->FindClass(RECTBOUNDARY_LOCATION);
    jboolean testeBoundaryType = env->IsInstanceOf(newBoundary, rectBoundaryClass);

    jmethodID getMinimumsMethodID = env->GetMethodID(boundaryClass, "getMinimums", "()Lwave/util/RealVector;");
    jmethodID getMaximumsMethodID = env->GetMethodID(boundaryClass, "getMaximums", "()Lwave/util/RealVector;");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jobject minRealVector = env->CallObjectMethod(newBoundary, getMinimumsMethodID);
    jobject maxRealVector = env->CallObjectMethod(newBoundary, getMaximumsMethodID);

    jdoubleArray minRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(minRealVector, toDoubleMethodID);
    jdoubleArray maxRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(maxRealVector, toDoubleMethodID);

    int minSize = env->GetArrayLength(minRealVectorArray);
    int maxSize = env->GetArrayLength(maxRealVectorArray);

    double minNativeArray [minSize];
    double maxNativeArray [maxSize];

    env->GetDoubleArrayRegion(minRealVectorArray, 0, minSize, minNativeArray);
    env->GetDoubleArrayRegion(maxRealVectorArray, 0, maxSize, maxNativeArray);

    if (testeBoundaryType) { //         RectBoundary 

        RealVector minNativeRealVector(minSize, minNativeArray);
        RealVector maxNativeRealVector(maxSize, maxNativeArray);
        RectBoundary nativeBoundary(minNativeRealVector, maxNativeRealVector);
        RpNumerics::getPhysics().boundary(nativeBoundary);

    } 


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFamilyIndex
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFamilyIndex
(JNIEnv *env, jobject obj, jint familyIndex) {

    //WaveFlowFactory::setFamilyIndex(familyIndex);


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setTimeDirection
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setTimeDirection
(JNIEnv * env, jobject obj, jint timeDirection) {
    //WaveFlowFactory::setTimeDirection(timeDirection);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    clean
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_clean(JNIEnv * env, jclass cls) {
    RpNumerics::clean();
}

void RpNumerics::clean() {

    delete physics_;
}

/* Class:     rpnumerics_RPNUMERICS
 * Method:    physicsID
 * Signature: ()Ljava/lang/String;
 */


JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_physicsID(JNIEnv * env, jclass cls) {

    return env->NewStringUTF(RpNumerics::getPhysics().ID().c_str());
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domain
 * Signature: ()Lwave/multid/Space;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_domain(JNIEnv * env, jclass cls) {

    jclass spaceClass = env->FindClass("wave/multid/Space");
    jmethodID spaceConstructor = (env)->GetMethodID(spaceClass, "<init>", "(Ljava/lang/String;I)V");

    jstring spaceName = env->NewStringUTF(RpNumerics::getPhysics().domain().name());
    jobject space = env->NewObject(spaceClass, spaceConstructor, spaceName, RpNumerics::getPhysics().domain().dim());

    return space;

}

/* Class:     rpnumerics_RPNUMERICS
 * Method:    initNative
 * Signature: (Ljava/lang/String;)V
 */

JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_initNative(JNIEnv * env, jclass cls, jstring physicsName) {

    const char *physicsID;

    physicsID = env->GetStringUTFChars(physicsName, NULL);


    if (physicsID == NULL) {
        return; /* OutOfMemoryError already thrown */
    }
//    cout << "Construindo a fisica: " << physicsID << endl;
    RpNumerics::setPhysics(Physics(physicsID));

//     if (!strcmp(physicsID, "TPCW")) {
//
//
//
//        RpNumerics::setPhysics(TPCW());
//    }

}



JNIEXPORT jobject JNICALL Java_rpnumerics_RpNumerics_getXZero(JNIEnv * env, jclass cls) {

    double teste[2];

    teste[0] = 0.1;
    teste[1] = 0.1;

    int coordsSize = 2;

    jclass realVectorClass_ = env->FindClass("wave/util/RealVector");
    jclass phasePointClass_ = env->FindClass("rpnumerics/PhasePoint");

    jmethodID phasePointConstructor_ = (env)->GetMethodID(phasePointClass_, "<init>", "(Lwave/util/RealVector;)V");
    jmethodID realVectorConstructorDoubleArray_ = env->GetMethodID(realVectorClass_, "<init>", "([D)V");

    jdoubleArray tempArray = env->NewDoubleArray(coordsSize);
    env->SetDoubleArrayRegion(tempArray, 0, coordsSize, teste);

    jobject realVector = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    jobject phasePoint = env->NewObject(phasePointClass_, phasePointConstructor_, realVector);

    env->DeleteLocalRef(tempArray);

    return phasePoint;
}




/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    createNativeRarefactionFlow
 * Signature: (Ljava/lang/String;)V
 */

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domainDim
 * Signature: ()I
 */

JNIEXPORT jint JNICALL Java_rpnumerics_RPNUMERICS_domainDim(JNIEnv * env, jclass cls) {
    return RpNumerics::getPhysics().domain().dim();
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    boundary
 * Signature: ()Lwave/util/Boundary;
 */



JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary(JNIEnv * env, jclass cls) {

    jclass realVectorClass = env->FindClass("wave/util/RealVector");
    jmethodID realVectorConstructor = (env)->GetMethodID(realVectorClass, "<init>", "([D)V");

    const Boundary & boundary = RpNumerics::getPhysics().boundary();
    const char * boundaryType = boundary.boundaryType();

    if (!strcmp(boundaryType, "rect")) {



        jclass boundaryClass = env->FindClass("wave/util/RectBoundary");
        jmethodID boundaryConstructor = (env)->GetMethodID(boundaryClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

        const RectBoundary & rectBoundary = (RectBoundary &) boundary;
        int boundaryDimension = rectBoundary.minimums().size();

        double minimum [boundaryDimension];
        double maximum [boundaryDimension];

        for (int i = 0; i < boundaryDimension; i++) {
            minimum[i] = boundary.minimums().component(i);
            maximum[i] = boundary.maximums().component(i);
        }


        //-----------------------------------------------------------------------


        jdoubleArray min = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray max = (env)->NewDoubleArray(boundaryDimension);

        (env)->SetDoubleArrayRegion(min, 0, boundaryDimension, minimum);
        (env)->SetDoubleArrayRegion(max, 0, boundaryDimension, maximum);

        jobject minRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, min);
        jobject maxRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, max);

        jobject boundary = (env)->NewObject(boundaryClass, boundaryConstructor, minRealVector, maxRealVector);

        (env)->DeleteLocalRef(min);
        (env)->DeleteLocalRef(max);
        (env)->DeleteLocalRef(minRealVector);
        (env)->DeleteLocalRef(maxRealVector);

        return boundary;

    }

    if (!strcmp(boundaryType, "triang")) {

        const IsoTriang2DBoundary & boundary = (const IsoTriang2DBoundary &) RpNumerics::getPhysics().boundary();
        jclass isoRect2DBboundaryClass = env->FindClass("wave/util/IsoTriang2DBoundary");
        jmethodID isoTriang2DBoundaryConstructor = (env)->GetMethodID(isoRect2DBboundaryClass, "<init>", 
					"(Lwave/util/RealVector;Lwave/util/RealVector;Lwave/util/RealVector;)V");

        int boundaryDimension = boundary.minimums().size();

        jdoubleArray A = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray B = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray C = (env)->NewDoubleArray(boundaryDimension);

        // Getting A,B and C
        double Anative [boundaryDimension];
        double Bnative [boundaryDimension];
        double Cnative [boundaryDimension];

        for (int i = 0; i < boundaryDimension; i++) {
            Anative[i] = boundary.getA().component(i);
            Bnative[i] = boundary.getB().component(i);
            Cnative[i] = boundary.getC().component(i);
        }


        //-----------------------------------------------------------------------


        (env)->SetDoubleArrayRegion(A, 0, boundaryDimension, Anative);
        (env)->SetDoubleArrayRegion(B, 0, boundaryDimension, Bnative);
        (env)->SetDoubleArrayRegion(C, 0, boundaryDimension, Cnative);

        jobject ArealVector = (env)->NewObject(realVectorClass, realVectorConstructor, A);
        jobject BrealVector = (env)->NewObject(realVectorClass, realVectorConstructor, B);
        jobject CrealVector = (env)->NewObject(realVectorClass, realVectorConstructor, C);

        jobject isoTriang2DBoundary = (env)->NewObject(isoRect2DBboundaryClass, isoTriang2DBoundaryConstructor, ArealVector, BrealVector, CrealVector);

        (env)->DeleteLocalRef(A);
        (env)->DeleteLocalRef(B);
        (env)->DeleteLocalRef(C);

        (env)->DeleteLocalRef(ArealVector);
        (env)->DeleteLocalRef(BrealVector);
        (env)->DeleteLocalRef(CrealVector);

        return isoTriang2DBoundary;


    }
    cout << "Boundary not defined" << endl;
    return NULL;

}



