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


#include "RpNumerics.h"

//JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_calc(JNIEnv *env, jobject obj, jobject initialPoint) {

JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_nativeCalc(JNIEnv *env, jobject obj, jobject equiPoint, jobject refPoint, jdouble sigma) {
//
//    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
//    jclass stationaryPointClass = env->FindClass(STATIONARYPOINT_LOCATION);
//    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);
//
//
//    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
//    jmethodID phasePointConstructorID = env->GetMethodID(phasePointClass, "<init>", "(Lwave/util/RealVector;)V");
//
//    jmethodID stationaryPointConstructorID = env->GetMethodID(stationaryPointClass, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;)V");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
//
//    
//    jdoubleArray equiPointArray = (jdoubleArray) (env)->CallObjectMethod(equiPoint, toDoubleMethodID);
//
//
//    jdoubleArray refPointArray = (jdoubleArray) (env)->CallObjectMethod(refPoint, toDoubleMethodID);
//
//
//    int dimension = env->GetArrayLength(refPointArray);
//
//    double equiPointBuffer [dimension];
//
//    env->GetDoubleArrayRegion(equiPointArray, 0, dimension, equiPointBuffer);
//
//    double refPointBuffer [dimension];
//
//    env->GetDoubleArrayRegion(refPointArray, 0, dimension, refPointBuffer);
//
//    RealVector nativeEquiPoint(dimension, equiPointBuffer);
//
//    RealVector nativeRefPoint(dimension, refPointBuffer);
//
//
//
//    //    jobject result = env->NewObject(classStationaryPoint_, stationaryPointConstructor_, initialPoint, tempArray, tempArray, eigenVec, DimP, schurFormP, schurVecP, DimP, schurFormN, schurVecN, integrationFlag);
//
//    vector<RealVector> cp;
//
//
//    vector<eigenpair> ep;
//
//
//    const FluxFunction *fluxFunction = &RpNumerics::getPhysics().fluxFunction();
//    const AccumulationFunction * accumFunction = &RpNumerics::getPhysics().accumulation();
//
//
//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout << "Parametros nos estacionarios: " << RpNumerics::getPhysics().fluxFunction().fluxParams().params()<<endl;
//        ////cout <<" Native equipoint: "<<nativeEquiPoint<<endl;
//        ////cout <<" Native ref: "<<nativeRefPoint<<endl;
//    }
//
//    Viscosity_Matrix v;
//    Viscous_Profile::critical_points_linearization(fluxFunction, accumFunction,
//            &v, sigma, nativeEquiPoint, nativeRefPoint, ep);
//
//    for (int i = 0; i < nativeEquiPoint.size(); i++) {
//        equiPointBuffer[i] = nativeEquiPoint(i);
//
//
//    }
//
//    
//    
//    jdoubleArray eigenValR = env->NewDoubleArray(ep.size());
//
//    jdoubleArray eigenValI = env->NewDoubleArray(ep.size());
//
//    jobjectArray eigenVecArray = env->NewObjectArray(ep.size(), realVectorClass, NULL);
//
//    double eigenValRBuffer [ep.size()];
//    double eigenValIBuffer [ep.size()];
//
//
//    if ( Debug::get_debug_level() == 5 ) {
//        ////cout <<"Ponto passado: "<<nativeEquiPoint<<endl;
//    }
//
//
//
//
//    for (int i = 0; i < ep.size(); i++) {
//
//
//        eigenValIBuffer[i] = ep[i].i;
//        eigenValRBuffer[i] = ep[i].r;
//
//
//
//
//        if ( Debug::get_debug_level() == 5 ) {
//            ////cout << "Parte real: " << ep[i].r << endl;
//            ////cout << "Parte imaginaria: " << ep[i].i << endl;
//        }
//
//
//
//        vector<double> vrrVector = ep[i].vrr; // Real part
//
//        double eigenVecBuffer [vrrVector.size()];
//
//        for (int j = 0; j < vrrVector.size(); j++) {
//
//            eigenVecBuffer[j] = vrrVector[j];
//
//
//        }
//
//
//        jdoubleArray eigenVecR = env->NewDoubleArray(ep.size());
//
//        (env)->SetDoubleArrayRegion(eigenVecR, 0, ep.size(), eigenVecBuffer);
//
//
//        jobject eigenVectorRealVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, eigenVecR);
//
//
//        (env)->SetObjectArrayElement(eigenVecArray, i, eigenVectorRealVector);
//
//    }
//
//    (env)->SetDoubleArrayRegion(eigenValR, 0, ep.size(), eigenValRBuffer);
//    (env)->SetDoubleArrayRegion(eigenValI, 0, ep.size(), eigenValIBuffer);
//
//
//    jdoubleArray stationaryPointCoords = env->NewDoubleArray(dimension);
//
//    env->SetDoubleArrayRegion(stationaryPointCoords, 0, dimension, equiPointBuffer);
//
//    jobject stationaryPointLocation = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, stationaryPointCoords);
//
//    jobject stationaryPointCoordsPhasePoint = env->NewObject(phasePointClass, phasePointConstructorID, stationaryPointLocation);
//
//    jobject stationaryPoint = env->NewObject(stationaryPointClass, stationaryPointConstructorID, stationaryPointCoordsPhasePoint, eigenValR, eigenValI, eigenVecArray);
//
//    
//    return stationaryPoint;
//
//


}
