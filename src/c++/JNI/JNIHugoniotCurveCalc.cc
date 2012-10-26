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

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDILjava/lang/String;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");

    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];

   
    //Input point


    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    //-------------------------------------------------------------------

    cout << "Parametros flux: " << RpNumerics::getPhysics().fluxFunction().fluxParams().params() << endl;
    cout << "Parametros accum: " << RpNumerics::getPhysics().accumulation().accumulationParams().params() << endl;

    RealVector Uref(dimension, input);
    
    RpNumerics::getPhysics().getSubPhysics(0).preProcess(Uref);
    
    const    Boundary * testeBoundary=    RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();
    
    cout <<"Teste"<<testeBoundary->minimums()<<" "<<testeBoundary->maximums()<<endl;

    vector<HugoniotPolyLine> hugoniotPolyLineVector;

    Hugoniot_Locus * hugoniotCurve = RpNumerics::getPhysics().getSubPhysics(0).getHugoniotFunction();
   

    GridValues * gv = RpNumerics::getGridFactory().getGrid("hugoniotcurve");

//    for (int i = 0; i < gv->grid.rows(); i++) {
//        for (int j = 0; j < gv->grid.cols(); j++) {
//            
//            cout<<"Ponto: "<<i<<" "<<j<<" "<<gv->grid(i,j)<<endl;
//
//        }
//    }

    hugoniotCurve->classified_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), *gv, Uref, hugoniotPolyLineVector);

//    cout << "Saida: " << hugoniotPolyLineVector.size() << endl;



    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {


//        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].point.size() - 1; j++) {

         
            RpNumerics::getPhysics().getSubPhysics(0).postProcess(hugoniotPolyLineVector[i].point[0]);
            RpNumerics::getPhysics().getSubPhysics(0).postProcess(hugoniotPolyLineVector[i].point[1]);
              

            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

            double * leftCoords = (double *) hugoniotPolyLineVector[i].point[0];
            

            double * rightCoords = (double *) hugoniotPolyLineVector[i].point[1];

            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

            //Construindo left e right points
            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            int pointType = hugoniotPolyLineVector[i].type;

            string signature = "";//hugoniotPolyLineVector[i].signature;

//            cout <<"segmento: "<<hugoniotPolyLineVector[i].point[0]<<" "<<hugoniotPolyLineVector[i].point[1]<<endl;


            double leftSigma = hugoniotPolyLineVector[i].speed[0];
            double rightSigma = hugoniotPolyLineVector[i].speed[1];

            double leftLambda1 = hugoniotPolyLineVector[i].eigenvalue[0].component(0);
            double leftLambda2 = hugoniotPolyLineVector[i].eigenvalue[0].component(1);

            double rightLambda1 = hugoniotPolyLineVector[i].eigenvalue[1].component(0);
            double rightLambda2 = hugoniotPolyLineVector[i].eigenvalue[1].component(1);


            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType, env->NewStringUTF(signature.c_str()));
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

//        }


    }



    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);


    return result;
}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc__Lrpnumerics_PhasePoint_2IILwave_util_RealVector_2Lwave_util_RealVector_2
(JNIEnv *env, jobject obj, jobject uMinus, jint xRes, jint yRes, jobject topR, jobject dwnL) {


//    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
//
//    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);
//
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
//
//    jmethodID toDoubleRealVectorMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDILjava/lang/String;)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//
//    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;)V");
//
//    //Input processing
//    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);
//
//    jdoubleArray topArray = (jdoubleArray) (env)->CallObjectMethod(topR, toDoubleMethodID);
//
//    jdoubleArray downArray = (jdoubleArray) (env)->CallObjectMethod(dwnL, toDoubleMethodID);
//
//    int dimension = env->GetArrayLength(phasePointArray);
//
//    int topLength = env->GetArrayLength(topArray);
//    int downLength = env->GetArrayLength(downArray);
//
//    double input [dimension];
//
//    double topDimension [topLength];
//    double downDimension [downLength];
//
//
//    RealVector pMin(2);
//    RealVector pMax(2);
//
//
//
//    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
//
//    env->GetDoubleArrayRegion(topArray, 0, topLength, topDimension);
//    env->GetDoubleArrayRegion(downArray, 0, downLength, downDimension);
//
//
//    pMin.component(0) = downDimension[0];
//    pMin.component(1) = downDimension[1];
//
//    pMax.component(0) = topDimension[0];
//    pMax.component(1) = topDimension[1];
//
//    vector<int> resolution;
//
//    resolution.push_back(xRes);
//    resolution.push_back(yRes);
//
//
//
//
//
//    env->DeleteLocalRef(phasePointArray);
//
//
//    //Calculations using the input
//
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//
//    //    Test testFunction;
//
//    //-------------------------------------------------------------------
//
//    RealVector Uref(dimension, input);
//
//    vector<HugoniotPolyLine> hugoniotPolyLineVector;
//
//    Hugoniot_Curve hugoniotCurve;
//
//    GridValues gv(&RpNumerics::getPhysics().boundary(), pMin, pMax, resolution);
//
//    hugoniotCurve.classified_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), gv, Uref, hugoniotPolyLineVector);
//
//    //    delete tempFluxFunction;
//
//
//    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {
//
//
//        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].point.size() - 1; j++) {
//
//            int m = (hugoniotPolyLineVector[i].point[0].size() - dimension - 1) / 2; // Number of valid eigenvalues
//
//            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//            double * leftCoords = (double *) hugoniotPolyLineVector[i].point[j];
//            double * rightCoords = (double *) hugoniotPolyLineVector[i].point[j + 1];
//
//            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//            //Construindo left e right points
//            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//            int pointType = hugoniotPolyLineVector[i].type;
//
//            string signature = hugoniotPolyLineVector[i].signature;
//
//            double leftSigma = hugoniotPolyLineVector[i].speed[j];
//            double rightSigma = hugoniotPolyLineVector[i].speed[j + 1];
//
//            double leftLambda1 = hugoniotPolyLineVector[i].eigenvalue[j].component(0);
//            double leftLambda2 = hugoniotPolyLineVector[i].eigenvalue[j].component(1);
//
//            double rightLambda1 = hugoniotPolyLineVector[i].eigenvalue[j + 1].component(0);
//            double rightLambda2 = hugoniotPolyLineVector[i].eigenvalue[j + 1].component(1);
//
//
//            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType, env->NewStringUTF(signature.c_str()));
//            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);
//
//        }
//
//
//    }
//
//    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray);
//
//    // Limpando
//
//
//    env->DeleteLocalRef(hugoniotSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);




//    return result;
    
    return NULL;

}
















