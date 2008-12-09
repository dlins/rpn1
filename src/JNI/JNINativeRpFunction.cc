/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIOrbitCalc.cc
 **/

//! Definition of JNIFluxFuctionFacade
/*!
 *
 * TODO:
 *
 * NOTE :
 *
 * @ingroup JNI
 */

#include "FluxFunction.h"
#include "RpNumerics.h"
#include "rpnumerics_NativeRpFunction.h"
#include "Quad2FluxParams.h"
#include "JNIDefs.h"
#include "PluginService.h"
#include "WaveFlowPlugin.h"
#include "RPnPluginManager.h"
#include <iostream>

JNIEXPORT jint JNICALL Java_rpnumerics_NativeRpFunction_nativeJet(JNIEnv * env, jobject obj, jobject waveState, jobject jetMatrix, jint degree) {

    //Classes references

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass phasePointClass = env->FindClass(PHASEPOINT_LOCATION);

    jclass waveStateClass = env->FindClass(WAVESTATE_LOCATION);
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);

    jclass shockFlowClass = env->FindClass(SHOCKFLOW_LOCATION);
    jclass rarefactionFlowClass = env->FindClass(RAREFACTIONFLOW_LOCATION);



    jclass fluxFunctionClass = env->FindClass(FLUXFUNCTION_LOCATION);
    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);

    jclass objectClass = env->FindClass("java/lang/Object");
    jclass clsClass = env->FindClass("java/lang/Class");


    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(phasePointClass, "toDouble", "()[D");

    jmethodID waveStateDimMethodID = env->GetMethodID(waveStateClass, "stateSpaceDim", "()I");
    jmethodID waveStateIntialPointID = env->GetMethodID(waveStateClass, "initialState", "()Lrpnumerics/PhasePoint;");

    jmethodID jetMatrixsetF = env->GetMethodID(jetMatrixClass, "setElement", "(ID)V");
    jmethodID jetMatrixsetDF = env->GetMethodID(jetMatrixClass, "setElement", "(IID)V");
    jmethodID jetMatrixsetD2F = env->GetMethodID(jetMatrixClass, "setElement", "(IIID)V");



    jmethodID getfluxParamsMethodID = (env)->GetMethodID(fluxFunctionClass, "fluxParams", "()Lrpnumerics/FluxParams;");
    jmethodID getParamsMethodID = (env)->GetMethodID(fluxParamsClass, "getParams", "()Lwave/util/RealVector;");
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
    int dimension = env->CallIntMethod(waveState, waveStateDimMethodID);

    //Processing the input

    jobject initialPoint = env->CallObjectMethod(waveState, waveStateIntialPointID); //Assuming initial point as WaveState data (???)
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    double input [dimension];

    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    //Creating native WaveState

    WaveState nativeWaveState(dimension);

    for (int i = 0; i < dimension; i++) {
        nativeWaveState(i) = input[i];
    }


    //Creating native JetMatrix

    JetMatrix nativeJetMatrix(dimension);

    //Calling the function

    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    //        cout << objectClassName << "\n";


    if (!strcmp(objectClassName, "FluxFunction")) {

        jobject fluxParamsObj = (env)->CallObjectMethod(obj, getfluxParamsMethodID);

        jobject realVectorObj = (env)->CallObjectMethod(fluxParamsObj, getParamsMethodID);

        jdoubleArray paramsArray = (jdoubleArray) (env)->CallObjectMethod(realVectorObj, toDoubleMethodID);

        int paramsLength = env->GetArrayLength(paramsArray);

        double nativeParamsArray [paramsLength];

        env->GetDoubleArrayRegion(paramsArray, 0, paramsLength, nativeParamsArray);

        Physics & physics = RpNumerics::getPhysics();

        FluxParams nativeFluxParams(paramsLength, nativeParamsArray);

        physics.fluxParams(nativeFluxParams);

        fluxFunction.jet(nativeWaveState, nativeJetMatrix, degree);

    }


    if (!strcmp(objectClassName, "ShockFlow")) {


        jdouble sigma = (env)->CallDoubleMethod(obj, getSigmaMethodID); // <--- sigma

        jobject xzero = (env)->CallObjectMethod(obj, getXZeroMethodID);

        jdoubleArray xzeroArray = (jdoubleArray) (env)->CallObjectMethod(xzero, toDoubleMethodID);

        int xzeroLength = env->GetArrayLength(xzeroArray);

        double nativeXZeroArray [xzeroLength]; // <--- XZero


        for (int i = 0; i < xzeroLength; i++) {
            cout << "XZero: " << nativeXZeroArray [i] << "\n";
        }


        env->GetDoubleArrayRegion(xzeroArray, 0, xzeroLength, nativeXZeroArray);

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("ShockFlow");

        WaveFlow* flow = (WaveFlow*) plug;

        flow->jet(nativeWaveState, nativeJetMatrix, degree);

        RPnPluginManager::unload(plug, "ShockFlow");

    }

    if (!strcmp(objectClassName, "RarefactionFlow")) {


        jint family = (env)->CallIntMethod(obj, getFamilyMethodID); // <--- Family

        jobject referenceVector = (env)->CallObjectMethod(obj, getReferenceVectorMethodID);

        jdoubleArray referenceArray = (jdoubleArray) (env)->CallObjectMethod(referenceVector, toDoubleMethodID);

        int referenceLength = env->GetArrayLength(referenceArray);

        double nativeReferenceArray [referenceLength]; // <--- Reference Vector

        cout << "Family: " << family << "\n";

        for (int i = 0; i < referenceLength; i++) {
            cout << "Reference Vector: " << nativeReferenceArray [i] << "\n";
        }


    }



    env->ReleaseStringUTFChars(className, objectClassName);


    //Filling the output jet matrix
    switch (degree) {
        case 0:
            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }
            break;
        case 1:

            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }

            for (int i = 0; i < dimension; i++) { //DF
                for (int j = 0; j < dimension; j++) {
                    env->CallVoidMethod(jetMatrix, jetMatrixsetDF, i, j, nativeJetMatrix(i, j));
                }
            }
            break;
        case 2:

            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }

            for (int i = 0; i < dimension; i++) { //DF
                for (int j = 0; j < dimension; j++) {
                    env->CallVoidMethod(jetMatrix, jetMatrixsetDF, i, j, nativeJetMatrix(i, j));
                }
            }


            for (int i = 0; i < dimension; i++) { //D2F
                for (int j = 0; j < dimension; j++) {
                    for (int k = 0; k < dimension; k++) {
                        env->CallVoidMethod(jetMatrix, jetMatrixsetD2F, i, j, k, nativeJetMatrix(i, j, k));
                    }

                }
            }

            break;
        default:
            cout << "Error in derivative degree" << endl;
    }
    return 0;

}

