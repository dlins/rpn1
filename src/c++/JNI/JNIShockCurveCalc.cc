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




#include "ColorCurve.h"
#include <stdio.h>


#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>
#include "Shock.h"
#include "Rarefaction.h"
#include "ShockContinuationMethod3D2D.h"


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
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
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

    //    cout << "Valor de increase" << increase << endl;

    //    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();
    //
    //    RealVector min(physicsBoundary. minimums());
    //    RealVector max(physicsBoundary. maximums());

    //    cout << "Valor de family" << familyIndex << endl;


    RealVector * originalDirection = new RealVector(realVectorInput.size());

    originalDirection->component(0) = 0;
    originalDirection->component(1) = 0;


    if (increase == RAREFACTION_SPEED_INCREASE)
        increase = WAVE_FORWARD;

    if (increase == RAREFACTION_SPEED_DECREASE)
        increase = WAVE_BACKWARD;



    FluxFunction * fluxFunction = (FluxFunction *) & RpNumerics::getPhysics().getSubPhysics(0).fluxFunction();
    AccumulationFunction * accumulationFunction = (AccumulationFunction *) & RpNumerics::getPhysics().getSubPhysics(0).accumulation();

    Boundary * tempBoundary = (Boundary *) RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();

    int info_shock_curve, info_shock_curve_alt;

    double tol = 1e-5;

    int dim = realVectorInput.size();
    int t = 11;


    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);

    ShockContinuationMethod3D2D shock;


    shock.curveCalc(realVectorInput, true, realVectorInput, increase, familyIndex, SHOCK_FOR_ITSELF,
            const RealVector *orig_direction, int number_ignore_doub_contact,
            fluxFunction, accumulationFunction, tempBoundary
            coords, info_shock_curve,
            std::vector<RealVector> shock_alt,
            info_shock_curve_alt, newtonTolerance);


    shock.curve(increase, coords); // If _SHOCK_INIT_IS_REF_


    cout << "Tolerancia: " << newtonTolerance << endl;

    //Orbit members creation

    if (coords.size() == 0) return NULL;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);


    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);


        RpNumerics::getPhysics().getSubPhysics(0).postProcess(tempVector);

        double lambda = tempVector.component(tempVector.size() - 1);

        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);

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
