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


#include "rpnumerics_CoincidenceCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "Coincidence_Contour.h"




using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_CoincidenceCurveCalc_nativeCalc(JNIEnv * env, jobject obj) {

//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout << "Em coincidence nativo: " << endl;
//    }
//
//
//
//    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//        
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass coincidenceCurveClass = env->FindClass(COINCIDENCECURVE_LOCATION);
//    
//
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//    jmethodID coincidenceCurveConstructor = env->GetMethodID(coincidenceCurveClass, "<init>", "(Ljava/util/List;)V");
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//    
//    
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    int dimension = RpNumerics::getPhysics().getSubPhysics(0).domain().dim();
//
//
//    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accumulationFunction =&RpNumerics::getPhysics().accumulation();
//
//    Coincidence * coincidence  = RpNumerics::getPhysics().getSubPhysics(0).getCoincidenceMethod();
//
//    if (coincidence==NULL){
//        return NULL;
//    }
//    
//    
//    Coincidence_Contour coincidenceContour(coincidence);
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
//
//    std::vector< RealVector> outputVector;
//
//    coincidenceContour.curve(fluxFunction, accumulationFunction, *gv, outputVector);
//
//
//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout<<"Tamanho da curva de coincidencia: "<<outputVector.size() <<endl;
//    }
//
//    for (int i = 0; i < outputVector.size() / 2; i++) {
//        
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(outputVector[2 * i]);
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(outputVector[2 * i + 1]);
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
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        
//
//        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//
//    jobject result = env->NewObject(coincidenceCurveClass, coincidenceCurveConstructor, segmentsArray);
//
//    env->DeleteLocalRef(hugoniotSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//
//    return result;


}
















