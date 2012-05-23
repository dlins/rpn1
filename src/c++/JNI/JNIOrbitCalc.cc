/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIOrbitCalc.cc
 **/


//! Definition of JNIOrbitCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_OrbitCalc.h"
#include "Viscous_Profile.h"
#include "Rarefaction.h"
#include "RpNumerics.h"
#include "JNIDefs.h"
#include <vector>

using std::vector;

JNIEXPORT jobject JNICALL Java_rpnumerics_OrbitCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jobject referencePoint, jdouble sigma, jint timeDirection) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classOrbit = (env)->FindClass(ORBIT_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);

    jmethodID orbitPointConstructorID = (env)->GetMethodID(classOrbitPoint, "<init>", "(Lwave/util/RealVector;)V");
    jmethodID orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArrayID = env->GetMethodID(realVectorClass, "<init>", "([D)V");


    //Input processing
    //    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    //
    //    double input [env->GetArrayLength(phasePointArray)];
    //
    //    env->GetDoubleArrayRegion(phasePointArray, 0, env->GetArrayLength(phasePointArray), input);
    //
    //    env->DeleteLocalRef(phasePointArray);
    //


    jdoubleArray initialPointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);


    jdoubleArray referencePointArray = (jdoubleArray) (env)->CallObjectMethod(referencePoint, toDoubleMethodID);


    int dimension = env->GetArrayLength(referencePointArray);

    double equiPointBuffer [dimension];

    env->GetDoubleArrayRegion(initialPointArray, 0, dimension, equiPointBuffer);

    double refPointBuffer [dimension];

    env->GetDoubleArrayRegion(referencePointArray, 0, dimension, refPointBuffer);

    RealVector nativeEquiPoint(dimension, equiPointBuffer);

    RealVector nativeRefPoint(dimension, refPointBuffer);

    const FluxFunction *fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumFunction = &RpNumerics::getPhysics().accumulation();

    const Boundary * boundary = &RpNumerics::getPhysics().boundary();

    Viscosity_Matrix v;


    double deltaxi = 1e-2;

    std::vector<RealVector> coords;

    //TODO Remove

    if (timeDirection==RAREFACTION_SPEED_INCREASE)
        timeDirection=ORBIT_FORWARD;

    if (timeDirection==RAREFACTION_SPEED_DECREASE)
        timeDirection=ORBIT_BACKWARD;

    Viscous_Profile::orbit(fluxFunction, accumFunction,
            &v,
            boundary,
            nativeEquiPoint, nativeRefPoint, sigma,
            deltaxi,
            timeDirection,
            coords);

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);


    //Orbit memebers creation
    for (int i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;

        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject realVectorCoords = env->NewObject(realVectorClass,realVectorConstructorDoubleArrayID,jTempArray);

        //Lambda is the last component.
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructorID, realVectorCoords);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit


    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor_, orbitPointArray, timeDirection);

    //Cleaning up



    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classOrbit);


    return orbit;

}

//----------------------------------------------------------------Stub------------------------------------------------------------------
//    TODO Obter os pontos do ODESolution retornado pelo ODESolver

//    int orbitPointArraySize =  solution->getCoords().size(); // TODO Substituir pelo tamanho do array de pontos retornado pelo ODESolution
//
//    jobjectArray  orbitPointArray  = (env)->NewObjectArray(orbitPointArraySize, classOrbitPoint, NULL);
//
//
//    for(i=0;i < solution->getCoords().size();i++ ){
//
//        jdoubleArray jTempArray = (env)->NewDoubleArray(profile.getDimension());
//
//        RealVector vectorCoords = solution->getCoords().at(i);
//
//        double bufferArray [profile.getDimension()] ;
//
//        for (j=0; j <profile.getDimension();j++ ){
//
//            bufferArray[j]=vectorCoords.component(j);
//        }
//
//        (env)->SetDoubleArrayRegion(jTempArray, 0, profile.getDimension(), bufferArray);
//
//        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);
//
//        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
//
//        (env)->DeleteLocalRef(orbitPoint);
//        (env)->DeleteLocalRef(jTempArray);
//
////        cout << solution->getCoords().at(i) << endl;
//
//    }
//
//    //Building the orbit
//
//    jclass classOrbit = (env)->FindClass("rpnumerics/Orbit");
//
//    jmethodID orbitConstructor = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
//
//    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor, orbitPointArray, timeDirection);
//
//    return orbit;
//---------------------------------------------------------------------------------------------------------------------------------------

//------------------------------- Stub ---------------------------------
//
//    double arrayStub [2];
//
//    arrayStub[0]= 0.10;
//    arrayStub[1]= 0.10;
//
//    jdoubleArray jarrayStub = (env)->NewDoubleArray(2);
//
//    (env)->SetDoubleArrayRegion(jarrayStub, 0, 2, arrayStub);
//
//
//    jobjectArray  orbitPointArray  = (env)->NewObjectArray(1, classOrbitPoint, NULL);
//
//    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
//
//    jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jarrayStub);
//
//    (env)->SetObjectArrayElement(orbitPointArray, 0, orbitPoint);
//
//    //Construindo a Orbita
//
//    jclass classOrbit = (env)->FindClass("rpnumerics/Orbit");
//
//    jmethodID orbitConstructor = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
//
//    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor, orbitPointArray, 0);
//
//    return orbit;
//

//---------------------------------------------------------------------------------



