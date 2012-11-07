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



#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include  "BLInflectionTP.h"
#include "rpnumerics_BuckleyLeverettinInflectionCurveCalc.h"
//#include "ColorCurve.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_BuckleyLeverettinInflectionCurveCalc_nativeCalc(JNIEnv * env, jobject obj) {

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass coincidenceCurveClass = env->FindClass(BLCURVE_LOCATION);

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DI)V");
    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    //    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    jmethodID coincidenceCurveConstructor = env->GetMethodID(coincidenceCurveClass, "<init>", "(Ljava/util/List;)V");


    //Input processing

    //Calculations using the input

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    //    Test testFunction;
    int dimension = 3;
    //-------------------------------------------------------------------
    RealVector Uref(dimension);


    TPCW & tpcw = (TPCW &) RpNumerics::getPhysics().getSubPhysics(0);
    

    Flux2Comp2PhasesAdimensionalized * fluxFunction = (Flux2Comp2PhasesAdimensionalized *) & tpcw.fluxFunction();

    Accum2Comp2PhasesAdimensionalized * accumulationFunction = (Accum2Comp2PhasesAdimensionalized *) & tpcw.accumulation();


    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcation");

    cout<<gv<<endl;

    std::vector< RealVector> outputVector;

    
    BLInflectionTP newBLInflection;

    newBLInflection.curve(fluxFunction, accumulationFunction, *gv, outputVector);
    
    cout<<"Tamanho do buck:"<<outputVector.size()<<endl;


    for (int i = 0; i < outputVector.size() / 2; i++) {

        tpcw.postProcess(outputVector[2 * i]);
        tpcw.postProcess(outputVector[2 * i + 1]);


        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

        double * leftCoords = (double *) outputVector[2 * i];
        double * rightCoords = (double *) outputVector[2 * i + 1];

        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);


        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

        int pointType = 1;

        //            double leftSigma = hugoniotPolyLineVector[i].vec[j].component(dimension + m);
        //            double rightSigma = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);

        double leftSigma = 0;
        double rightSigma = 0;


        //            cout<<"Antes de criar hugoniot segment"<<endl;
        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, 17);
        env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);

    }
    //    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {
    //
    //        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].vec.size() - 1; j++) {
    //
    //            int m = (hugoniotPolyLineVector[i].vec[0].size() - dimension - 1) / 2; // Number of valid eigenvalues
    //
    //
    //            //            cout << "type of " << j << " = " << hugoniotPolyLineVector[i].type << endl;
    //            //            cout << "coord 1 " << j << " = " << hugoniotPolyLineVector[i].vec[j] << endl;
    //
    //            hugoniotPolyLineVector[i].vec[j].component(2) = maxDimension.component(2);
    //            hugoniotPolyLineVector[i].vec[j + 1].component(2) = maxDimension.component(2);
    //
    //            tpcw.postProcess(hugoniotPolyLineVector[i].vec);
    //
    //
    //            //            cout << "coord 2 " << j + 1 << " = " << hugoniotPolyLineVector[i].vec[j + 1] << endl;
    //
    //            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
    //            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
    //
    //
    //            double * leftCoords = (double *) hugoniotPolyLineVector[i].vec[j];
    //            double * rightCoords = (double *) hugoniotPolyLineVector[i].vec[j + 1];
    //
    //
    //            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
    //            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
    //
    //
    //            //Construindo left e right points
    //            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
    //            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
    //
    //            int pointType = hugoniotPolyLineVector[i].type;
    //
    //            double leftSigma = hugoniotPolyLineVector[i].vec[j].component(dimension + m);
    //            double rightSigma = hugoniotPolyLineVector[i].vec[j + 1].component(dimension + m);
    //
    //            //                        double leftSigma = 0;
    //            //                        double rightSigma = 0;
    //            //
    //
    //            //            cout<<"Antes de criar hugoniot segment"<<endl;
    //            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, 18);
    //            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);
    //
    //        }
    //
    //
    //    }
    //
    //
    //
    //    // Limpando
    //
    //    //        env->DeleteLocalRef(realVectorLeftPoint);
    //
    //    //        env->DeleteLocalRef(realVectorRightPoint);
    //
    //    //        env->DeleteLocalRef(hugoniotSegment);
    //




    jobject result = env->NewObject(coincidenceCurveClass, coincidenceCurveConstructor, segmentsArray);

    //    env->DeleteLocalRef(eigenValRLeft);
    //    env->DeleteLocalRef(eigenValRRight);
    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass);
    env->DeleteLocalRef(arrayListClass);



    return result;


}
















