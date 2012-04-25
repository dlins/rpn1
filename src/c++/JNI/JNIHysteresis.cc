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


#include "rpnumerics_HysteresisCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>

#include "Hysteresis.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_HysteresisCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jint domainFamily, jintArray resolution) {

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hysteresisCurveClass = env->FindClass(HYSTERESISCURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hysteresisCurveConstructor = env->GetMethodID(hysteresisCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    // Storage space for the segments:

    std::vector<RealVector> curve_segments;
    std::vector<RealVector> domain_segments;

    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();


    GridValues * gv = RpNumerics::getPhysics().getGrid(0);


    int dimension = RpNumerics::getPhysics().domain().dim();

    cout << "Familia " << domainFamily << endl;

    Hysteresis::curve(fluxFunction, accumulationFunction, *gv, 0,
            domainFamily, domainFamily,
            fluxFunction, accumulationFunction,
            1,
            curve_segments,
            domain_segments);

    if (curve_segments.size() == 0 || domain_segments.size() == 0)return NULL;

    cout << "Tamanho da histerese curve : " << curve_segments.size() << endl;
    cout << "Tamanho da histerese domain: " << domain_segments.size() << endl;

    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {



        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) curve_segments.at(2 * i);
        double * rightCoords = (double *) curve_segments.at(2 * i + 1);



        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

//        int pointType = 20;
//
//        double leftSigma = 0;
//        double rightSigma = 0;
//

        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, realSegment);

    }

    for (unsigned int i = 0; i < domain_segments.size() / 2; i++) {

        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) domain_segments.at(2 * i);
        double * rightCoords = (double *) domain_segments.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);




        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, realSegment);

    }




    jobject result = env->NewObject(hysteresisCurveClass, hysteresisCurveConstructor, leftSegmentsArray, rightSegmentsArray);




    return result;


}
















