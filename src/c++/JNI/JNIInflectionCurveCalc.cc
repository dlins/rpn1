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


#include "rpnumerics_InflectionCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "Inflection_Curve.h"
#include "TPCW.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_InflectionCurveCalc_nativeCalc__I
(JNIEnv * env, jobject obj, jint family) {

    //JNIEXPORT jobject JNICALL Java_rpnumerics_InflectionCurveCalc_nativeCalc(JNIEnv * env, jobject obj, jint family) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass inflectionCurveClass = env->FindClass(INFLECTIONCURVE_LOCATION);


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID inflectionCurveConstructor = env->GetMethodID(inflectionCurveClass, "<init>", "(Ljava/util/List;)V");

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    int dimension = RpNumerics::getPhysics().domain().dim();

    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcation");

    Inflection_Curve inflectionCurve;

    std::vector<RealVector> left_vrs;

    inflectionCurve.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(), *gv, family, left_vrs);

    if (left_vrs.size() == 0)
        return NULL;

    for (int i = 0; i < left_vrs.size() / 2; i++) {

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i + 1]);

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) left_vrs[2 * i];
        double * rightCoords = (double *) left_vrs[2 * i + 1];

        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
    }




    jobject result = env->NewObject(inflectionCurveClass, inflectionCurveConstructor, segmentsArray);


    IF_DEBUG
        if (result == NULL) cout << "Eh nulo" << endl;
    END_DEBUG

    // Limpando
    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}

JNIEXPORT jobject JNICALL Java_rpnumerics_InflectionCurveCalc_nativeCalc__IIILwave_util_RealVector_2Lwave_util_RealVector_2
  (JNIEnv *env, jobject obj, jint family,jint xRes, jint yRes, jobject topR, jobject dwnL){

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass inflectionCurveClass = env->FindClass(INFLECTIONCURVE_LOCATION);
    
    
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID inflectionCurveConstructor = env->GetMethodID(inflectionCurveClass, "<init>", "(Ljava/util/List;)V");



    int dimension = RpNumerics::getPhysics().domain().dim();

//Area processing
    
    

    jdoubleArray topArray = (jdoubleArray) (env)->CallObjectMethod(topR, toDoubleMethodID);

    jdoubleArray downArray = (jdoubleArray) (env)->CallObjectMethod(dwnL, toDoubleMethodID);

    int topLength = env->GetArrayLength(topArray);
    int downLength = env->GetArrayLength(downArray);

    double topDimension [topLength];
    double downDimension [downLength];

    RealVector pMin(2);
    RealVector pMax(2);


    env->GetDoubleArrayRegion(topArray, 0, topLength, topDimension);
    env->GetDoubleArrayRegion(downArray, 0, downLength, downDimension);


    pMin.component(0) = downDimension[0];
    pMin.component(1) = downDimension[1];

    pMax.component(0) = topDimension[0];
    pMax.component(1) = topDimension[1];

    vector<int> resolution;

    resolution.push_back(xRes);
    resolution.push_back(yRes);


    GridValues gv(&RpNumerics::getPhysics().boundary(), pMin, pMax, resolution);

    Inflection_Curve inflectionCurve;

    std::vector<RealVector> left_vrs;

    inflectionCurve.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(), gv, family, left_vrs);



    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    if (left_vrs.size() == 0)
        return NULL;

    for (int i = 0; i < left_vrs.size() / 2; i++) {

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i + 1]);

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) left_vrs[2 * i];
        double * rightCoords = (double *) left_vrs[2 * i + 1];

        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
    }




    jobject result = env->NewObject(inflectionCurveClass, inflectionCurveConstructor, segmentsArray);


    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;



}










