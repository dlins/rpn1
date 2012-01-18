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
(JNIEnv * env, jobject obj, jint domainFamily, jint curveFamily, jint xResolution, jint yResolution, jint singular, jint characteristicWhere) {

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hysteresisCurveClass = env->FindClass(HYSTERESISCURVE_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hysteresisCurveConstructor = env->GetMethodID(hysteresisCurveClass, "<init>", "(IILjava/util/List;Ljava/util/List;)V");

    //Input processing

    int dimension = 2;


    //Calculations using the input

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    // Storage space for the segments:

    std::vector<RealVector> curve_segments;
    std::vector<RealVector> domain_segments;

    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();

    Boundary * physicsBoundary = RpNumerics::getPhysics().boundary().clone();

    RealVector min(physicsBoundary-> minimums());
    RealVector max(physicsBoundary-> maximums());




    int * number_of_grid_points = new int[2];


    number_of_grid_points[0] = xResolution;
    number_of_grid_points[1] = yResolution;


    Hysteresis::curve(physicsBoundary, fluxFunction, accumulationFunction,
            curveFamily,
            min, max, number_of_grid_points,
            domainFamily,
            fluxFunction, accumulationFunction,
            characteristicWhere, singular,
            curve_segments,
            domain_segments);


    delete physicsBoundary;
    cout << "Tamanho da coincidence curve extension: " << curve_segments.size() << endl;
    cout << "Tamanho da coincidence domain extension: " << domain_segments.size() << endl;
   
    cout << "Resolucao x " << xResolution << endl;

    cout << "Resolucao y " << yResolution << endl;

    cout << "Familia da curva" << curveFamily << endl;
    cout << "Familia do dominio" << domainFamily << endl;
    cout << "characteristic " << characteristicWhere << endl;


    delete number_of_grid_points;


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

        int pointType = 20;

        double leftSigma = 0;
        double rightSigma = 0;


        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, hugoniotSegment);

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

        int pointType = 20;

        double leftSigma = 0;
        double rightSigma = 0;


        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }




    jobject result = env->NewObject(hysteresisCurveClass, hysteresisCurveConstructor, curveFamily, domainFamily, leftSegmentsArray, rightSegmentsArray);




    return result;


}
















