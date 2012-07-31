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
#include "TPCW.h"
#include "ColorCurve.h"
#include "Hugoniot_Curve.h"
#include "GridValuesFactory.h"

using std::vector;
using namespace std;

JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
(JNIEnv * env, jobject obj, jobject uMinus) {

    printf("Seting UMinus\n");

}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc__Lrpnumerics_PhasePoint_2_3I
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

    //-------------------------------------------------------------------


 




    RealVector Uref(dimension, input);

    vector<HugoniotPolyLine> hugoniotPolyLineVector;

    Hugoniot_Curve hugoniotCurve;

    GridValues * gv = RpNumerics::getGridFactory().getGrid("hugoniotcurve");

    
    hugoniotCurve.classified_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), *gv, Uref, hugoniotPolyLineVector);

    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {


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


            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType);
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

        }


    }



    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);


    return result;
}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc__Lrpnumerics_PhasePoint_2IILwave_util_RealVector_2Lwave_util_RealVector_2
(JNIEnv *env, jobject obj, jobject uMinus, jint xRes, jint yRes, jobject topR, jobject dwnL) {


    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");

    jmethodID toDoubleRealVectorMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDI)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");

    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    jdoubleArray topArray = (jdoubleArray) (env)->CallObjectMethod(topR, toDoubleMethodID);

    jdoubleArray downArray = (jdoubleArray) (env)->CallObjectMethod(dwnL, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    int topLength = env->GetArrayLength(topArray);
    int downLength = env->GetArrayLength(downArray);

    double input [dimension];

    double topDimension [topLength];
    double downDimension [downLength];


    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->GetDoubleArrayRegion(topArray, 0, topLength, topDimension);
    env->GetDoubleArrayRegion(downArray, 0, downLength, downDimension);

    env->DeleteLocalRef(phasePointArray);

    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;

    //-------------------------------------------------------------------

    RealVector Uref(dimension, input);

    cout << Uref << endl;

    cout << "Implementar refinamento !!" << endl;

    vector<HugoniotPolyLine> hugoniotPolyLineVector;

    Hugoniot_Curve hugoniotCurve;

    GridValues * gv = RpNumerics::getGridFactory().getGrid("hugoniotcurve");

    hugoniotCurve.classified_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), *gv, Uref, hugoniotPolyLineVector);

    //    delete tempFluxFunction;

    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {



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


            cout << leftLambda1 << " " << leftLambda2 << " " << rightLambda1 << " " << rightLambda2 << endl;

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
















