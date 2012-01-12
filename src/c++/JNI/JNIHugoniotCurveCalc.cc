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


#include "rpnumerics_HugoniotCurveCalcND.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
//#include "ContourMethod.h"
//#include "ReducedTPCWHugoniotFunctionClass.h"
#include "TPCW.h"
//#include "StoneHugoniotFunctionClass.h"
//#include "CoincidenceTPCW.h"
//#include"SubinflectionTPCW.h"
//#include "ColorCurve.h"
#include "Hugoniot_Curve.h"

using std::vector;
using namespace std;

JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
(JNIEnv * env, jobject obj, jobject uMinus) {

    printf("Seting UMinus\n");

}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc
(JNIEnv * env, jobject obj, jobject uMinus, jintArray resolution) {

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");

    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];

    //Resolution

    jint cells[dimension];
    //
    //
    env->GetIntArrayRegion(resolution, 0, dimension, cells);

    //Input point


    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;

    //-------------------------------------------------------------------


    RealVector Uref(dimension, input);

    cout << Uref << endl;

    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

    SubPhysics & physics = RpNumerics::getPhysics().getSubPhysics(0);





    RealVector min(physicsBoundary. minimums());
    RealVector max(physicsBoundary. maximums());

    physics.preProcess(min);
    physics.preProcess(max);


    cout << min << endl;
    cout << max << endl;

    cout << RpNumerics::getPhysics().fluxFunction().fluxParams().params() << endl;
    cout << RpNumerics::getPhysics().accumulation().accumulationParams().params() << endl;


    vector<HugoniotPolyLine> hugoniotPolyLineVector;


//    if (RpNumerics::getPhysics().ID().compare("TPCW") == 0) {
//
//        TPCW & physics = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
//
//        RectBoundary * tempBoundary = new RectBoundary(min, max);
//
//        Flux2Comp2PhasesAdimensionalized & fluxFunction = (Flux2Comp2PhasesAdimensionalized &) physics.fluxFunction();
//
//        Flux2Comp2PhasesAdimensionalized::ReducedFlux2Comp2PhasesAdimensionalized *reducedFlux = fluxFunction.getReducedFlux();
//
//        Accum2Comp2PhasesAdimensionalized & accumFunction = (Accum2Comp2PhasesAdimensionalized &) physics.accumulation();
//
//        Accum2Comp2PhasesAdimensionalized::ReducedAccum2Comp2PhasesAdimensionalized * reducedAccum = accumFunction.getReducedAccumulation();
//
//        Hugoniot_Curve hugoniotCurve(tempBoundary,reducedFlux, reducedAccum, min, max, cells, Uref);
//
//        hugoniotCurve.classified_curve(hugoniotPolyLineVector);
//
//    } else {
        Hugoniot_Curve hugoniotCurve(RpNumerics::getPhysics().boundary().clone(),&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(),
                min, max, cells, Uref);
        hugoniotCurve.classified_curve(hugoniotPolyLineVector);
//    }



    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {

        physics.postProcess(hugoniotPolyLineVector[i].vec);

        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {

            int m = (hugoniotPolyLineVector[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues

            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

            double * leftCoords = (double *) hugoniotPolyLineVector[i].vec[j];
            double * rightCoords = (double *) hugoniotPolyLineVector[i].vec[j + 1];


            //            cout << hugoniotPolyLineVector[i].vec[j] << " " << hugoniotPolyLineVector[i].vec[j + 1]<<endl;


            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


            //Construindo left e right points
            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            int pointType = hugoniotPolyLineVector[i].type;

            double leftSigma = hugoniotPolyLineVector[i].vec[j].component(dimension + m);
            double rightSigma = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);

            double leftLambda1 = hugoniotPolyLineVector[i].vec[j].component(dimension + m + 1);
            double leftLambda2 = hugoniotPolyLineVector[i].vec[j].component(dimension + m + 2);

            double rightLambda1 = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 1);
            double rightLambda2 = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 2);


            //            cout <<leftLambda1<<" "<<leftLambda2<<" "<<rightLambda1<<" "<<rightLambda2<<endl;

            //            cout<<"Antes de criar hugoniot segment"<<endl;
            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }



    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);

    // Limpando


    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);




    return result;
}


/*


std::vector<RealVector> left_vrs;

hugoniotCurve.curve(left_vrs);

//    cout << "Tamanho da curva de hugoniot: " << left_vrs.size() << endl;

for (int i = 0; i < left_vrs.size() / 2; i++) {

    jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
    jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);


//        cout << left_vrs[2 * i] << endl;
//        cout << left_vrs[2 * i + 1] << endl;

    double * leftCoords = (double *) left_vrs[2 * i];
    double * rightCoords = (double *) left_vrs[2 * i + 1];


    env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
    env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


    //Construindo left e right points
    jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
    jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

    int pointType = 0; //hugoniotPolyLineVector[i].type;


    double leftSigma = 0; //hugoniotPolyLineVector[i].vec[j].component(dimension + m);
    double rightSigma = 0; //hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);

    double leftLambda1 = 0; //hugoniotPolyLineVector[i].vec[j].component(dimension + m + 1);
    double leftLambda2 = 0; //hugoniotPolyLineVector[i].vec[j].component(dimension + m + 2);

    double rightLambda1 = 0; //hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 1);
    double rightLambda2 = 0; //hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m + 2);


    //            cout <<leftLambda1<<" "<<leftLambda2<<" "<<rightLambda1<<" "<<rightLambda2<<endl;

    //            cout<<"Antes de criar hugoniot segment"<<endl;
    jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType);
    env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

    //        }


}



jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);

// Limpando


env->DeleteLocalRef(hugoniotSegmentClass);
env->DeleteLocalRef(realVectorClass);
env->DeleteLocalRef(arrayListClass);




return result;


}



 */












