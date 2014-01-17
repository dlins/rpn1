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
#include "ShockCurve.h"
#include "RarefactionCurve.h"
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

//    vector <RealVector> coords, shock_alt;

    //    RealVector * originalDirection = new RealVector(realVectorInput.size());
    //
    //    originalDirection->component(0) = 0;
    //    originalDirection->component(1) = 0;


    if (increase == 20)
        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

    if (increase == 22)
        increase = RAREFACTION_SPEED_SHOULD_DECREASE;

    FluxFunction * fluxFunction = (FluxFunction *) & RpNumerics::getPhysics().getSubPhysics(0).fluxFunction();
    AccumulationFunction * accumulationFunction = (AccumulationFunction *) & RpNumerics::getPhysics().getSubPhysics(0).accumulation();

    Boundary * tempBoundary = (Boundary *) RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();


    HugoniotContinuation2D2D hc(fluxFunction, accumulationFunction, tempBoundary);

    ShockCurve shock(&hc);


    ReferencePoint ref(realVectorInput, fluxFunction, accumulationFunction, 0);

    int n = realVectorInput.size();
    RealVector r(n);
    for (int i = 0; i < n; i++) r(i) = ref.e[familyIndex].vrr[i];



    Curve shkcurve;

    std::vector<int> stop_current_index;
    std::vector<int> stop_current_family;
    std::vector<int> stop_reference_index;
    std::vector<int> stop_reference_family;

    int shock_stopped_because;


    int edge;
    
    RealVector candidate = realVectorInput + 1e-3 * r;

    JetMatrix F_j(n), G_j(n);
    fluxFunction->jet(candidate, F_j, 0);
    accumulationFunction->jet(candidate, G_j, 0);

    double sigma = hc.sigma(F_j.function(), G_j.function(), ref.F, ref.G);
    RealVector direction;

    double lambda = ref.e[familyIndex].r;
    
    if ((sigma >= lambda && (increase == RAREFACTION_SPEED_SHOULD_INCREASE)) ||
            (sigma < lambda && (increase == RAREFACTION_SPEED_SHOULD_DECREASE))
            ) direction = r;
    else direction = -r;



    int shck_info = shock.curve_engine(ref, realVectorInput, direction, familyIndex,
            SHOCKCURVE_SHOCK_CURVE,
            DONT_CHECK_EQUALITY_AT_LEFT,
            SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_RIGHT,
            USE_CURRENT_FAMILY /*int what_family_to_use*/,
            STOP_AFTER_TRANSITION /*int after_transition*/,
            shkcurve,
            stop_current_index,
            stop_current_family,
            stop_reference_index,
            stop_reference_family,
            shock_stopped_because,
            edge);



//
//    int info_shock_curve, info_shock_curve_alt;
//
//    int dim = realVectorInput.size();
//
//    RealVector original_direction(dim);
//
//    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);
//
//    ShockMethod * shock = RpNumerics::getPhysics().getSubPhysics(0).getShockMethod();

    //    shock->curveCalc(realVectorInput, true, realVectorInput, increase, familyIndex, SHOCK_FOR_ITSELF,
    //            &original_direction, 0,
    //            fluxFunction, accumulationFunction, tempBoundary,
    //            coords, info_shock_curve,
    //            shock_alt,
    //            info_shock_curve_alt, newtonTolerance);

    //Orbit members creation

    if (shkcurve.curve.size() == 0) return NULL;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(shkcurve.curve.size(), classOrbitPoint, NULL);


    for (i = 0; i < shkcurve.curve.size(); i++) {

        RealVector tempVector = shkcurve.curve.at(i);

        RpNumerics::getPhysics().getSubPhysics(0).postProcess(tempVector);

        double lambda = shkcurve.speed[i];

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



    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(shockCurveClass);

    return rarefactionOrbit;



}
