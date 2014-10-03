/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRarefactionOrbitCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_RarefactionExtensionCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"

#include <vector>

//#include "Rarefaction_Extension.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionExtensionCalc_nativeCalc(JNIEnv * env, jobject obj,jobject initialPoint, jint increase, jint curveFamily, jint domainFamily, jint characteristicWhere) {

//
//    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass compositeCurveClass = env->FindClass(RAREFACTIONEXTENSIONCURVE_LOCATION);
//
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
//
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//    jmethodID compositeCurveConstructor = env->GetMethodID(compositeCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");
//
//
//    //Input processing
//    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
//
//    int dimension = RpNumerics::getPhysics().domain().dim();
//
//    double input [dimension];
//
//    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
//
//    env->DeleteLocalRef(phasePointArray);
//
//    RealVector inputPoint(dimension, input);
//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout << inputPoint << endl;
//    }
//
//    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//
//    // Storage space for the segments:
//
//
//    std::vector<RealVector> curve_segments;
//    std::vector<RealVector> domain_segments;
//  
//
//    int singular = 0;
//
//    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accumFunction = &RpNumerics::getPhysics().accumulation();
//
//    const Boundary * boundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcation");
//
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(inputPoint);
//
//    Rarefaction_Extension::extension_curve(*gv, fluxFunction,
//            accumFunction,
//            inputPoint,
//            .01,
//            curveFamily,domainFamily,
//            increase,
//            boundary, characteristicWhere,
//            curve_segments,
//            domain_segments);
//
//
//    if (curve_segments.size() == 0 || domain_segments.size() == 0) {
//
//
//        return NULL;
//    }
//
//
//    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(curve_segments.at(2 * i));
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(curve_segments.at(2 * i + 1));
//
//        double * leftCoords = (double *) curve_segments.at(2 * i);
//        double * rightCoords = (double *) curve_segments.at(2 * i + 1);
//
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//
//
//
//    for (unsigned int i = 0; i < domain_segments.size() / 2; i++) {
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(domain_segments.at(2 * i));
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(domain_segments.at(2 * i + 1));
//
//        double * leftCoords = (double *) domain_segments.at(2 * i);
//        double * rightCoords = (double *) domain_segments.at(2 * i + 1);
//
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//
//
//        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//    jobject result = env->NewObject(compositeCurveClass, compositeCurveConstructor, leftSegmentsArray, rightSegmentsArray);
//
//
//    return result;

}
