/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNILevelCurveCalc.cc
 **/


//! Definition of JNILevelCurveCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_PointLevelCalc.h"
#include "rpnumerics_LevelCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "Eigenvalue_Contour.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_PointLevelCalc_calcNative(JNIEnv * env, jobject obj, jint family, jobject initialPoint, jintArray resolution) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass doubleClass = env->FindClass("java/lang/Double");

    jclass levelCurveClass = env->FindClass(LEVELCURVE_LOCATION);



    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");


    jmethodID doubleConstructor = env->GetMethodID(doubleClass, "<init>", "(D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID levelCurveConstructor = env->GetMethodID(levelCurveClass, "<init>", "(ILjava/util/List;D)V");


    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);





    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (int i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);



    int dimension = RpNumerics::getPhysics().domain().dim();

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();


    int cells [dimension];

    env->GetIntArrayRegion(resolution, 0, dimension, cells);



    Eigenvalue_Contour ec;

    ec.set_level_from_point(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(),
            family, realVectorInput);

    vector < RealVector > eigen_contours;
    double vec_levels;


    GridValues & gv = RpNumerics::getPhysics().getGrid(0);

    ec.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(),
            gv, eigen_contours, vec_levels);




    if (eigen_contours.size() == 0)
        return NULL;



    for (int i = 0; i < eigen_contours.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) eigen_contours[2 * i];
        double * rightCoords = (double *) eigen_contours[2 * i + 1];


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);

    }



    jobject result = env->NewObject(levelCurveClass, levelCurveConstructor, family, segmentsArray, vec_levels);

    // Limpando

    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}

JNIEXPORT jobject JNICALL Java_rpnumerics_LevelCurveCalc_calcNative(JNIEnv * env, jobject obj, jint family, jdouble level, jintArray resolution) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass doubleClass = env->FindClass("java/lang/Double");

    jclass levelCurveClass = env->FindClass(LEVELCURVE_LOCATION);



    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");


    jmethodID doubleConstructor = env->GetMethodID(doubleClass, "<init>", "(D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID levelCurveConstructor = env->GetMethodID(levelCurveClass, "<init>", "(ILjava/util/List;D)V");


    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);



    int dimension = RpNumerics::getPhysics().domain().dim();

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();


    int cells [dimension];

    env->GetIntArrayRegion(resolution, 0, dimension, cells);

    Eigenvalue_Contour ec;

    ec.set_level(level, family);

    vector < RealVector > eigen_contours;

    double vec_levels;

    GridValues & gv = RpNumerics::getPhysics().getGrid(0);

    ec.curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(),
            gv, eigen_contours, vec_levels);

    //    ec.curve(eigen_contours, vec_levels);

    if (eigen_contours.size() == 0)
        return NULL;

    for (int i = 0; i < eigen_contours.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) eigen_contours[2 * i];
        double * rightCoords = (double *) eigen_contours[2 * i + 1];


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);

    }



    jobject result = env->NewObject(levelCurveClass, levelCurveConstructor, family, segmentsArray, vec_levels);

    // Limpando

    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}














