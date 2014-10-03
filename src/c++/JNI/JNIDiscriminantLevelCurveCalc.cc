/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 *
 **/


/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_DiscriminantPointLevelCalc.h"
#include "rpnumerics_DiscriminantLevelCurveCalc.h"
#include "rpnumerics_DerivativeDiscriminantLevelCurveCalc.h"


#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>
#include "CharacteristicPolynomialLevels.h"


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_DiscriminantPointLevelCalc_calcNative(JNIEnv * env, jobject obj,jobject initialPoint) {


//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//
//    jclass levelCurveClass = env->FindClass(CHARACTERISTICSPOLYCURVE_LOCATION );
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
//
//    jmethodID levelCurveConstructor = env->GetMethodID(levelCurveClass, "<init>", "(Ljava/util/List;D)V");
//
//
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    //Input processing
//    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
//
//    double input [env->GetArrayLength(inputPhasePointArray)];
//
//    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);
//
//    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));
//
//
//    for (int i = 0; i < (unsigned int) realVectorInput.size(); i++) {
//
//        realVectorInput.component(i) = input[i];
//
//    }
//
//    string id =RpNumerics::getPhysics().getSubPhysics(0).ID();
//
//    if (id.compare("TPCW")==0 || id.compare("Quad2")){
//        return NULL;
//        
//    }
//    
//
//    int dimension = RpNumerics::getPhysics().domain().dim();
//
////    Eigenvalue_Contour ec;
//    
//    CharacteristicPolynomialLevels ec;
//
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);
//    
//    if (realVectorInput.size()==3){//TODO REMOVE !!
//        realVectorInput.component(2)=1.0;
//    }
//
//    
//
//    vector < RealVector > eigen_contours;
//    double vec_levels;
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
//
//    ec.discriminant_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(),
//            *gv, realVectorInput,eigen_contours, vec_levels);
//
//    if (eigen_contours.size() == 0)
//        return NULL;
//
//    for (int i = 0; i < eigen_contours.size() / 2; i++) {
//
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i]);
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i + 1]);
//
//        double * leftCoords = (double *) eigen_contours[2 * i];
//        double * rightCoords = (double *) eigen_contours[2 * i + 1];
//
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//
//
//    jobject result = env->NewObject(levelCurveClass, levelCurveConstructor, segmentsArray, vec_levels);
//
//    // Limpando
//
//    env->DeleteLocalRef(realSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//    return result;


}

JNIEXPORT jobject JNICALL Java_rpnumerics_DiscriminantLevelCurveCalc_calcNative(JNIEnv * env, jobject obj,jdouble level) {

//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//    jclass levelCurveClass = env->FindClass(CHARACTERISTICSPOLYCURVE_LOCATION);
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//
//
//    jmethodID levelCurveConstructor = env->GetMethodID(levelCurveClass, "<init>", "(Ljava/util/List;D)V");
//
//
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//
//    string id =RpNumerics::getPhysics().getSubPhysics(0).ID();
//
//
//    if (id.compare("TPCW")==0 || id.compare("Quad2")){
//        return NULL;
//        
//    }
//    
//
//    
//    int dimension = RpNumerics::getPhysics().domain().dim();
//
//    CharacteristicPolynomialLevels ec;
//
//    vector < RealVector > eigen_contours;
//
//    double vec_levels;
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
//    
//    
//    
//    ec.discriminant_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(),
//            *gv, level,eigen_contours);
//
//
//    if (eigen_contours.size() == 0)
//        return NULL;
//
//    for (int i = 0; i < eigen_contours.size() / 2; i++) {
//
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i]);
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i + 1]);
//
//
//        double * leftCoords = (double *) eigen_contours[2 * i];
//        double * rightCoords = (double *) eigen_contours[2 * i + 1];
//
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//
//
//    jobject result = env->NewObject(levelCurveClass, levelCurveConstructor, segmentsArray, vec_levels);
//
//    // Limpando
//
//    env->DeleteLocalRef(realSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//    return result;


}




JNIEXPORT jobject JNICALL Java_rpnumerics_DerivativeDiscriminantLevelCurveCalc_calcNative
  (JNIEnv *env , jobject obj , jint u){
    
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//
//    jclass realSegmentClass = env->FindClass(REALSEGMENT_LOCATION);
//
//    jclass arrayListClass = env->FindClass("java/util/ArrayList");
//    jclass levelCurveClass = env->FindClass(CHARACTERISTICSPOLYCURVE_LOCATION);
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//
//    jmethodID realSegmentConstructor = (env)->GetMethodID(realSegmentClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
//
//    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
//    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
//
//
//    jmethodID levelCurveConstructor = env->GetMethodID(levelCurveClass, "<init>", "(Ljava/util/List;D)V");
//
//
//    jobject segmentsArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
//    
//    
//    string id =RpNumerics::getPhysics().getSubPhysics(0).ID();
//    
//     if (id.compare("TPCW")==0 || id.compare("Quad2")){
//        return NULL;
//        
//    }
//    
//
//    int dimension = RpNumerics::getPhysics().domain().dim();
//
//    CharacteristicPolynomialLevels ec;
//
//    vector < RealVector > eigen_contours;
//
//    vector<double>vec_levels;
//
//    GridValues * gv = RpNumerics::getGridFactory().getGrid("bifurcationcurve");
//    
//    
//    //cout<<"Valor de u: "<<u<<endl;
//    
//    
//   ec.derivative_discriminant_curve(&RpNumerics::getPhysics().fluxFunction(), &RpNumerics::getPhysics().accumulation(),
//                                          *gv, u,
//                                          eigen_contours,
//                                          vec_levels);
//  
//    if (eigen_contours.size() == 0)
//        return NULL;
//
//    for (int i = 0; i < eigen_contours.size() / 2; i++) {
//
//
//        jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
//        jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i]);
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(eigen_contours[2 * i + 1]);
//
//
//        double * leftCoords = (double *) eigen_contours[2 * i];
//        double * rightCoords = (double *) eigen_contours[2 * i + 1];
//
//
//        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, leftCoords);
//        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, rightCoords);
//
//
//        //Construindo left e right points
//        jobject realVectorLeftPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRLeft);
//        jobject realVectorRightPoint = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenValRRight);
//
//
//        jobject realSegment = env->NewObject(realSegmentClass, realSegmentConstructor, realVectorLeftPoint, realVectorRightPoint);
//        env->CallObjectMethod(segmentsArray, arrayListAddMethod, realSegment);
//
//    }
//
//
//    int family=0;
//    jobject result = env->NewObject(levelCurveClass, levelCurveConstructor, segmentsArray, 0);
//
//    // Limpando
//
//    env->DeleteLocalRef(realSegmentClass);
//    env->DeleteLocalRef(realVectorClass);
//    env->DeleteLocalRef(arrayListClass);
//
//
//    return result;
    
    
    
    
}









