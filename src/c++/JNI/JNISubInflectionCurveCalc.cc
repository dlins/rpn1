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


#include "rpnumerics_SubInflectionCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "SubinflectionTP.h"



using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_SubInflectionCurveCalc_nativeCalc(JNIEnv * env, jobject obj) {

//
//    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
//
//    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
//
//    jclass coincidenceCurveClass = env->FindClass(COINCIDENCECURVE_LOCATION);
//    
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass subinflectionCurveClass = env->FindClass(SUBINFLECTIONCURVE_LOCATION);
//
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//
//    jmethodID subinflectionCurveConstructor = env->GetMethodID(subinflectionCurveClass, "<init>", "(Ljava/util/List;)V");
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//
//
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//
//    //    Test testFunction;
//    int dimension = 3;
//    //-------------------------------------------------------------------
//
//    TPCW & tpcw = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
//
//
//    Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized *) & tpcw.fluxFunction();
//
//    Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) & tpcw.accumulation();
//
//
//    Thermodynamics * thermo = fluxFunction->getThermo();
//
//
//    SubinflectionTP newSubinflection(accumulationFunction->accumulationParams().component(0));
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
//
//
//
//    std::vector< RealVector> outputVector;
//
//    newSubinflection.curve(fluxFunction, accumulationFunction, *gv, outputVector);
//
//
//    for (int i = 0; i < outputVector.size() / 2; i++) {
//
//        tpcw.postProcess(outputVector[2 * i]);
//        tpcw.postProcess(outputVector[2 * i + 1]);
//
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//        double * leftCoords = (double *) outputVector[2 * i];
//        double * rightCoords = (double *) outputVector[2 * i + 1];
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//
//        double leftSigma = 0;
//        double rightSigma = 0;
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        //        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, 17);
//        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//    jobject result = env->NewObject(subinflectionCurveClass, subinflectionCurveConstructor, segmentsArray);
//
//    env->DeleteLocalRef(hugoniotSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//
//    return result;
//

}
















