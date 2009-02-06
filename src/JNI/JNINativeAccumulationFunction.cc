/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNINativeAccumulationFunction.cc
 **/

//! Definition of JNIAccumulationFuctionFacade
/*!
 *
 * TODO:
 *
 * NOTE :
 *
 * @ingroup JNI
 */

#include "AccumulationFunction.h"
#include "RpNumerics.h"
#include "rpnumerics_AccumulationFunction.h"
#include "JNIDefs.h"
#include <iostream>

JNIEXPORT jint JNICALL Java_rpnumerics_AccumulationFunction_nativeJet(JNIEnv * env, jobject obj, jobject waveState, jobject jetMatrix, jint degree) {

    //Classes references

    jclass phasePointClass = env->FindClass(PHASEPOINT_LOCATION);

    jclass waveStateClass = env->FindClass(WAVESTATE_LOCATION);
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);


    jclass accumulationFunctionClass = env->FindClass(ACCUMULATIONFUNCTION_LOCATION);
    jclass accumulationParamsClass = env->FindClass(ACCUMULATIONPARAMS_LOCATION);

    //Methods references

    jmethodID toDoubleMethodID = (env)->GetMethodID(phasePointClass, "toDouble", "()[D");

    jmethodID waveStateDimMethodID = env->GetMethodID(waveStateClass, "stateSpaceDim", "()I");
    jmethodID waveStateIntialPointID = env->GetMethodID(waveStateClass, "initialState", "()Lrpnumerics/PhasePoint;");

    jmethodID jetMatrixsetF = env->GetMethodID(jetMatrixClass, "setElement", "(ID)V");
    jmethodID jetMatrixsetDF = env->GetMethodID(jetMatrixClass, "setElement", "(IID)V");
    jmethodID jetMatrixsetD2F = env->GetMethodID(jetMatrixClass, "setElement", "(IIID)V");

    jmethodID getaccumulationParamsMethodID = (env)->GetMethodID(accumulationFunctionClass, "accumulationParams", "()Lrpnumerics/AccumulationParams;");
    jmethodID getParamsMethodID = (env)->GetMethodID(accumulationParamsClass, "getParams", "()Lwave/util/RealVector;");


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

    //Getting the native accumulation function

    const AccumulationFunction & accumulationFunction = RpNumerics::getAccumulation();

    jobject accumulationParamsObj = (env)->CallObjectMethod(obj, getaccumulationParamsMethodID);

    jobject realVectorObj = (env)->CallObjectMethod(accumulationParamsObj, getParamsMethodID);

    jdoubleArray paramsArray = (jdoubleArray) (env)->CallObjectMethod(realVectorObj, toDoubleMethodID);

    int paramsLength = env->GetArrayLength(paramsArray);

    double nativeParamsArray [paramsLength];

    env->GetDoubleArrayRegion(paramsArray, 0, paramsLength, nativeParamsArray);

    Physics & physics = RpNumerics::getPhysics();

    AccumulationParams nativeAccumulationParams(paramsLength, nativeParamsArray);

    //Setting the new parameters
    
    physics.accumulationParams(nativeAccumulationParams);

    accumulationFunction.jet(nativeWaveState, nativeJetMatrix, degree);


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
 * Class:     rpnumerics_NativeAccumulationFunction
 * Method:    nativeJet
 * Signature: (Lwave/util/RealVector;Lwave/util/JetMatrix;I)I
 */
JNIEXPORT jint JNICALL Java_rpnumerics_AccumulationFunction_nativeVectorJet(JNIEnv * env, jobject obj, jobject realVector, jobject jetMatrix, jint degree) {

    //Classes references
    jclass jetMatrixClass = env->FindClass(JETMATRIX_LOCATION);

    jclass phasePointClass = env->FindClass(PHASEPOINT_LOCATION);

    jclass accumulationFunctionClass = env->FindClass(ACCUMULATIONFUNCTION_LOCATION);
    jclass accumulationParamsClass = env->FindClass(ACCUMULATIONPARAMS_LOCATION);

    //Methods ID

    jmethodID toDoubleMethodID = (env)->GetMethodID(phasePointClass, "toDouble", "()[D");


    jmethodID getaccumulationParamsMethodID = (env)->GetMethodID(accumulationFunctionClass, "accumulationParams", "()Lrpnumerics/AccumulationParams;");
    jmethodID getParamsMethodID = (env)->GetMethodID(accumulationParamsClass, "getParams", "()Lwave/util/RealVector;");


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

    //Getting the native accumulation function

    const AccumulationFunction & accumulationFunction = RpNumerics::getAccumulation();

    jobject accumulationParamsObj = (env)->CallObjectMethod(obj, getaccumulationParamsMethodID);

    jobject realVectorObj = (env)->CallObjectMethod(accumulationParamsObj, getParamsMethodID);

    jdoubleArray paramsArray = (jdoubleArray) (env)->CallObjectMethod(realVectorObj, toDoubleMethodID);

    int paramsLength = env->GetArrayLength(paramsArray);

    double nativeParamsArray [paramsLength];

    env->GetDoubleArrayRegion(paramsArray, 0, paramsLength, nativeParamsArray);

    Physics & physics = RpNumerics::getPhysics();

    AccumulationParams nativeaccumulationParams(paramsLength, nativeParamsArray);

    //Setting the new parameters

    physics.accumulationParams(nativeaccumulationParams);

    accumulationFunction.jet(nativeWaveState, nativeJetMatrix, degree);

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



