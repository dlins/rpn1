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


#include "rpnumerics_EnvelopeCurveCalc.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include "Debug.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_EnvelopeCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jint where_constant, jint number_of_steps) {


    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass envelopeCurveClass = env->FindClass(ENVELOPECURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID envelopeCurveConstructor = env->GetMethodID(envelopeCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");

    int dimension = RpNumerics::getPhysics().domain().dim();

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    // Storage space for the segments:
    std::vector<RealVector> left_vrs;
    std::vector<RealVector> right_vrs;


    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();

    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcation");

    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "constant : " << where_constant << endl;
        ////cout << "number of steps : " << number_of_steps << endl;
    }

    Boundary * physicsBoundary = (Boundary *)&RpNumerics::getPhysics().boundary();

    const char * boundaryType = physicsBoundary->boundaryType();

    if (!strcmp(boundaryType, "Three_Phase_Boundary")) {

        Three_Phase_Boundary * boundary = (Three_Phase_Boundary *) physicsBoundary;

        physicsBoundary->envelope_curve(flux, accum, *gv,
                where_constant, number_of_steps, true,
                left_vrs, right_vrs);
    }

    if (!strcmp(boundaryType, "rect")) {

        RectBoundary * boundary = (RectBoundary *) physicsBoundary;
        boundary->envelope_curve(flux, accum, *gv,
                where_constant, number_of_steps, true,
                left_vrs, right_vrs);

    }



    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "left_vrs.size()  = " << left_vrs.size() << endl;
        ////cout << "right_vrs.size()  = " << right_vrs.size() << endl;
    }


    if (left_vrs.size() == 0 || right_vrs.size() == 0)return NULL;



    for (unsigned int i = 0; i < left_vrs.size() / 2; i++) {


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) left_vrs.at(2 * i);
        double * rightCoords = (double *) left_vrs.at(2 * i + 1);

        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);


        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, realSegment);

    }

    for (unsigned int i = 0; i < right_vrs.size() / 2; i++) {



        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) right_vrs.at(2 * i);
        double * rightCoords = (double *) right_vrs.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);


        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, realSegment);

    }


    jobject result = env->NewObject(envelopeCurveClass, envelopeCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    return result;

}
















