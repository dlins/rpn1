/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIWaveFlow.cc
 **/

//! Definition of JNIWaveFlow
/*!
 *
 * TODO:
 *
 * NOTE :
 *
 * @ingroup JNI
 */

#include "rpnumerics_WaveFlow.h"
#include "FluxFunction.h"
#include "RpNumerics.h"
#include "JNIDefs.h"
#include "PluginService.h"
#include "RPnPluginManager.h"
#include "ShockFlowPlugin.h"
#include "RarefactionFlowPlugin.h"
#include <iostream>

JNIEXPORT jobject JNICALL Java_rpnumerics_WaveFlow_flux(JNIEnv * env, jobject obj, jobject realVector) {

    //Classes references

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);


    jclass shockFlowClass = env->FindClass(SHOCKFLOW_LOCATION);
    jclass rarefactionFlowClass = env->FindClass(RAREFACTIONFLOW_LOCATION);


    jclass objectClass = env->FindClass("java/lang/Object");
    jclass clsClass = env->FindClass("java/lang/Class");


    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID getSizeMethodID = (env)->GetMethodID(realVectorClass, "getSize", "()I");
    jmethodID realVectorConstructorID = (env)->GetMethodID(realVectorClass, "<init>", "(I)V");
    jmethodID setElementMethodID = (env)->GetMethodID(realVectorClass, "setElement", "(ID)V");


    jmethodID getSigmaMethodID = (env)->GetMethodID(shockFlowClass, "getSigma", "()D");
    jmethodID getXZeroMethodID = (env)->GetMethodID(shockFlowClass, "getXZero", "()Lrpnumerics/PhasePoint;");


    jmethodID getReferenceVectorMethodID = (env)->GetMethodID(rarefactionFlowClass, "getReferenceVector", "()Lrpnumerics/PhasePoint;");
    jmethodID getFamilyMethodID = (env)->GetMethodID(rarefactionFlowClass, "getFamily", "()I");


    jmethodID getClassID = (env)->GetMethodID(objectClass, "getClass", "()Ljava/lang/Class;");
    jmethodID getClassNameID = (env)->GetMethodID(clsClass, "getSimpleName", "()Ljava/lang/String;");

    //Catching the object class name

    jobject classObj = (env)->CallObjectMethod(obj, getClassID);
    jstring className = (jstring) (env)->CallObjectMethod(classObj, getClassNameID);
    const char *objectClassName;
    objectClassName = env->GetStringUTFChars(className, NULL);

    //Getting the dimension
    int dimension = env->CallIntMethod(realVector, getSizeMethodID);

    //Processing the input

    jdoubleArray realVectorArray = (jdoubleArray) (env)->CallObjectMethod(realVector, toDoubleMethodID);
    double input [dimension];

    env->GetDoubleArrayRegion(realVectorArray, 0, dimension, input);

    //Creating input and output objects


    RealVector realVectorInput(dimension, input);
    RealVector realVectorOutput(dimension);


    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    if (!strcmp(objectClassName, "ShockFlow")) {

        jdouble sigma = (env)->CallDoubleMethod(obj, getSigmaMethodID); // <--- sigma

        jobject xzero = (env)->CallObjectMethod(obj, getXZeroMethodID);

        jdoubleArray xzeroArray = (jdoubleArray) (env)->CallObjectMethod(xzero, toDoubleMethodID);

        int xzeroLength = env->GetArrayLength(xzeroArray);

        double nativeXZeroArray [xzeroLength]; // <--- XZero

        env->GetDoubleArrayRegion(xzeroArray, 0, xzeroLength, nativeXZeroArray);

        //                for (int i = 0; i < xzeroLength; i++) {
        //                    cout << "XZero: " << nativeXZeroArray [i] << "\n";
        //                }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("ShockFlow");

        ShockFlowPlugin* flow = (ShockFlowPlugin *) plug;

        PhasePoint phasePoint(RealVector(xzeroLength, nativeXZeroArray));

        ShockFlowParams newParams(phasePoint, sigma);

        //Setting the new parameters and flux function
        flow->setParams(newParams);
       
        flow->setFluxFunction(fluxFunction);

        flow->flux(realVectorInput, realVectorOutput);

        RPnPluginManager::unload(plug, "ShockFlow");

    }

    if (!strcmp(objectClassName, "RarefactionFlow")) {


        jint family = (env)->CallIntMethod(obj, getFamilyMethodID); // <--- Family

        jobject referenceVector = (env)->CallObjectMethod(obj, getReferenceVectorMethodID);

        jdoubleArray referenceArray = (jdoubleArray) (env)->CallObjectMethod(referenceVector, toDoubleMethodID);

        int referenceLength = env->GetArrayLength(referenceArray);

        double nativeReferenceArray [referenceLength]; // <--- Reference Vector

        env->GetDoubleArrayRegion(referenceArray, 0, referenceLength, nativeReferenceArray);

        cout << "Family: " << family << "\n";

        for (int i = 0; i < referenceLength; i++) {
            cout << "Reference Vector: " << nativeReferenceArray [i] << "\n";
        }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("RarefactionFlow");

        RarefactionFlowPlugin* flow = (RarefactionFlowPlugin*) plug;


        //Setting the new parameters and flux function


        flow->setFamilyIndex(family);

        flow->setReferenceVector(RealVector(dimension, nativeReferenceArray));

        flow->setFluxFunction(fluxFunction);

        flow->flux(realVectorInput, realVectorOutput);

        RPnPluginManager::unload(plug, "RarefactionFlow");

    }

    jobject returnedRealVector = env->NewObject(realVectorClass, realVectorConstructorID, dimension);

    for (int i = 0; i < dimension; i++) {

        env->CallVoidMethod(returnedRealVector, setElementMethodID, i, realVectorOutput(i));

    }

    env->ReleaseStringUTFChars(className, objectClassName);

    return returnedRealVector;

}

