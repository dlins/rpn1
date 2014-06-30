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
#include "Double_Contact_Function.h"
#include <vector>
#include <iostream>
#include "Debug.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_DoubleContactCurveCalc_nativeCalc__II
(JNIEnv *env, jobject obj, jint leftFamily, jint rightFamily) {

    //JNIEXPORT jobject JNICALL Java_rpnumerics_DoubleContactCurveCalc_nativeCalc
    //(JNIEnv * env, jobject obj, jint leftFamily, jint rightFamily) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass doubleContactCurveClass = env->FindClass(DOUBLECONTACT_LOCATION);


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID doubleContactCurveConstructor = env->GetMethodID(doubleContactCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");


    int dimension = RpNumerics::getPhysics().domain().dim();


    jobject leftSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
    jobject rightSegmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    // Storage space for the segments:
    std::vector<RealVector> left_vrs;
    std::vector<RealVector> right_vrs;



    const FluxFunction * leftFlux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * leftAccum = &RpNumerics::getPhysics().accumulation();

    const FluxFunction * rightFlux = leftFlux;
    const AccumulationFunction * rightAccum = leftAccum;


    GridValues * gv = RpNumerics::getGridFactory().getGrid("doublecontactcurve");

    Double_Contact_Function * doubleContactFunction = RpNumerics::getPhysics().getSubPhysics(0).getDoubleContactFunction();


    doubleContactFunction->curve(leftFlux, leftAccum, gv, leftFamily,
            rightFlux, rightAccum, gv, rightFamily,
            left_vrs, right_vrs);


    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "left_vrs.size()  = " << left_vrs.size() << endl;
        ////cout << "right_vrs.size()  = " << right_vrs.size() << endl;
    }

    
    if (left_vrs.size() == 0 || right_vrs.size() == 0)return NULL;


    for (unsigned int i = 0; i < left_vrs.size() / 2; i++) {

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i + 1]);


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



        RpNumerics::getPhysics().getSubPhysics(0).postProcess(right_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(right_vrs[2 * i + 1]);

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


    jobject result = env->NewObject(doubleContactCurveClass, doubleContactCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    return result;

}

JNIEXPORT jobject JNICALL Java_rpnumerics_DoubleContactCurveCalc_nativeCalc__Ljava_util_List_2II
(JNIEnv *env, jobject obj, jobject areasList, jint leftFamily, jint rightFamily) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass listClass = env->FindClass("java/util/ArrayList");


    jclass classList = env->FindClass("java/util/List");


    jclass areaClass = env->FindClass(AREA_LOCATION);


    jclass doubleContactCurveClass = env->FindClass(DOUBLECONTACT_LOCATION);


    jmethodID listGetMethodID = env->GetMethodID(classList, "get", "(I)Ljava/lang/Object;");

    jmethodID areaGetResolutionMethodID = env->GetMethodID(areaClass, "getResolution", "()Lwave/util/RealVector;");

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID downLeftMethodID = env->GetMethodID(areaClass, "getDownLeft", "()Lwave/util/RealVector;");
    jmethodID topRightMethodID = env->GetMethodID(areaClass, "getTopRight", "()Lwave/util/RealVector;");


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

    jmethodID arrayListConstructor = env->GetMethodID(listClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(listClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID doubleContactCurveConstructor = env->GetMethodID(doubleContactCurveClass, "<init>", "(Ljava/util/List;Ljava/util/List;)V");


    int dimension = RpNumerics::getPhysics().domain().dim();


    jobject leftSegmentsArray = env->NewObject(listClass, arrayListConstructor, NULL);
    jobject rightSegmentsArray = env->NewObject(listClass, arrayListConstructor, NULL);

    // Storage space for the segments:
    std::vector<RealVector> left_vrs;
    std::vector<RealVector> right_vrs;

    //LEFT GRID VALUES


    jobject leftArea = env->CallObjectMethod(areasList, listGetMethodID, 0);

    jobject leftResolutionRealVector = env->CallObjectMethod(leftArea, areaGetResolutionMethodID);

    jdoubleArray leftResolutionArray = (jdoubleArray) env->CallObjectMethod(leftResolutionRealVector, toDoubleMethodID);

    double resolutionBuffer [2];

    env->GetDoubleArrayRegion(leftResolutionArray, 0, 2, resolutionBuffer);

    vector<int> leftResolution;


    for (int i = 0; i < 2; i++) {
        leftResolution.push_back((int) resolutionBuffer[i]);
    }

    jobject downLeftRealVector = env->CallObjectMethod(leftArea, downLeftMethodID);

    jdoubleArray downLeftArray = (jdoubleArray) env->CallObjectMethod(downLeftRealVector, toDoubleMethodID);

    double downLeftBuffer [2];

    env->GetDoubleArrayRegion(downLeftArray, 0, 2, downLeftBuffer);

    jobject topRight = env->CallObjectMethod(leftArea, topRightMethodID);

    jdoubleArray topRightArray = (jdoubleArray) env->CallObjectMethod(topRight, toDoubleMethodID);

    double topRightBuffer [2];

    env->GetDoubleArrayRegion(topRightArray, 0, 2, topRightBuffer);

    RealVector leftPmin(2, downLeftBuffer);
    RealVector rightPmax(2, topRightBuffer);
    
    
    
    if ( Debug::get_debug_level() == 5 ) {
        ////cout<<"No JNI LEFT: "<<leftPmin<<" "<<rightPmax<<endl;
    }

    const Boundary * boundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();


    GridValues leftGridValues(boundary, leftPmin, rightPmax, leftResolution);



    //RIGHT GRID VALUES


    jobject rightArea = env->CallObjectMethod(areasList, listGetMethodID, 1);

    jobject rightResolutionRealVector = env->CallObjectMethod(rightArea, areaGetResolutionMethodID);

    jdoubleArray rightResolutionArray = (jdoubleArray) env->CallObjectMethod(rightResolutionRealVector, toDoubleMethodID);

    env->GetDoubleArrayRegion(rightResolutionArray, 0, 2, resolutionBuffer);

    vector<int> rightResolution;


    for (int i = 0; i < 2; i++) {
        rightResolution.push_back((int) resolutionBuffer[i]);
    }

    jobject downRightRealVector = env->CallObjectMethod(rightArea, downLeftMethodID);

    jdoubleArray downRightArray = (jdoubleArray) env->CallObjectMethod(downRightRealVector, toDoubleMethodID);



    env->GetDoubleArrayRegion(downRightArray, 0, 2, downLeftBuffer);

    jobject topRightR = env->CallObjectMethod(rightArea, topRightMethodID);

    jdoubleArray topRightArrayR = (jdoubleArray) env->CallObjectMethod(topRightR, toDoubleMethodID);



    env->GetDoubleArrayRegion(topRightArrayR, 0, 2, topRightBuffer);

    RealVector leftPminR(2, downLeftBuffer);
    RealVector rightPmaxR(2, topRightBuffer);


    
    if ( Debug::get_debug_level() == 5 ) {
        ////cout<<"No JNI RIGHT: "<<leftPminR<<" "<<rightPmaxR<<endl;
    }
    

    GridValues rightGridValues(boundary, leftPminR, rightPmaxR, rightResolution);

//---------------------------------------------------------------------------------------------------

    const FluxFunction * leftFlux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * leftAccum = &RpNumerics::getPhysics().accumulation();

    const FluxFunction * rightFlux = leftFlux;
    const AccumulationFunction * rightAccum = leftAccum;




    Double_Contact_Function * doubleContactFunction = RpNumerics::getPhysics().getSubPhysics(0).getDoubleContactFunction();


    doubleContactFunction->curve(leftFlux, leftAccum, &leftGridValues, leftFamily,
            rightFlux, rightAccum, &rightGridValues, rightFamily,
            left_vrs, right_vrs);


    if ( Debug::get_debug_level() == 5 ) {
        ////cout << "left_vrs.size()  = " << left_vrs.size() << endl;
        ////cout << "right_vrs.size()  = " << right_vrs.size() << endl;
    }



    if (left_vrs.size() == 0 || right_vrs.size() == 0)return NULL;


    for (unsigned int i = 0; i < left_vrs.size() / 2; i++) {

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i + 1]);


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



        RpNumerics::getPhysics().getSubPhysics(0).postProcess(right_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(right_vrs[2 * i + 1]);

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


    jobject result = env->NewObject(doubleContactCurveClass, doubleContactCurveConstructor, leftSegmentsArray, rightSegmentsArray);


    return result;





}











