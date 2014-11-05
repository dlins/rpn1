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



using std::vector;
using namespace std;

JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
(JNIEnv * env, jobject obj, jobject uMinus) {

   
}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc__Lrpnumerics_PhasePoint_2Lrpn_configuration_Configuration_2
(JNIEnv * env, jobject obj, jobject uMinus, jobject configuration) {

    jclass classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);

    jclass classConfiguration = env->FindClass(CONFIGURATION_LOCATION);


    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);



    jmethodID getParamMethodID = (env)->GetMethodID(classConfiguration, "getParam", "(Ljava/lang/String;)Ljava/lang/String;");

    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DDDDDILjava/lang/String;)V");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;Ljava/util/List;)V");

    //Input processing
    jdoubleArray phasePointArray = (jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);

    int dimension = env->GetArrayLength(phasePointArray);

    double input [dimension];



    jstring javaMethodName = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("method"));

    jstring javaCaseName = (jstring) env->CallObjectMethod(configuration, getParamMethodID, env->NewStringUTF("case"));


    string methodName(env->GetStringUTFChars(javaMethodName, NULL));
    string caseName(env->GetStringUTFChars(javaCaseName, NULL));

    cout << "Method pego no JNI: " << methodName << endl;

    //Input point


    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    env->DeleteLocalRef(phasePointArray);

  

    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    //-------------------------------------------------------------------

    RealVector Uref(dimension, input);
    
    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();
    

    vector<RealVector> transitionList;

    vector<HugoniotPolyLine> hugoniotPolyLineVector;

    ReferencePoint refPoint(Uref,flux, accum, 0);
//
//    cout << "Tipo do metodo c++" << methodName << endl;   
//
//    cout << "Caso pego no JNI: " << caseName << endl;

    vector<HugoniotCurve *> hugoniotMethods;
   
    
    //Escolhendo o metodo
    RpNumerics::physicsVector_->at(0)->list_of_Hugoniot_methods(hugoniotMethods);
    HugoniotCurve * method ;
    for (int i = 0; i < hugoniotMethods.size(); i++) {
       if (hugoniotMethods[i]->Hugoniot_info().compare(methodName)==0){
           method=hugoniotMethods[i];
       }

    }
    
    //Escolhendo o caso  (ponto de referencia)
    std::vector<int> referencePointFlag;
    std::vector<std::string> referencePointName;
    
    int caseNumber=0;
    
    method->list_of_reference_points(referencePointFlag,referencePointName);
    
    
    for (int i = 0; i < referencePointName.size(); i++) {
        
        if(referencePointName[i].compare(caseName)==0){
            
            caseNumber=referencePointFlag[i];
        }

    }
    
    method->curve(refPoint,caseNumber, hugoniotPolyLineVector);

    jobject transitionArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);


    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {


        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].point.size() - 1; j++) {


            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);

         

            double * leftCoords = (double *) hugoniotPolyLineVector[i].point[j];
            double * rightCoords = (double *) hugoniotPolyLineVector[i].point[j + 1];

            env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
            env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);

            //Construindo left e right points
            jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
            jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);

            env->DeleteLocalRef(eigenValRLeft);
            env->DeleteLocalRef(eigenValRRight);

            int pointType = hugoniotPolyLineVector[i].type[j];

            string signature = hugoniotPolyLineVector[i].signature[j];


            double leftSigma = hugoniotPolyLineVector[i].speed[j];
            double rightSigma = hugoniotPolyLineVector[i].speed[j + 1];

            double leftLambda1 = hugoniotPolyLineVector[i].eigenvalue[j].component(0);
            double leftLambda2 = hugoniotPolyLineVector[i].eigenvalue[j].component(1);

            double rightLambda1 = hugoniotPolyLineVector[i].eigenvalue[j + 1].component(0);
            double rightLambda2 = hugoniotPolyLineVector[i].eigenvalue[j + 1].component(1);


            jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorRightPoint, rightSigma, leftLambda1, leftLambda2, rightLambda1, rightLambda2, pointType, env->NewStringUTF(signature.c_str()));
            env->CallObjectMethod(segmentsArray, arrayListAddMethod, hugoniotSegment);
            
            env->DeleteLocalRef(hugoniotSegment);


        }


    }



    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray, transitionArray);


    return result;
}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc__Lrpnumerics_PhasePoint_2IILwave_util_RealVector_2Lwave_util_RealVector_2
(JNIEnv *env, jobject obj, jobject uMinus, jint xRes, jint yRes, jobject topR, jobject dwnL) {

//    ////cout << "entrando e recalc de hugoniot curve" << endl;
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
//    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Lrpnumerics/PhasePoint;Ljava/util/List;Ljava/util/List;)V");
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
//
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(Uref);
//
//
//    vector<HugoniotPolyLine> hugoniotPolyLineVector;
//
//
//
//    GridValues gv(&RpNumerics::getPhysics().boundary(), pMin, pMax, resolution);
//
//
//
//    vector<bool> isCircular;
//
//    vector<RealVector> transitionList;
//
//    Viscosity_Matrix * vm = RpNumerics::getPhysics().getSubPhysics(0).getViscosityMatrix();
//
//    ReferencePoint refPoint(Uref, &RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(), 0);
//
//
//    HugoniotCurve *hugoniotCurve = RpNumerics::getPhysics().getSubPhysics(0).getHugoniotCurve("IMPLICIT");
//
//
//
//
//    //    hugoniotCurve->curve(gv, refPoint, 0, hugoniotPolyLineVector, transitionList);
//
//    hugoniotCurve->curve(refPoint, 13, hugoniotPolyLineVector);
//
//
//
//
//    jobject transitionArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//    //
//    //    for (int i = 0; i < transitionList.size(); i++) {
//    //
//    //        if (Debug::get_debug_level() == 5) {
//    //            //cout << "Ponto de transicao: " << transitionList[i] << endl;
//    //        }
//    //
//    //        jdoubleArray transPointArray = env->NewDoubleArray(dimension);
//    //        double * leftCoords = (double *) transitionList[i];
//    //
//    //        env->SetDoubleArrayRegion(transPointArray, 0, dimension, leftCoords);
//    //
//    //        jobject transPointrealVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, transPointArray);
//    //
//    //        env->CallObjectMethod(transitionArray, arrayListAddMethod, transPointrealVector);
//    //
//    //
//    //    }
//
//
//
//
//
//
//
//
//
//
//    //    delete tempFluxFunction;
//
//
//    for (int i = 0; i < hugoniotPolyLineVector.size(); i++) {
//
//
//        for (unsigned int j = 0; j < hugoniotPolyLineVector[i].point.size() - 1; j++) {
//
//
//
//            jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//            jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//
//
//            RpNumerics::getPhysics().getSubPhysics(0).postProcess(hugoniotPolyLineVector[i].point[j]);
//            RpNumerics::getPhysics().getSubPhysics(0).postProcess(hugoniotPolyLineVector[i].point[j + 1]);
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
//            int pointType = hugoniotPolyLineVector[i].type[j];
//
//            string signature = hugoniotPolyLineVector[i].signature[j];
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
//
//
//    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, uMinus, segmentsArray, transitionArray);
//
//    // Limpando
//
//
//    env->DeleteLocalRef(hugoniotSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//
//
//    return result;

}
















