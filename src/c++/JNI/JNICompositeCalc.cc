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
#include "rpnumerics_CompositeCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "Stone.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"
#include <vector>
#include "Rarefaction.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jint increase, jint familyIndex) {

    cout << "chamando JNI composite calc" << endl;

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    jclass compositeCurveClass = env->FindClass(COMPOSITECURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID compositeCurveConstructor = env->GetMethodID(compositeCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");


    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];

    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

    RealVector inputPoint(dimension, input);
    cout << inputPoint << endl;

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    // Storage space for the segments:


    int number_of_grid_points[2];


    if (RpNumerics::getPhysics().ID().compare("Stone") == 0) {

        cout << "Chamando com stone" << endl;

        dimension = 2;

        const FluxFunction * stoneflux = &RpNumerics::getPhysics().fluxFunction();

        const AccumulationFunction * stoneaccum = &RpNumerics::getPhysics().accumulation();

        Boundary * tempBoundary = RpNumerics::getPhysics().boundary().clone();


        //        const RealVector & pmin = RpNumerics::getPhysics().boundary().minimums();
        //        const RealVector & pmax = RpNumerics::getPhysics().boundary().maximums();

        //        RealVector pmin(2);
        //        RealVector pmax(2);
        //
        //
        //        pmin.component(0) = 0.0;
        //        pmin.component(1) = 0.0;
        //
        //
        //        pmax.component(0) = 1.0;
        //        pmax.component(1) = 1.0;
        //
        //
        //
        //        cout << pmin << endl;
        //        cout << pmax << endl;
        //
        //
        cout << "Increase: " << increase << endl;

        double deltaxi = 1e-3;

        //Compute rarefaction


        std::vector<RealVector> rarefactionCurve;

        Rarefaction::curve(inputPoint,
                RAREFACTION_INITIALIZE_YES,
                0,
                familyIndex,
                increase,
                CHECK_RAREFACTION_MONOTONY_TRUE,
                deltaxi,
                stoneflux, stoneaccum,
                RAREFACTION_GENERAL_ACCUMULATION,
                tempBoundary,
                rarefactionCurve);






    }

  


    for (unsigned int i = 0; i < curve_segments.size() / 2; i++) {
        //    for (unsigned int i = 0; i < right_vrs.size() / 2; i++) {

        //        cout << "Coordenada : " << left_vrs.at(2 * i) << endl;
        //        cout << "Coordenada : " << left_vrs.at(2 * i + 1) << endl;


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) curve_segments.at(2 * i);
        double * rightCoords = (double *) curve_segments.at(2 * i + 1);


        //
        //        double * leftCoords = (double *) right_vrs.at(2 * i);
        //        double * rightCoords = (double *) right_vrs.at(2 * i + 1 );


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;
        //            cout << "type of " << j << " = " << classified[i].type << endl;
        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }




    for (unsigned int i = 0; i < domain_segments.size() / 2; i++) {

        //        cout << "Coordenada : " << left_vrs.at(2 * i) << endl;
        //        cout << "Coordenada : " << left_vrs.at(2 * i + 1) << endl;


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


        double * leftCoords = (double *) domain_segments.at(2 * i);
        double * rightCoords = (double *) domain_segments.at(2 * i + 1);


        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);

        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;
        //            cout << "type of " << j << " = " << classified[i].type << endl;
        //            cout << "speed of " << j << " = " << classified[i].vec[j].component(dimension + m) << endl;
        //            cout << "speed of " << j + 1 << " = " << classified[i].vec[j + 1].component(dimension + m) << endl;

        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }

    jobject result = env->NewObject(compositeCurveClass, compositeCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    //    env->DeleteLocalRef(hugoniotSegmentClass);
    //    env->DeleteLocalRef(realVectorClass);
    //    env->DeleteLocalRef(arrayListClass);



    return result;

}
