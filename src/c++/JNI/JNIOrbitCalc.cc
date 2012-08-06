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

JNIEXPORT jobject JNICALL Java_rpnumerics_OrbitCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jobject referencePoint, jdouble sigma, jint timeDirection, jobjectArray poincareSection) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classOrbit = (env)->FindClass(ORBIT_LOCATION);
    jclass phasePointClass = (env)->FindClass(PHASEPOINT_LOCATION);

    jmethodID orbitPointConstructorID = (env)->GetMethodID(classOrbitPoint, "<init>", "(Lwave/util/RealVector;)V");
    jmethodID orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    jmethodID realVectorConstructorDoubleArrayID = env->GetMethodID(realVectorClass, "<init>", "([D)V");







    //Input processing

    //
    //    double input [env->GetArrayLength(phasePointArray)];
    //

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


    double deltaxi = 1e-2; //original = 1e-2

    std::vector<RealVector> coords;

    //TODO Remove

    if (timeDirection == RAREFACTION_SPEED_INCREASE)
        timeDirection = ORBIT_FORWARD;

    if (timeDirection == RAREFACTION_SPEED_DECREASE)
        timeDirection = ORBIT_BACKWARD;

    if (poincareSection != NULL) { //Apenas para um segmento


        RealVector nativePoincarePoint1(2);
        RealVector nativePoincarePoint2(2);
        vector<RealVector> poincareSegment;

        jobject poincarePoint1 = env->GetObjectArrayElement(poincareSection, 0);

        jobject poincarePoint2 = env->GetObjectArrayElement(poincareSection, 1);

        jdoubleArray poincare1PointArray = (jdoubleArray) (env)->CallObjectMethod(poincarePoint1, toDoubleMethodID);
        jdoubleArray poincare2PointArray = (jdoubleArray) (env)->CallObjectMethod(poincarePoint2, toDoubleMethodID);

        double tempPoint1[2];
        double tempPoint2[2];

        env->GetDoubleArrayRegion(poincare1PointArray, 0, 2, tempPoint1);
        env->GetDoubleArrayRegion(poincare2PointArray, 0, 2, tempPoint2);


        nativePoincarePoint1.component(0) = tempPoint1[0];
        nativePoincarePoint1.component(1) = tempPoint1[1];


        nativePoincarePoint2.component(0) = tempPoint2[0];
        nativePoincarePoint2.component(1) = tempPoint2[1];

        poincareSegment.push_back(nativePoincarePoint1);
        poincareSegment.push_back(nativePoincarePoint2);

        Viscous_Profile::orbit(fluxFunction, accumFunction,
                &v,
                boundary,
                nativeEquiPoint, nativeRefPoint, sigma,
                deltaxi,
                timeDirection,
                coords, &poincareSegment);


//        cout << "Segmento de poincare: " << nativePoincarePoint1 << " " << nativePoincarePoint2 << endl;

    } else {

        Viscous_Profile::orbit(fluxFunction, accumFunction,
                &v,
                boundary,
                nativeEquiPoint, nativeRefPoint, sigma,
                deltaxi,
                timeDirection,
                coords);

        cout << "Sem poincare" << endl;


    }


    cout << "Tamanho da orbita: " << coords.size() << endl;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);


    //Orbit memebers creation
    for (int i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;

        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject realVectorCoords = env->NewObject(realVectorClass, realVectorConstructorDoubleArrayID, jTempArray);

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




