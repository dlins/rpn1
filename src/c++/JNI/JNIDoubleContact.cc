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


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_DoubleContactCurveCalc_nativeCalc
(JNIEnv * env, jobject obj, jintArray resolution, jint leftFamily, jint rightFamily) {


    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass doubleContactCurveClass = env->FindClass(DOUBLECONTACT_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
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

    //    jint  number_of_grid_pnts [dimension];
    //
    //
    //    env->GetIntArrayRegion(resolution, 0, dimension, number_of_grid_pnts );

    cout << " Parametros " << RpNumerics::getPhysics().fluxFunction().fluxParams().params() << endl;

    const FluxFunction * leftFlux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * leftAccum = &RpNumerics::getPhysics().accumulation();

    const FluxFunction * rightFlux = leftFlux;
    const AccumulationFunction * rightAccum = rightAccum;

    //    const Boundary * leftBoundary = &RpNumerics::getPhysics().boundary();
    //    const Boundary * rightBoundary = leftBoundary;



    GridValues * gv = RpNumerics::getGridFactory().getGrid("doublecontactcurve");

    //    RealVector pmin(leftBoundary->minimums());
    //    RealVector pmax(leftBoundary->maximums());

    cout << "left family: " << leftFamily << endl;
    cout << "right family: " << rightFamily << endl;

    //    Double_Contact dc(pmin, pmax, number_of_grid_pnts,
    //            leftFamily, leftBoundary,
    //            pmin, pmax, number_of_grid_pnts,
    //
    //            rightFamily, rightBoundary);





    Double_Contact_Function * doubleContactFunction = RpNumerics::getPhysics().getSubPhysics(0).getDoubleContactFunction();



    doubleContactFunction->curve(leftFlux, leftAccum, gv, leftFamily,
            rightFlux, rightAccum, gv, rightFamily,
            left_vrs, right_vrs);



    cout << "left_vrs.size()  = " << left_vrs.size() << endl;



    cout << "right_vrs.size()  = " << right_vrs.size() << endl;





    //    for (int i = 0; i < left_vrs.size() / 2; i++) {
    //        //        cout<<"Ponto esquerda: "<<left_vrs[2*i]<<" Ponto direita: "<<right_vrs[2*i]<<endl;
    //
    //
    //    }



    if (left_vrs.size() == 0 || right_vrs.size() == 0)return NULL;


    for (unsigned int i = 0; i < left_vrs.size() / 2; i++) {

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i]);
        RpNumerics::getPhysics().getSubPhysics(0).postProcess(left_vrs[2 * i + 1]);


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) left_vrs.at(2 * i);
        double * rightCoords = (double *) left_vrs.at(2 * i + 1);


        cout << "Ponto esquerda: " << left_vrs[2 * i] << " Ponto direita: " << left_vrs[2 * i + 1] << endl;


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
















