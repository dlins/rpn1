/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIShockCurveCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_ShockCurveCalc.h"
#include "ShockContinuationMethod.h"
#include "LSODESolver.h"
#include "LSODEProfile.h"

#include "ContinuationShockFlow.h"
#include "ShockContinuationMethod3D2D.h"


#include "ColorCurve.h"
#include <stdio.h>


#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>
#include "Shock.h"
#include "Rarefaction.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jdouble newtonTolerance, jobject initialPoint, jint familyIndex, jint increase) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);

    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);

    //    dimension;
    //

    vector <RealVector> coords, shock_alt;

    cout << "Valor de increase" << increase << endl;

    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

    RealVector min(physicsBoundary. minimums());
    RealVector max(physicsBoundary. maximums());

    cout << "Valor de family" << familyIndex << endl;


    RealVector * originalDirection = new RealVector(realVectorInput.size());

    originalDirection->component(0) = 0;
    originalDirection->component(1) = 0;


    if (increase == RAREFACTION_SPEED_INCREASE)
        increase = WAVE_FORWARD;

    if (increase == RAREFACTION_SPEED_DECREASE)
        increase = WAVE_BACKWARD;

    RealVector initPoint = realVectorInput;

    //    initPoint.component(0) = 0.5678;
    //    initPoint.component(1) = 0.4121;


    FluxFunction * fluxFunction = (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone();
    AccumulationFunction * accumulationFunction = (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone();

    Boundary * tempBoundary = RpNumerics::getPhysics().boundary().clone();

    Shock::curve(realVectorInput, true, initPoint, increase, familyIndex, SHOCK_FOR_ITSELF, originalDirection,

            fluxFunction, accumulationFunction,
            tempBoundary, coords, shock_alt);

    //    for (int i = 0; i < shock_alt.size(); i++) {
    //
    //        cout<<"shock alt"<<shock_alt[i]<<endl;
    //        coords.push_back(shock_alt[i]);
    //
    //
    //    }


    delete tempBoundary;
    delete fluxFunction;
    delete accumulationFunction;


    //Orbit members creation

    cout << "Tamanho do shock: " << coords.size() << endl;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        RealVector newVector(tempVector.size() + 1);

        newVector(0) = tempVector(0);
        newVector(1) = tempVector(1);
        newVector(2) = 0;

        double * dataCoords = newVector;




        jdoubleArray jTempArray = (env)->NewDoubleArray(newVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, newVector.size(), dataCoords);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, increase);


    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(shockCurveClass);

    return rarefactionOrbit;



}
