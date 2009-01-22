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
#include "rpnumerics_NativeFluxFunction.h"
#include "JNIDefs.h"
#include <iostream>

JNIEXPORT jint JNICALL Java_rpnumerics_NativeFluxFunction_nativeJet(JNIEnv * env, jobject obj, jobject waveState, jobject jetMatrix, jint degree) {

    //Classes references

    jclass phasePointClass = env->FindClass(PHASEPOINT_LOCATION);

    jclass waveStateClass = env->FindClass(WAVESTATE_LOCATION);
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);


    jclass fluxFunctionClass = env->FindClass(FLUXFUNCTION_LOCATION);
    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);

    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(phasePointClass, "toDouble", "()[D");

    jmethodID waveStateDimMethodID = env->GetMethodID(waveStateClass, "stateSpaceDim", "()I");
    jmethodID waveStateIntialPointID = env->GetMethodID(waveStateClass, "initialState", "()Lrpnumerics/PhasePoint;");

    jmethodID jetMatrixsetF = env->GetMethodID(jetMatrixClass, "setElement", "(ID)V");
    jmethodID jetMatrixsetDF = env->GetMethodID(jetMatrixClass, "setElement", "(IID)V");
    jmethodID jetMatrixsetD2F = env->GetMethodID(jetMatrixClass, "setElement", "(IIID)V");

    jmethodID getfluxParamsMethodID = (env)->GetMethodID(fluxFunctionClass, "fluxParams", "()Lrpnumerics/FluxParams;");
    jmethodID getParamsMethodID = (env)->GetMethodID(fluxParamsClass, "getParams", "()Lwave/util/RealVector;");


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

    //Getting the native flux function

    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    jobject fluxParamsObj = (env)->CallObjectMethod(obj, getfluxParamsMethodID);

    jobject realVectorObj = (env)->CallObjectMethod(fluxParamsObj, getParamsMethodID);

    jdoubleArray paramsArray = (jdoubleArray) (env)->CallObjectMethod(realVectorObj, toDoubleMethodID);

    int paramsLength = env->GetArrayLength(paramsArray);

    double nativeParamsArray [paramsLength];

    env->GetDoubleArrayRegion(paramsArray, 0, paramsLength, nativeParamsArray);

    Physics & physics = RpNumerics::getPhysics();

    FluxParams nativeFluxParams(paramsLength, nativeParamsArray);

    //Setting the new parameters
    
    physics.fluxParams(nativeFluxParams);

    fluxFunction.jet(nativeWaveState, nativeJetMatrix, degree);


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
JNIEXPORT jint JNICALL Java_rpnumerics_NativeFluxFunction_nativeVectorJet(JNIEnv * env, jobject obj, jobject realVector, jobject jetMatrix, jint degree) {

    //Classes references
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);

    jclass phasePointClass = env->FindClass(PHASEPOINT_LOCATION);

    jclass fluxFunctionClass = env->FindClass(FLUXFUNCTION_LOCATION);
    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);

    //Methods ID

    jmethodID toDoubleMethodID = (env)->GetMethodID(phasePointClass, "toDouble", "()[D");


    jmethodID getfluxParamsMethodID = (env)->GetMethodID(fluxFunctionClass, "fluxParams", "()Lrpnumerics/FluxParams;");
    jmethodID getParamsMethodID = (env)->GetMethodID(fluxParamsClass, "getParams", "()Lwave/util/RealVector;");


    jmethodID jetMatrixsetF = env->GetMethodID(jetMatrixClass, "setElement", "(ID)V");
    jmethodID jetMatrixsetDF = env->GetMethodID(jetMatrixClass, "setElement", "(IID)V");
    jmethodID jetMatrixsetD2F = env->GetMethodID(jetMatrixClass, "setElement", "(IIID)V");

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

    //Getting the native flux function

    const FluxFunction & fluxFunction = RpNumerics::getFlux();

    jobject fluxParamsObj = (env)->CallObjectMethod(obj, getfluxParamsMethodID);

    jobject realVectorObj = (env)->CallObjectMethod(fluxParamsObj, getParamsMethodID);

    jdoubleArray paramsArray = (jdoubleArray) (env)->CallObjectMethod(realVectorObj, toDoubleMethodID);

    int paramsLength = env->GetArrayLength(paramsArray);

    double nativeParamsArray [paramsLength];

    env->GetDoubleArrayRegion(paramsArray, 0, paramsLength, nativeParamsArray);

    Physics & physics = RpNumerics::getPhysics();

    FluxParams nativeFluxParams(paramsLength, nativeParamsArray);

    //Setting the new parameters

    physics.fluxParams(nativeFluxParams);

    fluxFunction.jet(nativeWaveState, nativeJetMatrix, degree);

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



