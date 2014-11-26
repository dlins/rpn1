/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/


//! Definition of JNIHugoniotCurveCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_StateInformation.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include <iosfwd>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_StateInformation_getStateInformation
(JNIEnv * env, jobject obj, jobject inputPoint) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    jclass stateInfoClass = env->FindClass(RPNSTATEINFO);



    //
    jclass hashMapClass = env->FindClass("java/util/HashMap");
    //
    //    jclass ellipticBoundaryClass = env->FindClass(ELLIPTICBOUNDARY_LOCATION);
    //
    //
    //    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    //


    //

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID hashMapConstructor = env->GetMethodID(hashMapClass, "<init>", "()V");

    jmethodID putHashMethod = env->GetMethodID(hashMapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");


    jobject hashMapInformation = env->NewObject(hashMapClass, hashMapConstructor);


    jmethodID rpnStateInfoConstructor = env-> GetMethodID(stateInfoClass, "<init>", "(Ljava/util/HashMap;)V");





    //Input processing
    jdoubleArray inputPointArray = (jdoubleArray) (env)->CallObjectMethod(inputPoint, toDoubleMethodID);

    int dimension = env->GetArrayLength(inputPointArray);

    double input [dimension];

    env->GetDoubleArrayRegion(inputPointArray, 0, dimension, input);

    env->DeleteLocalRef(inputPointArray);


    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();

    RealVector inputPointNative(dimension, input);



    std::ostringstream fluxFunctionStream;

    WaveState inputState(inputPointNative);

    JetMatrix output(dimension);


    int jetOutput = flux->jet(inputState, output, 2);

    fluxFunctionStream << output;

    jstring fluxInformation = env->NewStringUTF(fluxFunctionStream.str().c_str());

    jstring fluxKey = env->NewStringUTF("fluxfunction");

    env->CallObjectMethod(hashMapInformation, putHashMethod, fluxKey, fluxInformation);


    //Accumulation

    std::ostringstream accumulationFunctionStream;

    JetMatrix outputAccumulation(dimension);

    jetOutput = accum->jet(inputState, outputAccumulation, 2);

    accumulationFunctionStream << outputAccumulation;

    jstring accumulationInformation = env->NewStringUTF(accumulationFunctionStream.str().c_str());

    jstring accumulationKey = env->NewStringUTF("accumulationfunction");

    env->CallObjectMethod(hashMapInformation, putHashMethod, accumulationKey, accumulationInformation);


    jobject result = env->NewObject(stateInfoClass, rpnStateInfoConstructor, hashMapInformation);

    return result;



}
















