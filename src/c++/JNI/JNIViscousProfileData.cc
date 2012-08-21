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




    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass stationaryPointClass = env->FindClass(STATIONARYPOINT_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);
    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    jmethodID phasePointConstructorID = env->GetMethodID(phasePointClass, "<init>", "(Lwave/util/RealVector;)V");



    jmethodID getEigenValRArrayMethod = env->GetMethodID(stationaryPointClass, "getEigenValR", "()[D");
    jmethodID getEigenValIArrayMethod = env->GetMethodID(stationaryPointClass, "getEigenValI", "()[D");
    jmethodID getEigenVecMethod = env->GetMethodID(stationaryPointClass, "getEigenVec", "()[Lwave/util/RealVector;");



    jmethodID arrayListGetMethod = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");

    jmethodID arrayListSizeMethod = env->GetMethodID(arrayListClass, "size", "()I");

    jmethodID getPointMethod = env->GetMethodID(stationaryPointClass, "getPoint", "()Lrpnumerics/PhasePoint;");



    jmethodID stationaryPointConstructorID = env->GetMethodID(stationaryPointClass, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");


    int stationaryPointListSize = env->CallIntMethod(stationaryPointList, arrayListSizeMethod);


    int dimension = 2;



    for (int i = 0; i < stationaryPointListSize; i++) {



        vector<eigenpair> eigenpairVector;

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

        double statPointValIBuffer [dimension];

        env->GetDoubleArrayRegion(statPointValIArray, 0, dimension, statPointValIBuffer);

        cout << nativeStatPointCoord << " " << statPointValRBuffer[0] << " " << statPointValRBuffer[1] << " " << statPointValIBuffer[0] << " " << statPointValIBuffer[1] << endl;

        jobjectArray eigenVectorArray = (jobjectArray) env->CallObjectMethod(statPoint, getEigenVecMethod);

        int eigenVectorArraySize = env->GetArrayLength(eigenVectorArray);



        for (int j = 0; j < eigenVectorArraySize; j++) {

            eigenpair eigenPair;

            jobject eigenVector = env->GetObjectArrayElement(eigenVectorArray, j);

            double statPointVecBuffer [dimension];

            jdoubleArray eigenVectorDoubleArray = (jdoubleArray) env->CallObjectMethod(eigenVector, toDoubleMethodID);

            env->GetDoubleArrayRegion(eigenVectorDoubleArray, 0, dimension, statPointVecBuffer);


            vector<double> vrr;


            for (int k = 0; k < dimension; k++) {

                vrr.push_back(statPointVecBuffer[k]);

            }

            eigenPair.r = statPointValRBuffer[j];

            eigenpairVector.push_back(eigenPair);


        }


        StationaryPoint nativeStationaryPoint (nativeStatPointCoord,eigenpairVector);



    }




    vector<StationaryPoint> * sPointList = &RpNumerics::getStationaryPointVector();


    //
    //    int dimension = env->GetArrayLength(refPointArray);
    //
    //    double equiPointBuffer [dimension];
    //
    //    env->GetDoubleArrayRegion(equiPointArray, 0, dimension, equiPointBuffer);
    //
    //
    //
    //    double refPointBuffer [dimension];
    //
    //    env->GetDoubleArrayRegion(refPointArray, 0, dimension, refPointBuffer);
    //
    //    RealVector nativeEquiPoint(dimension, equiPointBuffer);
    //
    //    RealVector nativeRefPoint(dimension, refPointBuffer);



    //    vector<RealVector> cp;


    vector<eigenpair> ep;



    //cout << "Parametros nos estacionarios: " << RpNumerics::getPhysics().fluxFunction().fluxParams().params()<<endl;

    //
    //
    //    for (int i = 0; i < nativeEquiPoint.size(); i++) {
    //        equiPointBuffer[i] = nativeEquiPoint(i);
    //
    //
    //    }
    //
    ////
    ////
    ////    jdoubleArray eigenValR = env->NewDoubleArray(ep.size());
    //
    //    jdoubleArray eigenValI = env->NewDoubleArray(ep.size());
    //
    //    jobjectArray eigenVecArray = env->NewObjectArray(ep.size(), realVectorClass, NULL);
    //
    //    double eigenValRBuffer [ep.size()];
    //    double eigenValIBuffer [ep.size()];
    //
    //
    //    //cout <<"Ponto passado: "<<nativeEquiPoint<<endl;
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
    //        //cout << "Parte real: " << ep[i].r << endl;
    //        //        cout << "Parte imaginaria: " << ep[i].i << endl;
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







}
