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
//Stone
#include "Stone.h" 
#include "StoneParams.h" 

//TPCW
#include "TPCW.h"



//-------------------------------------



#include "JNIDefs.h"
#include <string.h>
#include <iostream>
#include <vector>

using namespace std;

Physics * RpNumerics::physics_ = NULL;

GridValuesFactory * RpNumerics::gridValuesFactory_ = NULL;

double RpNumerics::sigma = 0;

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setRPnHome
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setRPnHome
(JNIEnv * env, jclass cls, jstring rpnHome) {

    const char *rpnHomeC;

    rpnHomeC = env->GetStringUTFChars(rpnHome, NULL);

    string rpnHomeString(rpnHomeC);

    Physics::setRPnHome(rpnHomeString);

    //    cout << "RPn home path in physics: " << Physics::getRPnHome() << endl;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setParams
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setParams
(JNIEnv * env, jclass cls, jobjectArray stringParamsArray) {

    const char *paramNative;

    int paramSize = env->GetArrayLength(stringParamsArray);

    vector<string> paramVector;

    for (int i = 0; i < paramSize; i++) {
        jstring paramString = (jstring) (env)->GetObjectArrayElement(stringParamsArray, i);

        paramNative = env->GetStringUTFChars(paramString, NULL);

        string paramElementString(paramNative);
        paramVector.push_back(paramElementString);

    }


    RpNumerics::getPhysics().setParams(paramVector);

    int dimension = RpNumerics::getPhysics().domain().dim();

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();


    GridValues * gridHugoniot = RpNumerics::getGridFactory().getGrid("hugoniotcurve");

    GridValues * gridDoubleContact = RpNumerics::getGridFactory().getGrid("doublecontactcurve");

    GridValues * gridBifurcation = RpNumerics::getGridFactory().getGrid("bifurcation");


    std::vector<int> resolution;

    resolution.push_back(gridHugoniot->grid.rows());
    resolution.push_back(gridHugoniot->grid.cols());

    RpNumerics::getGridFactory().updateGrids();


    //
    //    //TODO Substituir por um metodo de atualizacao dos grids no factory
    //
    //    gridHugoniot->set_grid(boundary, boundary->minimums(), boundary->maximums(), resolution);
    //
    //
    //    resolution.clear();
    //
    //    resolution.push_back(gridDoubleContact->grid.rows());
    //    resolution.push_back(gridDoubleContact->grid.cols());
    //
    //
    //
    //    gridDoubleContact->set_grid(boundary, boundary->minimums(), boundary->maximums(), resolution);
    //
    //    resolution.clear();
    //
    //    resolution.push_back(gridBifurcation->grid.rows());
    //    resolution.push_back(gridBifurcation->grid.cols());
    //
    //
    //    gridBifurcation->set_grid(boundary, boundary->minimums(), boundary->maximums(), resolution);

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

        nativeRealVectorArray[i] = nativeRealVectorParams.component(i);
    }

    jdoubleArray realVectorArray = env->NewDoubleArray(paramsSize);
    env->SetDoubleArrayRegion(realVectorArray, 0, paramsSize, nativeRealVectorArray);

    jobject realVector = (env)->NewObject(realVectorClass, realVectorConstructorID, realVectorArray);
    jobject fluxParams = (env)->NewObject(fluxParamsClass, fluxParamsConstructorID, realVector);

    return fluxParams;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setAccumulationParams
 * Signature: (Lwave/util/RealVector;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setAccumulationParams
(JNIEnv * env, jclass cls, jobject newParamsVector) {



    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");


    //Input processing
    jdoubleArray newVectorArray = (jdoubleArray) (env)->CallObjectMethod(newParamsVector, toDoubleMethodID);

    int dimension = env->GetArrayLength(newVectorArray);

    double input [dimension];

    env->GetDoubleArrayRegion(newVectorArray, 0, dimension, input);

    RealVector newAccumulationParamsVector(dimension, input);

    AccumulationParams newAccumulationParams(newAccumulationParamsVector);
    RpNumerics::getPhysics().accumulationParams(newAccumulationParams);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAccumulationParams
 * Signature: ()Lwave/util/RealVector;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getAccumulationParams
(JNIEnv * env, jclass cls) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jmethodID realVectorConstructorID = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    const AccumulationParams & nativeAccumulationParams = RpNumerics::getPhysics().accumulation().accumulationParams();

    const RealVector & nativeRealVectorParams = nativeAccumulationParams.params();

    int paramsSize = nativeRealVectorParams.size();


    double nativeRealVectorArray[paramsSize];

    for (int i = 0; i < paramsSize; i++) {

        nativeRealVectorArray[i] = nativeRealVectorParams.component(i);
    }

    jdoubleArray realVectorArray = env->NewDoubleArray(paramsSize);
    env->SetDoubleArrayRegion(realVectorArray, 0, paramsSize, nativeRealVectorArray);

    jobject realVector = (env)->NewObject(realVectorClass, realVectorConstructorID, realVectorArray);


    return realVector;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setResolution
 * Signature: (Lwave/util/RealVector;Lwave/util/RealVector;Ljava/lang/String;[I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setResolution
(JNIEnv * env, jclass cls, jobject min, jobject max, jstring gridName, jintArray newResolution) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");


    int dimension = env->GetArrayLength(newResolution);

    //min  processing
    jdoubleArray minLimit = (jdoubleArray) (env)->CallObjectMethod(min, toDoubleMethodID);

    double minNativeArray[dimension];
    env->GetDoubleArrayRegion(minLimit, 0, dimension, minNativeArray);
    //max processing

    jdoubleArray maxLimit = (jdoubleArray) (env)->CallObjectMethod(max, toDoubleMethodID);

    double maxNativeArray[dimension];

    env->GetDoubleArrayRegion(maxLimit, 0, dimension, maxNativeArray);


    RealVector minNativeVector(dimension, minNativeArray);

    RealVector maxNativeVector(dimension, maxNativeArray);

    //Processing resolution
    vector<int>newResolutionVector;

    int tempResolutionArray[dimension];


    env->GetIntArrayRegion(newResolution, 0, dimension, tempResolutionArray);


    for (int i = 0; i < dimension; i++) {
        newResolutionVector.push_back(tempResolutionArray[i]);

    }

    const char * gridNameNative = env->GetStringUTFChars(gridName, NULL);

    GridValues * grid = RpNumerics::getGridFactory().getGrid(string(gridNameNative));

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();


    grid->set_grid(boundary, minNativeVector, maxNativeVector, newResolutionVector);

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

    jclass isoTriangBoundaryClass = env->FindClass(ISOTRIANG2DBOUNDARY_LOCATION);

    jboolean testRectBoundary = env->IsInstanceOf(newBoundary, rectBoundaryClass);

    jboolean testIsoTriangBoundary = env->IsInstanceOf(newBoundary, isoTriangBoundaryClass);

    jmethodID getMinimumsMethodID = env->GetMethodID(boundaryClass, "getMinimums", "()Lwave/util/RealVector;");
    jmethodID getMaximumsMethodID = env->GetMethodID(boundaryClass, "getMaximums", "()Lwave/util/RealVector;");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");


    jmethodID getAMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getA", "()Lwave/util/RealVector;");
    jmethodID getBMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getB", "()Lwave/util/RealVector;");
    jmethodID getCMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getC", "()Lwave/util/RealVector;");


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

    if (testRectBoundary) { //  RectBoundary

        RealVector minNativeRealVector(minSize, minNativeArray);
        RealVector maxNativeRealVector(maxSize, maxNativeArray);
        RectBoundary nativeBoundary(minNativeRealVector, maxNativeRealVector);
        RpNumerics::getPhysics().boundary(nativeBoundary);

    }

    if (testIsoTriangBoundary) { //   IsoTriang2DBoundary


        RealVector minNativeRealVector(minSize, minNativeArray);
        RealVector maxNativeRealVector(maxSize, maxNativeArray);


        cout << "O tipo eh isotriang boundary" << minNativeRealVector << " " << maxNativeRealVector << endl;


        Three_Phase_Boundary triangBoundary(minNativeRealVector, maxNativeRealVector);


        RpNumerics::getPhysics().boundary(triangBoundary);


    }
    RpNumerics::getGridFactory().updateGrids();


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFamilyIndex
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFamilyIndex
(JNIEnv *env, jobject obj, jint familyIndex) {


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

        //cout <<"Min: "<<rectBoundary.minimums()<<" Max: "<<rectBoundary.maximums()<<endl;

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

    if (!strcmp(boundaryType, "Three_Phase_Boundary")) {

        const Three_Phase_Boundary & boundary = (const Three_Phase_Boundary &) RpNumerics::getPhysics().boundary();

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



