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



using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jdouble newtonTolerance, jobject initialPoint, jint familyIndex, jint increase) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
    jclass classWaveCurveBranch = (env)->FindClass(WAVECURVEBRANCH_LOCATION);

    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D[DD)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    jmethodID setReferencePointID = (env)->GetMethodID(classWaveCurveBranch, "setReferencePoint", "(Lrpnumerics/OrbitPoint;)V");

    //Input processing
    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);

    double input [env->GetArrayLength(inputPhasePointArray)];

    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);

    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));


    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {

        realVectorInput.component(i) = input[i];

    }

    env->DeleteLocalRef(inputPhasePointArray);

    int dimension = realVectorInput.size();

   const  Boundary * boundary =   RpNumerics::physicsVector_->at(0)->boundary();



    if (increase == 20)
        increase = RAREFACTION_SPEED_SHOULD_INCREASE;

    if (increase == 22)
        increase = RAREFACTION_SPEED_SHOULD_DECREASE;


    const FluxFunction * flux = RpNumerics::physicsVector_->at(0)->flux();
    const AccumulationFunction * accum = RpNumerics::physicsVector_->at(0)->accumulation();

    HugoniotContinuation * hc = RpNumerics::physicsVector_->at(0)->Hugoniot_continuation();


    ShockCurve * shock = RpNumerics::physicsVector_->at(0)->shock();


    ReferencePoint ref(realVectorInput, flux, accum, 0);

    int n = realVectorInput.size();


    RealVector r(n);
    for (int i = 0; i < n; i++) r(i) = ref.e[familyIndex].vrr[i];


    
    
//    cout<<"familia em C++ : "<<familyIndex<<endl;
    

    Curve shkcurve;

    std::vector<int> stop_current_index;
    std::vector<int> stop_current_family;
    std::vector<int> stop_reference_index;
    std::vector<int> stop_reference_family;

    int shock_stopped_because;


    int edge;

    RealVector candidate = realVectorInput + 1e-3 * r;

    JetMatrix F_j(n), G_j(n);
    flux->jet(candidate, F_j, 0);
    accum->jet(candidate, G_j, 0);

    double sigma = hc->sigma(F_j.function(), G_j.function(), ref.F, ref.G); //Velocidade do choque no ponto de referencia


    RealVector direction;

    double lambda = ref.e[familyIndex].r;

    if ((sigma >= lambda && (increase == RAREFACTION_SPEED_SHOULD_INCREASE)) ||
            (sigma < lambda && (increase == RAREFACTION_SPEED_SHOULD_DECREASE))
            ) direction = r;
    else direction = -r;



    int shck_info = shock->curve_engine(ref, realVectorInput, direction, familyIndex,
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

    double nativeEigenValues [2];

    

    jdoubleArray eigenValuesArray = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(eigenValuesArray, 0, dimension, nativeEigenValues);


    jdoubleArray refPointCoords = (env)->NewDoubleArray(dimension);

    (env)->SetDoubleArrayRegion(refPointCoords, 0, dimension, (double *) ref.point);

    jobject referenceOrbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, refPointCoords, eigenValuesArray, sigma);


    //Orbit members creation

    if (shkcurve.curve.size() == 0) return NULL;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(shkcurve.curve.size(), classOrbitPoint, NULL);


    for (i = 0; i < shkcurve.curve.size(); i++) {

        RealVector tempVector = shkcurve.curve.at(i);

       
        //        //cout<<tempVector<<endl;

        double lambda = shkcurve.speed[i];

//        double lambda = 0;

        //        shkcurve.eigenvalues // Autovalores em cada ponto

        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());
        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);


        jdoubleArray jeigenValuesArray = (env)->NewDoubleArray(2);
        RealVector eigenValue = shkcurve.eigenvalues[i];
        (env)->SetDoubleArrayRegion(jeigenValuesArray, 0, eigenValue.size(), (double *) eigenValue);


        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, jeigenValuesArray, lambda);


        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, increase);

    env->CallVoidMethod(rarefactionOrbit, setReferencePointID, referenceOrbitPoint);


    //Cleaning up



    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(shockCurveClass);

    return rarefactionOrbit;



}

