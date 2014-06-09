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


#include "rpnumerics_EigenValuePointLevelCalc.h"
#include "rpnumerics_EigenValueLevelCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "CharacteristicPolynomialLevels.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_EigenValuePointLevelCalc_calcNative(JNIEnv * env, jobject obj, jint family, jobject initialPoint) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass eigenValueCurveClass = env->FindClass(EIGENVALUECURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID levelCurveConstructor = env->GetMethodID(eigenValueCurveClass, "<init>", "(ILjava/util/List;D)V");


    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (int i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }


    int dimension = RpNumerics::getPhysics().domain().dim();



    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);

    if (realVectorInput.size() == 3) {//TODO REMOVE !!
        realVectorInput.component(2) = 1.0;
    }
   

    vector < RealVector > eigen_contours;
    double level;

    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
    
    
     CharacteristicPolynomialLevels ec;
    
    ec.eigenvalue_curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(),
                               *gv, 
                               realVectorInput, family, 
                               eigen_contours, level);



    if (eigen_contours.size() == 0)
        return NULL;

    for (int i = 0; i < eigen_contours.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);



        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i + 1]);

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



    jobject result = env->NewObject(eigenValueCurveClass, levelCurveConstructor, family, segmentsArray, level);

    // Limpando

    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}

JNIEXPORT jobject JNICALL Java_rpnumerics_EigenValueLevelCalc_calcNative(JNIEnv * env, jobject obj, jint family, jdouble level) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass eigenValueCurveClass = env->FindClass(EIGENVALUECURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");


    jmethodID levelCurveConstructor = env->GetMethodID(eigenValueCurveClass, "<init>", "(ILjava/util/List;D)V");


    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    int dimension = RpNumerics::getPhysics().domain().dim();


    cout << "Chamando curva de nivel sem ponto"<<level << endl;



    vector < RealVector > eigen_contours;

//    double vec_levels;

    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");

    CharacteristicPolynomialLevels ec;

    ec.eigenvalue_curve(& RpNumerics::getPhysics().fluxFunction(), & RpNumerics::getPhysics().accumulation(),
            *gv, level, family, eigen_contours);


    if (eigen_contours.size() == 0)
        return NULL;

    for (int i = 0; i < eigen_contours.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i + 1]);


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



    jobject result = env->NewObject(eigenValueCurveClass, levelCurveConstructor, family, segmentsArray, level);

    // Limpando

    env->DeleteLocalRef(realSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);


    return result;


}