/*
 * Class:     rpnumerics_WaveFlow
 * Method:    fluxDeriv
 * Signature: (Lwave/util/RealVector;)Lwave/util/JacobianMatrix;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_WaveFlow_fluxDeriv(JNIEnv * env, jobject obj, jobject realVector) {

    //Classes references

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass jacobianMatrixClass = env->FindClass(JACOBIANMATRIX_LOCATION);


    jclass shockFlowClass = env->FindClass(SHOCKFLOW_LOCATION);
    jclass rarefactionFlowClass = env->FindClass(RAREFACTIONFLOW_LOCATION);


    jclass objectClass = env->FindClass("java/lang/Object");
    jclass clsClass = env->FindClass("java/lang/Class");


    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID getSizeMethodID = (env)->GetMethodID(realVectorClass, "getSize", "()I");

    jmethodID jacobianMatrixConstructorID = (env)->GetMethodID(jacobianMatrixClass, "<init>", "(I)V");
    jmethodID setElementMethodID = (env)->GetMethodID(jacobianMatrixClass, "setElement", "(IID)V");


    jmethodID getSigmaMethodID = (env)->GetMethodID(shockFlowClass, "getSigma", "()D");
    jmethodID getXZeroMethodID = (env)->GetMethodID(shockFlowClass, "getXZero", "()Lrpnumerics/PhasePoint;");


    jmethodID getReferenceVectorMethodID = (env)->GetMethodID(rarefactionFlowClass, "getReferenceVector", "()Lrpnumerics/PhasePoint;");
    jmethodID getFamilyMethodID = (env)->GetMethodID(rarefactionFlowClass, "getFamily", "()I");


    jmethodID getClassID = (env)->GetMethodID(objectClass, "getClass", "()Ljava/lang/Class;");
    jmethodID getClassNameID = (env)->GetMethodID(clsClass, "getSimpleName", "()Ljava/lang/String;");

    //Catching the object class name

    jobject classObj = (env)->CallObjectMethod(obj, getClassID);
    jstring className = (jstring) (env)->CallObjectMethod(classObj, getClassNameID);
    const char *objectClassName;
    objectClassName = env->GetStringUTFChars(className, NULL);

    //Getting the dimension
    int dimension = env->CallIntMethod(realVector, getSizeMethodID);

    //Processing the input

    jdoubleArray realVectorArray = (jdoubleArray) (env)->CallObjectMethod(realVector, toDoubleMethodID);
    double input [dimension];

    env->GetDoubleArrayRegion(realVectorArray, 0, dimension, input);

    //Creating input and output objects


    RealVector realVectorInput(dimension, input);
    JacobianMatrix jacobianMatrixOutput(dimension);


    const FluxFunction & fluxFunction = RpNumerics::getFlux();




    if (!strcmp(objectClassName, "ShockFlow")) {

        jdouble sigma = (env)->CallDoubleMethod(obj, getSigmaMethodID); // <--- sigma

        jobject xzero = (env)->CallObjectMethod(obj, getXZeroMethodID);

        jdoubleArray xzeroArray = (jdoubleArray) (env)->CallObjectMethod(xzero, toDoubleMethodID);

        int xzeroLength = env->GetArrayLength(xzeroArray);

        double nativeXZeroArray [xzeroLength]; // <--- XZero

        env->GetDoubleArrayRegion(xzeroArray, 0, xzeroLength, nativeXZeroArray);

        //                for (int i = 0; i < xzeroLength; i++) {
        //                    cout << "XZero: " << nativeXZeroArray [i] << "\n";
        //                }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("ShockFlow");

        ShockFlowPlugin* flow = (ShockFlowPlugin *) plug;

        PhasePoint phasePoint(RealVector(xzeroLength, nativeXZeroArray));

        ShockFlowParams newParams(phasePoint, sigma);


        //Setting the new parameters and flux function
        flow->setParams(newParams);

        flow->setFluxFunction(fluxFunction);

        flow->fluxDeriv(realVectorInput, jacobianMatrixOutput);

        RPnPluginManager::unload(plug, "ShockFlow");

    }

    if (!strcmp(objectClassName, "RarefactionFlow")) {


        jint family = (env)->CallIntMethod(obj, getFamilyMethodID); // <--- Family

        jobject referenceVector = (env)->CallObjectMethod(obj, getReferenceVectorMethodID);

        jdoubleArray referenceArray = (jdoubleArray) (env)->CallObjectMethod(referenceVector, toDoubleMethodID);

        int referenceLength = env->GetArrayLength(referenceArray);

        double nativeReferenceArray [referenceLength]; // <--- Reference Vector

        env->GetDoubleArrayRegion(referenceArray, 0, referenceLength, nativeReferenceArray);

        cout << "Family: " << family << "\n";

        for (int i = 0; i < referenceLength; i++) {
            cout << "Reference Vector: " << nativeReferenceArray [i] << "\n";
        }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("RarefactionFlow");

        RarefactionFlowPlugin* flow = (RarefactionFlowPlugin*) plug;

        flow->setFamilyIndex(family);
        flow->setReferenceVector(RealVector(dimension, nativeReferenceArray));
        flow->setFluxFunction(fluxFunction);


        flow->fluxDeriv(realVectorInput, jacobianMatrixOutput);

        RPnPluginManager::unload(plug, "RarefactionFlow");

    }

    jobject returnedJacobianMatrix = env->NewObject(jacobianMatrixClass, jacobianMatrixConstructorID, dimension);

    for (int i = 0; i < dimension; i++) {
        for (int j = 0; j < dimension; j++) {
            env->CallVoidMethod(returnedJacobianMatrix, setElementMethodID, i, j, jacobianMatrixOutput(i, j));
        }
    }
    env->ReleaseStringUTFChars(className, objectClassName);

    return returnedJacobianMatrix;



}

/*
 * Class:     rpnumerics_WaveFlow
 * Method:    fluxDeriv2
 * Signature: (Lwave/util/RealVector;)Lwave/util/HessianMatrix;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_WaveFlow_fluxDeriv2(JNIEnv * env, jobject obj, jobject realVector) {

    //Classes references

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass hessianMatrixClass = env->FindClass(HESSIANMATRIX_LOCATION);


    jclass shockFlowClass = env->FindClass(SHOCKFLOW_LOCATION);
    jclass rarefactionFlowClass = env->FindClass(RAREFACTIONFLOW_LOCATION);


    jclass objectClass = env->FindClass("java/lang/Object");
    jclass clsClass = env->FindClass("java/lang/Class");


    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID getSizeMethodID = (env)->GetMethodID(realVectorClass, "getSize", "()I");

    jmethodID hessianMatrixConstructorID = (env)->GetMethodID(hessianMatrixClass, "<init>", "(I)V");
    jmethodID setElementMethodID = (env)->GetMethodID(hessianMatrixClass, "setElement", "(IIID)V");


    jmethodID getSigmaMethodID = (env)->GetMethodID(shockFlowClass, "getSigma", "()D");
    jmethodID getXZeroMethodID = (env)->GetMethodID(shockFlowClass, "getXZero", "()Lrpnumerics/PhasePoint;");


    jmethodID getReferenceVectorMethodID = (env)->GetMethodID(rarefactionFlowClass, "getReferenceVector", "()Lrpnumerics/PhasePoint;");
    jmethodID getFamilyMethodID = (env)->GetMethodID(rarefactionFlowClass, "getFamily", "()I");


    jmethodID getClassID = (env)->GetMethodID(objectClass, "getClass", "()Ljava/lang/Class;");
    jmethodID getClassNameID = (env)->GetMethodID(clsClass, "getSimpleName", "()Ljava/lang/String;");

    //Catching the object class name

    jobject classObj = (env)->CallObjectMethod(obj, getClassID);
    jstring className = (jstring) (env)->CallObjectMethod(classObj, getClassNameID);
    const char *objectClassName;
    objectClassName = env->GetStringUTFChars(className, NULL);

    //Getting the dimension
    int dimension = env->CallIntMethod(realVector, getSizeMethodID);

    //Processing the input

    jdoubleArray realVectorArray = (jdoubleArray) (env)->CallObjectMethod(realVector, toDoubleMethodID);
    double input [dimension];

    env->GetDoubleArrayRegion(realVectorArray, 0, dimension, input);

    //Creating input and output objects


    RealVector realVectorInput(dimension, input);
    HessianMatrix hessianMatrixOutput(dimension);


    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    if (!strcmp(objectClassName, "ShockFlow")) {

        jdouble sigma = (env)->CallDoubleMethod(obj, getSigmaMethodID); // <--- sigma

        jobject xzero = (env)->CallObjectMethod(obj, getXZeroMethodID);

        jdoubleArray xzeroArray = (jdoubleArray) (env)->CallObjectMethod(xzero, toDoubleMethodID);

        int xzeroLength = env->GetArrayLength(xzeroArray);

        double nativeXZeroArray [xzeroLength]; // <--- XZero

        env->GetDoubleArrayRegion(xzeroArray, 0, xzeroLength, nativeXZeroArray);

        //                for (int i = 0; i < xzeroLength; i++) {
        //                    cout << "XZero: " << nativeXZeroArray [i] << "\n";
        //                }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("ShockFlow");

        ShockFlowPlugin* flow = (ShockFlowPlugin *) plug;

        PhasePoint phasePoint(RealVector(xzeroLength, nativeXZeroArray));

        ShockFlowParams newParams(phasePoint, sigma);

        //Setting the new parameters
        flow->setParams(newParams);

        flow->setFluxFunction(fluxFunction);

        flow->fluxDeriv2(realVectorInput, hessianMatrixOutput);

        RPnPluginManager::unload(plug, "ShockFlow");

    }

    if (!strcmp(objectClassName, "RarefactionFlow")) {


        jint family = (env)->CallIntMethod(obj, getFamilyMethodID); // <--- Family

        jobject referenceVector = (env)->CallObjectMethod(obj, getReferenceVectorMethodID);

        jdoubleArray referenceArray = (jdoubleArray) (env)->CallObjectMethod(referenceVector, toDoubleMethodID);

        int referenceLength = env->GetArrayLength(referenceArray);

        double nativeReferenceArray [referenceLength]; // <--- Reference Vector

        env->GetDoubleArrayRegion(referenceArray, 0, referenceLength, nativeReferenceArray);

        cout << "Family: " << family << "\n";

        for (int i = 0; i < referenceLength; i++) {
            cout << "Reference Vector: " << nativeReferenceArray [i] << "\n";
        }

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("RarefactionFlow");

        RarefactionFlowPlugin* flow = (RarefactionFlowPlugin *) plug;

        flow->setFamilyIndex(family);
        flow->setReferenceVector(RealVector(dimension, nativeReferenceArray));
        flow->setFluxFunction(fluxFunction);

        flow->fluxDeriv2(realVectorInput, hessianMatrixOutput);

        RPnPluginManager::unload(plug, "RarefactionFlow");

    }

    jobject returnedHessianMatrix = env->NewObject(hessianMatrixClass, hessianMatrixConstructorID, dimension);

    for (int i = 0; i < dimension; i++) {
        for (int j = 0; j < dimension; j++) {
            for (int k = 0; k < dimension; k++) {
                env->CallVoidMethod(returnedHessianMatrix, setElementMethodID, i, j, k, hessianMatrixOutput(i, j, k));
            }
        }
    }
    env->ReleaseStringUTFChars(className, objectClassName);

    return returnedHessianMatrix;
}