/*
 * Class:     rpnumerics_ShockCurveCalc
 * Method:    boundaryCalc
 * Signature: (Lrpnumerics/PhasePoint;III)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_boundaryCalc
(JNIEnv * env, jobject obj, jobject initialPoint, jint familyIndex, jint increase, jint edge) {


//
//    unsigned int i;
//
//    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
//    jclass shockCurveClass = (env)->FindClass(SHOCKCURVE_LOCATION);
//
//    jmethodID shockCurveConstructor = (env)->GetMethodID(shockCurveClass, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
//    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([DD)V");
//    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
//
//    //Input processing
//    jdoubleArray inputPhasePointArray = (jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
//
//    double input [env->GetArrayLength(inputPhasePointArray)];
//
//    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);
//
//    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));
//
//
//    for (i = 0; i < (unsigned int) realVectorInput.size(); i++) {
//
//        realVectorInput.component(i) = input[i];
//
//    }
//
//    env->DeleteLocalRef(inputPhasePointArray);
//
//    //    dimension;
//    //
//
//    //    vector <RealVector> coords, shock_alt;
//
//    //    RealVector * originalDirection = new RealVector(realVectorInput.size());
//    //
//    //    originalDirection->component(0) = 0;
//    //    originalDirection->component(1) = 0;
//
//
//    if (increase == 20)
//        increase = RAREFACTION_SPEED_SHOULD_INCREASE;
//
//    if (increase == 22)
//        increase = RAREFACTION_SPEED_SHOULD_DECREASE;
//
//    FluxFunction * flux = (FluxFunction *) & RpNumerics::getPhysics().getSubPhysics(0).fluxFunction();
//    AccumulationFunction * accum = (AccumulationFunction *) & RpNumerics::getPhysics().getSubPhysics(0).accumulation();
//
//    Boundary * tempBoundary = (Boundary *) RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();
//
//
//    RarefactionCurve rc(accum, flux, tempBoundary);
//
//
//
//
//    HugoniotContinuation2D2D hc(flux, accum, tempBoundary);
//
//    ShockCurve shock(&hc);
//
//
//    ReferencePoint ref(realVectorInput, flux, accum, 0);
//
//    int n = realVectorInput.size();
//    RealVector r(n);
//    for (int i = 0; i < n; i++) r(i) = ref.e[familyIndex].vrr[i];
//
//
//
//    Curve shkcurve;
//
//    std::vector<int> stop_current_index;
//    std::vector<int> stop_current_family;
//    std::vector<int> stop_reference_index;
//    std::vector<int> stop_reference_family;
//
//    int shock_stopped_because;
//
//
//    int s;
//
//    RealVector candidate = realVectorInput + 1e-3 * r;
//
//    JetMatrix F_j(n), G_j(n);
//    flux->jet(candidate, F_j, 0);
//    accum->jet(candidate, G_j, 0);
//
//    double sigma = hc.sigma(F_j.function(), G_j.function(), ref.F, ref.G);
//    RealVector direction;
//
//    double speed = ref.e[familyIndex].r;
//
//    if ((sigma >= speed && (increase == RAREFACTION_SPEED_SHOULD_INCREASE)) ||
//            (sigma < speed && (increase == RAREFACTION_SPEED_SHOULD_DECREASE))
//            ) direction = r;
//    else direction = -r;
//
//
//
//    int shck_info = shock.curve_engine_from_boundary(&rc, edge, increase, ref, realVectorInput, familyIndex,
//            SHOCKCURVE_SHOCK_CURVE,
//            DONT_CHECK_EQUALITY_AT_LEFT,
//            SHOCK_SIGMA_EQUALS_LAMBDA_OF_FAMILY_AT_RIGHT,
//            USE_CURRENT_FAMILY /*int what_family_to_use*/,
//            STOP_AFTER_TRANSITION /*int after_transition*/,
//            shkcurve,
//            stop_current_index,
//            stop_current_family,
//            stop_reference_index,
//            stop_reference_family,
//            shock_stopped_because,
//            s);
//
//
//
//    //Orbit members creation
//
//    if (shkcurve.curve.size() == 0) return NULL;
//
//    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(shkcurve.curve.size(), classOrbitPoint, NULL);
//
//
//    for (i = 0; i < shkcurve.curve.size(); i++) {
//
//        RealVector tempVector = shkcurve.curve.at(i);
//
//        RpNumerics::getPhysics().getSubPhysics(0).postProcess(tempVector);
//
//        double speed = shkcurve.speed[i];
//
//        double * dataCoords = tempVector;
//
//        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());
//
//        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);
//
//        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, speed);
//
//        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
//
//        env->DeleteLocalRef(jTempArray);
//
//        env->DeleteLocalRef(orbitPoint);
//
//    }
//
//    //Building the orbit
//
//    jobject rarefactionOrbit = (env)->NewObject(shockCurveClass, shockCurveConstructor, orbitPointArray, familyIndex, increase);
//
//
//    //Cleaning up
//
//
//
//    env->DeleteLocalRef(orbitPointArray);
//    env->DeleteLocalRef(classOrbitPoint);
//    env->DeleteLocalRef(shockCurveClass);
//
//    return rarefactionOrbit;


}