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

#include "wave_multid_model_MultiPolygon.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include "Debug.h"
#include <vector>
#include <iostream>



using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_wave_multid_model_MultiPolygon_convexHull
(JNIEnv *env, jobject obj, jobject listOfPoints) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jmethodID listSizeMethodID = env->GetMethodID(arrayListClass, "size", "()I");

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID getListMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");


    int n = env->CallIntMethod(listOfPoints,listSizeMethodID);
    int dimension;
    vector<RealVector> points;

    for (int i = 0; i < n; i++) {
        jobject realVector = env->CallObjectMethod(listOfPoints, getListMethod,  i);
        jdoubleArray realVectorArray = (jdoubleArray) (env)->CallObjectMethod(realVector, toDoubleMethodID);
        dimension = env->GetArrayLength(realVectorArray);
        double realVectorBuffer [dimension];
        env->GetDoubleArrayRegion(realVectorArray, 0, dimension, realVectorBuffer);
        RealVector nativePoint(dimension, realVectorBuffer);
        points.push_back(nativePoint);

    }


    vector<RealVector> convexH;
    convex_hull(points, convexH);
    jobject result = env->NewObject(arrayListClass, arrayListConstructor);

    for (int i = 0; i < convexH.size(); i++) {
        jdoubleArray realVectorArray = env->NewDoubleArray(dimension);
        double * convexCoords = (double *) convexH[i];
        env->SetDoubleArrayRegion(realVectorArray, 0, dimension, convexCoords);
        jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, realVectorArray);
        env->CallBooleanMethod(result, arrayListAddMethod, realVector);

    }


    return result;

}

