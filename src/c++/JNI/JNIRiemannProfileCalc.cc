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


#include "rpnumerics_RiemannProfileCalc.h"
#include "RiemannSolver.h"
#include "WaveCurve.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_RiemannProfileCalc_nativeCalc
(JNIEnv * env, jobject obj, jobject pmin, jobject pmax, jobject forwardList, jobject backwardList) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurveOrbit = (env)->FindClass(WAVECURVEORBIT_LOCATION);


    jclass arrayListClass = env->FindClass("java/util/List");

    jmethodID getCorrespondingCurveIndexMethodID = (env)->GetMethodID(classOrbitPoint, "getCorrespondingCurveIndex", "()I");
    jmethodID getCorrespondingPointMethodID = (env)->GetMethodID(classOrbitPoint, "getCorrespondingPointIndex", "()I");
    jmethodID getLambdaID = (env)->GetMethodID(classOrbitPoint, "getLambda", "()D");

    jmethodID getCurveTypeMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getCurveType", "()I");
    jmethodID getCurveIndexMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getCurveIndex", "()I");
    jmethodID isInitialSubCurveID = (env)->GetMethodID(classWaveCurveOrbit, "isInitialSubCurve", "()Z");


    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID getOrbitPointsMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getPoints", "()[Lrpnumerics/OrbitPoint;");
    jmethodID arrayListGetMethodID = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID arrayListSizeMethodID = env->GetMethodID(arrayListClass, "size", "()I");



    int dimension = 2;

    double limitsBuffer[dimension];

    //Processing downLeft area limit


    jdoubleArray downLeftArray = (jdoubleArray) env->CallObjectMethod(pmin, toDoubleMethodID);
    env->GetDoubleArrayRegion(downLeftArray, 0, env->GetArrayLength(downLeftArray), limitsBuffer);
    RealVector nativeDownLeft(dimension, limitsBuffer);

    //Processing topRight area limit


    jdoubleArray topRightArray = (jdoubleArray) env->CallObjectMethod(pmax, toDoubleMethodID);
    env->GetDoubleArrayRegion(topRightArray, 0, env->GetArrayLength(topRightArray), limitsBuffer);
    RealVector nativeTopRight(dimension, limitsBuffer);

    //Processing forward wavecurve orbit list


    int forwardListSize = env->CallIntMethod(forwardList, arrayListSizeMethodID);

//    cout << "Tamanho da lista para frente: " << forwardListSize << endl;

    vector<Curve> forwardCurvesVector;
    vector<Curve> backwardCurvesVector;

    for (int i = 0; i < forwardListSize; i++) {

        vector<RealVector> orig;
        vector<int>curve_corresponding;
        vector<int>orig_corresponding;

        jobject waveCurveOrbit = env->CallObjectMethod(forwardList, arrayListGetMethodID, i);

        jobjectArray orbitPointArray = (jobjectArray) (env)->CallObjectMethod(waveCurveOrbit, getOrbitPointsMethodID);

        int orbitPointArraySize = env->GetArrayLength(orbitPointArray);

        for (int j = 0; j < orbitPointArraySize; j++) {

            jobject orbitPoint = env->GetObjectArrayElement(orbitPointArray, j);


            jdoubleArray orbitPointCoordsArray = (jdoubleArray) (env)->CallObjectMethod(orbitPoint, toDoubleMethodID);

            double pointBuffer [dimension+1];

            env->GetDoubleArrayRegion(orbitPointCoordsArray, 0, dimension, pointBuffer);

            pointBuffer[dimension]=env->CallDoubleMethod(orbitPoint,getLambdaID);
            RealVector nativeOrbitPointCoords(dimension+1, pointBuffer);



            int correspondingCurveIndex = (env)->CallIntMethod(orbitPoint, getCorrespondingCurveIndexMethodID);
            int correspondingPointIndex = (env)->CallIntMethod(orbitPoint, getCorrespondingPointMethodID);


            curve_corresponding.push_back(correspondingCurveIndex);
            orig_corresponding.push_back(correspondingPointIndex);
            orig.push_back(nativeOrbitPointCoords);

            int curveIndex = (env)->CallIntMethod(waveCurveOrbit, getCurveIndexMethodID);
            int curveType = (env)->CallIntMethod(waveCurveOrbit, getCurveTypeMethodID);

//            cout << "Curva para frente C: " << pointBuffer[0] << " " << pointBuffer[1] << " " << correspondingCurveIndex << " " << correspondingPointIndex << " " << curveIndex << " " << curveType << endl;


        }
        int curveType = (env)->CallIntMethod(waveCurveOrbit, getCurveTypeMethodID);
        int curveIndex = (env)->CallIntMethod(waveCurveOrbit, getCurveIndexMethodID);
        bool initialSubCurve = env->CallBooleanMethod(waveCurveOrbit, isInitialSubCurveID);

        Curve curve(orig, curve_corresponding, orig_corresponding,
                curveType, curveIndex, initialSubCurve);

        forwardCurvesVector.push_back(curve);

    }



    int backwardListSize = env->CallIntMethod(backwardList, arrayListSizeMethodID);

//    cout << "Tamanho da lista para tras: " << backwardListSize << endl;



    for (int i = 0; i < backwardListSize; i++) {

        vector<RealVector> orig;
        vector<int>curve_corresponding;
        vector<int>orig_corresponding;

        jobject waveCurveOrbit = env->CallObjectMethod(backwardList, arrayListGetMethodID, i);

        jobjectArray orbitPointArray = (jobjectArray) (env)->CallObjectMethod(waveCurveOrbit, getOrbitPointsMethodID);

        int orbitPointArraySize = env->GetArrayLength(orbitPointArray);

        for (int j = 0; j < orbitPointArraySize; j++) {

            jobject orbitPoint = env->GetObjectArrayElement(orbitPointArray, j);
            jdoubleArray orbitPointCoordsArray = (jdoubleArray) (env)->CallObjectMethod(orbitPoint, toDoubleMethodID);


            double pointBuffer [dimension + 1];
            env->GetDoubleArrayRegion(orbitPointCoordsArray, 0, dimension, pointBuffer);
            pointBuffer[dimension] = env->CallDoubleMethod(orbitPoint, getLambdaID);
            RealVector nativeOrbitPointCoords(dimension+1, pointBuffer);


            int correspondingCurveIndex = (env)->CallIntMethod(orbitPoint, getCorrespondingCurveIndexMethodID);
            int correspondingPointIndex = (env)->CallIntMethod(orbitPoint, getCorrespondingPointMethodID);


            curve_corresponding.push_back(correspondingCurveIndex);
            orig_corresponding.push_back(correspondingPointIndex);
            orig.push_back(nativeOrbitPointCoords);

            int curveIndex = (env)->CallIntMethod(waveCurveOrbit, getCurveIndexMethodID);
            int curveType = (env)->CallIntMethod(waveCurveOrbit, getCurveTypeMethodID);

//            cout << "Curva para tras C: " << pointBuffer[0] << " " << pointBuffer[1] << " " << correspondingCurveIndex << " " << correspondingPointIndex << " " << curveIndex << " " << curveType << endl;


        }
        int curveType = (env)->CallIntMethod(waveCurveOrbit, getCurveTypeMethodID);
        int curveIndex = (env)->CallIntMethod(waveCurveOrbit, getCurveIndexMethodID);
        bool initialSubCurve = env->CallBooleanMethod(waveCurveOrbit, isInitialSubCurveID);

        Curve curve(orig, curve_corresponding, orig_corresponding,
                curveType, curveIndex, isInitialSubCurveID);

        backwardCurvesVector.push_back(curve);

    }



    vector<RealVector> profile;


    int solverOutput = RiemannSolver::saturation_profiles(forwardCurvesVector, // Family 0, forward
            backwardCurvesVector, // Family 1, backward
           nativeDownLeft,nativeTopRight,
            1.0,
           profile);


    cout<<"Retorno do perfil: "<<solverOutput<<endl;

    cout<<"Coordenadas do perfil"<<endl;
    for (int i = 0; i < profile.size(); i++) {
        cout <<profile.at(i)(0)<<" "<<profile.at(i)(1)<<" "<<profile.at(i)(2)<<endl;
    }


    return NULL;




}
















