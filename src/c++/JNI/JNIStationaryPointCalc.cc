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
#include "Viscous_Profile.h"
#include "RpNumerics.h"

//JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_calc(JNIEnv *env, jobject obj, jobject initialPoint) {

JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_calc(JNIEnv *env, jobject equiPoint, jobject refPoint, jdouble sigma) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass classStationaryPoint = env->FindClass(STATIONARYPOINT_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID phasePointConstructorID = env->GetMethodID(phasePointClass, "<init>", "(Lrpnumerics/RealVector;)V");

    jmethodID stationaryPointConstructor = env->GetMethodID(classStationaryPoint, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    //Input processing
    //
    //    double input [dimension];
    //
    //    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);

    //    env->DeleteLocalRef(phasePointArray);

    //Calculations 

    //    double * teste = new double [2];
    //
    //    teste[0] = 0.1;
    //    teste[1] = 0.1;
    //
    //    double * realMatrixTeste = new double [4];
    //
    //    realMatrixTeste[0] = 0.1;
    //    realMatrixTeste[1] = 0.1;
    //    realMatrixTeste[2] = 0.1;
    //    realMatrixTeste[3] = 0.1;

    //Constructor parameters


    //RealVector [] eigenVec creation 

    //    jobjectArray eigenVec = (jobjectArray) env->NewObjectArray(dimension, realVectorClass_, NULL);
    //
    //    jdoubleArray tempArray = env->NewDoubleArray(dimension);
    //
    //    env->SetDoubleArrayRegion(tempArray, 0, dimension, teste);
    //
    //    jobject tempRealVector1 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    //
    //    jobject tempRealVector2 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    //
    //    env->SetObjectArrayElement(eigenVec, 0, tempRealVector1);
    //    env->SetObjectArrayElement(eigenVec, 1, tempRealVector2);
    //
    //    // DimP
    //    int DimP = 2;

    // RealMatirx schurFormP, schurVecP, schurFormN, schurVecN creation
    //    jdoubleArray tempArrayMATRIX = env->NewDoubleArray(4);
    //
    //    env->SetDoubleArrayRegion(tempArrayMATRIX, 0, 4, realMatrixTeste);
    //
    //    jobject schurFormP = env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX);
    //
    //    jobject schurVecP = env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX);
    //
    //    jobject schurFormN = env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX);
    //
    //    jobject schurVecN = env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX);
    //
    //    // Integration flag
    //
    //    int integrationFlag = 1;




    jdoubleArray equiPointArray = (jdoubleArray) (env)->CallObjectMethod(equiPoint, toDoubleMethodID);


    jdoubleArray refPointArray = (jdoubleArray) (env)->CallObjectMethod(refPoint, toDoubleMethodID);


    int dimension = env->GetArrayLength(refPointArray);

    double equiPointBuffer [dimension];

    env->GetDoubleArrayRegion(equiPointArray, 0, dimension, equiPointBuffer);

    double refPointBuffer [dimension];

    env->GetDoubleArrayRegion(refPointArray, 0, dimension, refPointBuffer);

    RealVector nativeEquiPoint(dimension, equiPointBuffer);

    RealVector nativeRefPoint(dimension, refPointBuffer);



    //    jobject result = env->NewObject(classStationaryPoint_, stationaryPointConstructor_, initialPoint, tempArray, tempArray, eigenVec, DimP, schurFormP, schurVecP, DimP, schurFormN, schurVecN, integrationFlag);

    vector<RealVector> cp;


    vector<eigenpair> ep;


    const FluxFunction *fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumFunction = &RpNumerics::getPhysics().accumulation();

    Viscosity_Matrix v;
    Viscous_Profile::critical_points_linearization(fluxFunction, accumFunction,
            &v, sigma, nativeEquiPoint, nativeRefPoint, ep);


    jdoubleArray stationaryPointCoords = env->NewDoubleArray(dimension);

    env->SetDoubleArrayRegion(stationaryPointCoords, 0, dimension, equiPointBuffer);

    jobject stationaryPointLocation = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, stationaryPointCoords);



    jobject stationaryPointPhasePoint = env->NewObject(phasePointClass,phasePointConstructorID,stationaryPointLocation);












    //   return result;


    //    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec, int DimP,
    //            RealMatrix2 schurFormP, RealMatrix2 schurVecP, int DimN, RealMatrix2 schurFormN,
    //            RealMatrix2 schurVecN, int integrationFlag) {


}
