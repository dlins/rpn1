/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIStationaryPointCalc.cc
 **/


//! Definition of JNIStationaryPointCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
*/

#include "rpnumerics_StationaryPointCalc.h"
#include "JNIDefs.h"

JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_calc(JNIEnv *env, jobject obj, jobject initialPoint){
    
    jclass  realVectorClass_= env->FindClass(REALVECTOR_LOCATION);
    jclass  realMatrix2Class_= env->FindClass(REALMATRIX2_LOCATION);
    jclass  classStationaryPoint_=env->FindClass(STATIONARYPOINT_LOCATION);
    jclass    classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    
    jmethodID    stationaryPointConstructor_=env->GetMethodID(classStationaryPoint_, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;I)V");
    jmethodID    realVectorConstructorDoubleArray_= env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    jmethodID    realMatrix2Constructor_= env->GetMethodID(realMatrix2Class_, "<init>", "(II[D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
     //Input processing
    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    int dimension = env->GetArrayLength(phasePointArray);
    
    double input [dimension];
    
    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
    
    env->DeleteLocalRef(phasePointArray);
    
    //Calculations 
    
    double * teste = new double [2];
    
    teste[0]=0.1;
    teste[1]=0.1;

    double  * realMatrixTeste= new double [4];
    
    realMatrixTeste[0]=0.1;
    realMatrixTeste[1]=0.1;
    realMatrixTeste[2]=0.1;
    realMatrixTeste[3]=0.1;
    
    //Constructor parameters
    
    
    //RealVector [] eigenVec creation 
    
    jobjectArray eigenVec =(jobjectArray) env->NewObjectArray(dimension, realVectorClass_, NULL);
    
    jdoubleArray tempArray = env->NewDoubleArray(dimension) ;
    
    env->SetDoubleArrayRegion(tempArray, 0,dimension, teste);
    
    jobject tempRealVector1 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    jobject tempRealVector2 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    env->SetObjectArrayElement(eigenVec, 0, tempRealVector1);
    env->SetObjectArrayElement(eigenVec, 1, tempRealVector2);
    
    // DimP
    int DimP=2;
    
    // RealMatirx schurFormP, schurVecP, schurFormN, schurVecN creation
    jdoubleArray tempArrayMATRIX = env->NewDoubleArray(4);
    
    env->SetDoubleArrayRegion(tempArrayMATRIX, 0, 4, realMatrixTeste);
    
    jobject schurFormP=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurVecP=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurFormN=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurVecN=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    // Integration flag
    
    int integrationFlag=1;
    
    jobject result = env->NewObject(classStationaryPoint_, stationaryPointConstructor_, initialPoint, tempArray, tempArray, eigenVec, DimP, schurFormP, schurVecP, DimP, schurFormN, schurVecN, integrationFlag);
    
    return result;
    
    
//    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec, int DimP,
//            RealMatrix2 schurFormP, RealMatrix2 schurVecP, int DimN, RealMatrix2 schurFormN,
//            RealMatrix2 schurVecN, int integrationFlag) {
    
    
}
