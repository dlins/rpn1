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


#include "rpnumerics_DoubleContactCurveCalc.h"
#include "Double_Contact.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "ContourMethod.h"
#include "ReducedTPCWHugoniotFunctionClass.h"
#include "TPCW.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "StoneHugoniotFunctionClass.h"
#include "Double_ContactTPCW.h"

using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_DoubleContactCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jint xResolution, jint yResolution, jint leftFamily, jint rightFamily) {

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    //    jclass hugoniotPointTypeClass = (env)->FindClass(HUGONIOTPOINTTYPE_LOCATION);

    jclass doubleContactCurveClass = env->FindClass(DOUBLECONTACT_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    //    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    //    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    jmethodID doubleContactCurveConstructor = env->GetMethodID(doubleContactCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");

    //    int i;

    //Input processing
    //    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    int dimension;

    //    double input [dimension];


    //    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
    //
    //    env->DeleteLocalRef(phasePointArray);

    //Calculations using the input

    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    //    RealVector Uref(dimension, input);
    //
    //    cout << Uref << endl;

    // Storage space for the segments:
    std::vector<RealVector> left_vrs;
    std::vector<RealVector> right_vrs;

    int * number_of_grid_pnts = new int[2];


    number_of_grid_pnts[0] = xResolution;
    number_of_grid_pnts[1] = yResolution;

    if (RpNumerics::getPhysics().ID().compare("Stone") == 0) {

        cout << "Chamando com stone" << endl;

        dimension = 2;
        // Create the Double Contact
        //        // Grid (the same one for the left- and right-domains)
        //        RealVector pmin(2);
        //        pmin.component(0) = 0.0;
        //        pmin.component(1) = 0.0;
        //
        //        RealVector pmax(2);
        //        pmax.component(0) = 1.0;
        //        pmax.component(1) = 1.0;
        //

        cout << " Parametros " << RpNumerics::getPhysics().fluxFunction().fluxParams().params() << endl;


        int family = 1; // Or else...


        const FluxFunction * leftFlux = &RpNumerics::getPhysics().fluxFunction();
        const AccumulationFunction * leftAccum = &RpNumerics::getPhysics().accumulation();

        const FluxFunction * rightFlux = leftFlux;
        const AccumulationFunction * rightAccum = rightAccum;

        const Boundary * leftBoundary = &RpNumerics::getPhysics().boundary();
        const Boundary * rightBoundary = leftBoundary;


        RealVector pmin(leftBoundary->minimums());
        RealVector pmax(leftBoundary->maximums());


        Double_Contact dc(pmin, pmax, number_of_grid_pnts, leftFlux,
                leftAccum,
                leftFamily, leftBoundary,
                pmin, pmax, number_of_grid_pnts,
                rightFlux,
                rightAccum,
                rightFamily, rightBoundary);

        dc.compute_double_contact(left_vrs, right_vrs);

    }


    if (RpNumerics::getPhysics().ID().compare("TPCW") == 0) {

        cout << "Chamando com tpcw" << endl;
        dimension = 3;


        TPCW & tpcw = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
        const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

        RealVector min(2);

        RealVector max(2);


        min.component(0) = physicsBoundary.minimums().component(0);
        min.component(1) = physicsBoundary.minimums().component(1);

        max.component(0) = physicsBoundary.maximums().component(0);
        max.component(1) = physicsBoundary.maximums().component(1);


        tpcw.preProcess(min);
        tpcw.preProcess(max);

        cout << "Resolucao x " << number_of_grid_pnts[0] << endl;
        cout << "Resolucao y " << number_of_grid_pnts[1] << endl;



        cout << "Familia direita" << rightFamily << endl;
        cout << "Familia esquerda" << leftFamily << endl;




        Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized *) & tpcw.fluxFunction();

        Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) & tpcw.accumulation();

        Double_ContactTPCW dc(min, max, number_of_grid_pnts,
                fluxFunction, accumulationFunction,
                leftFamily,
                min, max, number_of_grid_pnts,
                fluxFunction, accumulationFunction,
                rightFamily);

        dc.compute_double_contactTPCW(left_vrs, right_vrs);

        printf("left_vrs.size()  = %d\n", left_vrs.size());
        printf("right_vrs.size() = %d\n", right_vrs.size());

        tpcw.postProcess(left_vrs);
        tpcw.postProcess(right_vrs);




    }

    delete number_of_grid_pnts;




    printf("right_vrs.size() = %d\n", right_vrs.size());
    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

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

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;


        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);
        env->CallObjectMethod(leftSegmentsArray, arrayListAddMethod, hugoniotSegment);

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

        int pointType = 0;

        double leftSigma = 0;
        double rightSigma = 0;


        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, pointType);


        env->CallObjectMethod(rightSegmentsArray, arrayListAddMethod, hugoniotSegment);

    }




    jobject result = env->NewObject(doubleContactCurveClass, doubleContactCurveConstructor, leftSegmentsArray, rightSegmentsArray);




    return result;


}
















