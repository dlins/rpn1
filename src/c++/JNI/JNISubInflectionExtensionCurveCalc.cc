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


#include "rpnumerics_SubInflectionExtensionCurveCalc.h"

#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>

//#include "SubinflectionTPCW.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_SubInflectionExtensionCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jint xResolution, jint yResolution, jint curveFamily, jint domainFamily, jint characteristicWhere) {

//    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass subInflectionExtensionCurveClass = env->FindClass(SUBINFLECTIONEXTENSIONCURVE_LOCATION);
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//    jmethodID subInflectionExtensionCurveConstructor = env->GetMethodID(subInflectionExtensionCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");
//
//    //Input processing
//
//    int dimension;
//
//
//    //Calculations using the input
//
//    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    // Storage space for the segments:
//
//    std::vector<RealVector> curve_segments;
//    std::vector<RealVector> domain_segments;
//
//    if (RpNumerics::getPhysics().ID().compare("TPCW") == 0) {
//
//
//        TPCW & tpcw = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
//        const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();
//
//        Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized *) & tpcw.fluxFunction();
//
//        Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) & tpcw.accumulation();
//
//
//        dimension=tpcw.domain().dim();
//
//
//        RealVector min(2);
//
//        RealVector max(2);
//
//
//        min.component(0) = physicsBoundary.minimums().component(0);
//        min.component(1) = physicsBoundary.minimums().component(1);
//
//        max.component(0) = physicsBoundary.maximums().component(0);
//        max.component(1) = physicsBoundary.maximums().component(1);
//
//
//        tpcw.preProcess(min);
//        tpcw.preProcess(max);
//
//        int number_of_grid_points[2];
//
//
//        number_of_grid_points[0] = xResolution;
//        number_of_grid_points[1] = yResolution;
//
//        int singular = 1;
//
//        curveFamily = 0;
//        domainFamily = 1;
//        characteristicWhere = 1;
//
//
//        if ( Debug::get_debug_level() == 5 ) {
//            //cout << "Resolucao x " << number_of_grid_points[0] << endl;
//            //cout << "Resolucao y " << number_of_grid_points[1] << endl;
//        }
//
//
// SubinflectionTPCW  subInflectionFunction((Flux2Comp2PhasesAdimensionalized*) & tpcw.fluxFunction(), (Accum2Comp2PhasesAdimensionalized*) & tpcw.accumulation());
//
//    SubinflectionTPCW_Extension::extension_curve(&subInflectionFunction,
//            min, max, number_of_grid_points, // For the domain.
//            (Flux2Comp2PhasesAdimensionalized*) & tpcw.fluxFunction(), (Accum2Comp2PhasesAdimensionalized*) & tpcw.accumulation(),
//            domainFamily,
//            (Flux2Comp2PhasesAdimensionalized*) & tpcw.fluxFunction(), (Accum2Comp2PhasesAdimensionalized*) & tpcw.accumulation(),
//            curveFamily,
//            characteristicWhere, singular,
//            curve_segments,
//            domain_segments);
//
//
//        if ( Debug::get_debug_level() == 5 ) {
//            //cout << "Curve: " << curve_segments.size() << endl;
//            //cout << "Domain: " << domain_segments.size() << endl;
//        }
//
//
//        tpcw.postProcess(curve_segments);
//        tpcw.postProcess(domain_segments);
//
//        if ( Debug::get_debug_level() == 5 ) {
//            //cout << "Familia da curva" << curveFamily << endl;
//            //cout << "Familia do dominio" << domainFamily << endl;
//            //cout << "characteristic " << characteristicWhere << endl;
//        }
//
//
//    }
//
//
//    if ( Debug::get_debug_level() == 5 ) {
//        printf("curve_segments.size()  = %d\n", curve_segments.size());
//        printf("domain_segments.size() = %d\n", domain_segments.size());
//    }
//
//
//    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {
//
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//
//        double * leftCoords = (double *) curve_segments.at(2 * i);
//        double * rightCoords = (double *) curve_segments.at(2 * i + 1);
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
//        int pointType = 20;
//
//        double leftSigma = 0;
//        double rightSigma = 0;
//
//        
//        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
//        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, hugoniotSegment);
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
//        int pointType = 20;
//
//        double leftSigma = 0;
//        double rightSigma = 0;
//        
//        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
//        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);
//
//    }
//
//
//    jobject result = env->NewObject(subInflectionExtensionCurveClass, subInflectionExtensionCurveConstructor, leftSegmentsArray, rightSegmentsArray);
//
//    return result;


}
















