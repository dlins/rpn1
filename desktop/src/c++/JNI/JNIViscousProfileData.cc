/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIViscousProfileData.cc
 **/


//! Definition of JNIViscousProfileData
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */
#include "rpnumerics_viscousprofile_ViscousProfileData.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include "eigen.h"

JNIEXPORT void JNICALL Java_rpnumerics_viscousprofile_ViscousProfileData_updateStationaryList
(JNIEnv * env, jobject obj, jobject stationaryPointList) {


    RpNumerics::getStationaryPointVector().clear();

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass stationaryPointClass = env->FindClass(STATIONARYPOINT_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID phasePointConstructorID = env->GetMethodID(phasePointClass, "<init>", "(Lwave/util/RealVector;)V");



    jmethodID getEigenValRArrayMethod = env->GetMethodID(stationaryPointClass, "getEigenValR", "()[D");
    jmethodID getEigenValIArrayMethod = env->GetMethodID(stationaryPointClass, "getEigenValI", "()[D");
    jmethodID getEigenVecMethod = env->GetMethodID(stationaryPointClass, "getEigenVec", "()[Lwave/util/RealVector;");
    jmethodID getTypeMethod = env->GetMethodID(stationaryPointClass, "getType", "()I");



    jmethodID arrayListGetMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");

    jmethodID arrayListSizeMethod = env->GetMethodID(arrayListClass, "size", "()I");

    jmethodID getPointMethod = env->GetMethodID(stationaryPointClass, "getPoint", "()Lrpnumerics/PhasePoint;");



    jmethodID stationaryPointConstructorID = env->GetMethodID(stationaryPointClass, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");


    int stationaryPointListSize = env->CallIntMethod(stationaryPointList, arrayListSizeMethod);


    int dimension = 2;



    for (int i = 0; i < stationaryPointListSize; i++) {

        jobject statPoint = env->CallObjectMethod(stationaryPointList, arrayListGetMethod, i);

        jobject statPointCoord = env->CallObjectMethod(statPoint, getPointMethod);

        jdoubleArray statPointCoordArray = (jdoubleArray) env->CallObjectMethod(statPointCoord, toDoubleMethodID);

        double statPointCoordsBuffer [dimension];

        env->GetDoubleArrayRegion(statPointCoordArray, 0, dimension, statPointCoordsBuffer);


        RealVector nativeStatPointCoord(dimension, statPointCoordsBuffer);

        jdoubleArray statPointValRArray = (jdoubleArray) env->CallObjectMethod(statPoint, getEigenValRArrayMethod);
        jdoubleArray statPointValIArray = (jdoubleArray) env->CallObjectMethod(statPoint, getEigenValIArrayMethod);


        double statPointValRBuffer [dimension];

        env->GetDoubleArrayRegion(statPointValRArray, 0, dimension, statPointValRBuffer);



        StationaryPoint * nativeStationaryPoint = new StationaryPoint(nativeStatPointCoord, env->CallIntMethod(statPoint, getTypeMethod));


        vector<StationaryPoint *> * sPointList = &RpNumerics::getStationaryPointVector();

        sPointList->push_back(nativeStationaryPoint);


    }




    vector<StationaryPoint*> * sPointList = &RpNumerics::getStationaryPointVector();


    for (int i = 0; i < sPointList->size(); i++) {
        cout << sPointList->at(i)->coords() << " " << sPointList->at(i)->type() << endl;

    }










}