/*
 * Class:     rpnumerics_NativeRpFunction
 * Method:    nativeJet
 * Signature: (Lwave/util/RealVector;Lwave/util/JetMatrix;I)I
 */
JNIEXPORT jint JNICALL Java_rpnumerics_NativeRpFunction_nativeVectorJet(JNIEnv * env, jobject obj, jobject realVector, jobject jetMatrix, jint degree) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);
    jclass objectClass = env->FindClass("java/lang/Object");
    jclass clsClass = env->FindClass("java/lang/Class");

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID getClassID = (env)->GetMethodID(objectClass, "getClass", "()Ljava/lang/Class;");
    jmethodID getClassNameID = (env)->GetMethodID(clsClass, "getSimpleName", "()Ljava/lang/String;");


    jmethodID jetMatrixsetF = env->GetMethodID(jetMatrixClass, "setElement", "(ID)V");
    jmethodID jetMatrixsetDF = env->GetMethodID(jetMatrixClass, "setElement", "(IID)V");
    jmethodID jetMatrixsetD2F = env->GetMethodID(jetMatrixClass, "setElement", "(IIID)V");

    //Catching the object class name

    jobject classObj = (env)->CallObjectMethod(obj, getClassID);

    jstring className = (jstring) (env)->CallObjectMethod(classObj, getClassNameID);

    const char *objectClassName;

    objectClassName = env->GetStringUTFChars(className, NULL);

    //Input processing
    jdoubleArray realVectorDoubleArray = (jdoubleArray) (env)->CallObjectMethod(realVector, toDoubleMethodID);

    int dimension = env->GetArrayLength(realVectorDoubleArray);

    double input [dimension];

    env->GetDoubleArrayRegion(realVectorDoubleArray, 0, dimension, input);

    RealVector nativeRealVector(dimension, input);

    env->DeleteLocalRef(realVectorDoubleArray);

    WaveState nativeWaveState(nativeRealVector);


    //Creating native JetMatrix

    JetMatrix nativeJetMatrix(dimension);

    //Calling the function


    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    if (!strcmp(objectClassName, "FluxFunction")) {

        fluxFunction.jet(nativeWaveState, nativeJetMatrix, degree);

    } else {

        RpnPlugin * plug = RPnPluginManager::getPluginInstance("WaveFlow");

        WaveFlow * flow = (WaveFlow *) plug;

        flow->jet(nativeWaveState, nativeJetMatrix, degree);

        RPnPluginManager::unload(plug, "WaveFlow");



    }

    env->ReleaseStringUTFChars(className, objectClassName);


    //Filling the output jet matrix
    switch (degree) {
        case 0:
            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }
            break;
        case 1:

            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }

            for (int i = 0; i < dimension; i++) { //DF
                for (int j = 0; j < dimension; j++) {
                    env->CallVoidMethod(jetMatrix, jetMatrixsetDF, i, j, nativeJetMatrix(i, j));
                }
            }
            break;
        case 2:

            for (int i = 0; i < dimension; i++) { //F
                env->CallVoidMethod(jetMatrix, jetMatrixsetF, i, nativeJetMatrix(i));
            }

            for (int i = 0; i < dimension; i++) { //DF
                for (int j = 0; j < dimension; j++) {
                    env->CallVoidMethod(jetMatrix, jetMatrixsetDF, i, j, nativeJetMatrix(i, j));
                }
            }


            for (int i = 0; i < dimension; i++) { //D2F
                for (int j = 0; j < dimension; j++) {
                    for (int k = 0; k < dimension; k++) {
                        env->CallVoidMethod(jetMatrix, jetMatrixsetD2F, i, j, k, nativeJetMatrix(i, j, k));
                    }

                }
            }

            break;
        default:
            cout << "Error in derivative degree" << endl;
    }

    return 0;

}



