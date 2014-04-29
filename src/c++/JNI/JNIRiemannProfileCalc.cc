/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/



/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_RiemannProfileCalc.h"
#include "RiemannProblem.h"
#include "WaveCurveFactory.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include "Debug.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_RiemannProfileCalc_nativeCalc
(JNIEnv * env, jobject obj, jobject pmin, jobject pmax, jobject forwardList, jobject backwardList) {


    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classWaveCurveOrbit = (env)->FindClass(WAVECURVEORBIT_LOCATION);
    jclass classRiemannProfile = (env)->FindClass(RIEMANNPROFILE_LOCATION);


    jclass arrayListClass = env->FindClass("java/util/List");

    jmethodID getCorrespondingCurveIndexMethodID = (env)->GetMethodID(classOrbitPoint, "getCorrespondingCurveIndex", "()I");

    jmethodID riemannProfileConstructorID = (env)->GetMethodID(classRiemannProfile, "<init>", "([Lrpnumerics/OrbitPoint;)V");


    jmethodID getCorrespondingPointMethodID = (env)->GetMethodID(classOrbitPoint, "getCorrespondingPointIndex", "()I");
    jmethodID getLambdaID = (env)->GetMethodID(classOrbitPoint, "getLambda", "()D");

    jmethodID getCurveTypeMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getCurveType", "()I");
    jmethodID getCurveIndexMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getCurveIndex", "()I");
    jmethodID isInitialSubCurveID = (env)->GetMethodID(classWaveCurveOrbit, "isInitialSubCurve", "()Z");


    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");

    jmethodID getOrbitPointsMethodID = (env)->GetMethodID(classWaveCurveOrbit, "getPoints", "()[Lrpnumerics/OrbitPoint;");
    jmethodID arrayListGetMethodID = env->GetMethodID(arrayListClass, "get", "(I)Ljava/lang/Object;");
    jmethodID arrayListSizeMethodID = env->GetMethodID(arrayListClass, "size", "()I");

    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID setLambdaID = (env)->GetMethodID(classOrbitPoint, "setLambda", "(D)V");

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

    const WaveCurve * waveCurve1 = RpNumerics::getWaveCurve(0);
    const WaveCurve * waveCurve2 = RpNumerics::getWaveCurve(1);


    RealVector intersectionPoint(dimension);
    int subcurve1;
    int subcurve2;

    int subPoint1;
    int subPoint2;

    cout << "Chamando Sol de Riemann" << endl;

    int intersectionCode = WaveCurveFactory::intersection(*waveCurve1, *waveCurve2, nativeDownLeft, nativeTopRight,
            intersectionPoint, subcurve1, subPoint1, subcurve2, subPoint2);



    RiemannProblem riemanProblem;

    vector<RealVector> profile;
    vector<double> speedVector;

    riemanProblem.profile(*waveCurve1, subcurve1, subPoint1, 0,
            *waveCurve2, subcurve2, subPoint2, 1,
            profile, speedVector);

    
//    riemanProblem.all_increase_profile(*waveCurve1, subcurve1, subPoint1, 0,
//            *waveCurve2, subcurve2, subPoint2, 1,
//            profile, speedVector);






    if (Debug::get_debug_level() == 5) {
        cout << "DownLeft: " << nativeDownLeft << " TopRight" << nativeTopRight << endl;
    }





    if (profile.size() == 0)
        return NULL;


//    cout << "Coordenadas do perfil" << endl;
//    for (int i = 0; i < profile.size(); i++) {
//        cout  << profile.at(i)(0) << " " << profile.at(i)(1) << endl;
//    }
//    for (int i = 0; i < speedVector.size(); i++) {
//
//        cout  << speedVector.at(i)<< endl;
//
//    }


    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(profile.size(), classOrbitPoint, NULL);
    for (int i = 0; i < profile.size(); i++) {

        RealVector tempVector = profile.at(i);
        tempVector.resize(3);
        tempVector[2]=speedVector[i];



        if (Debug::get_debug_level() == 5) {
            cout << tempVector << endl;
        }

        double * dataCoords = tempVector;

        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, dimension, dataCoords);

        //Lambda is the last component.
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);
        env->CallVoidMethod(orbitPoint, setLambdaID, speedVector[i]);


        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);


    }

    jobject riemannProfile = (env)->NewObject(classRiemannProfile, riemannProfileConstructorID, orbitPointArray);


    return riemannProfile;




}
















