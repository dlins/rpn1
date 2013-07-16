/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRarefactionOrbitCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_CompositeCalc.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include "Stone.h"
#include "StoneAccumulation.h"
#include "RectBoundary.h"
#include <vector>
#include "Rarefaction.h"

#include "CompositeCurve.h"
#include "Shock.h"
#include "Debug.h"

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_nativeCalc(JNIEnv * env, jobject obj, jobject initialPoint, jint increase, jint familyIndex) {

    if ( Debug::get_debug_level() == 5 ) {
        cout << "chamando JNI composite calc" << endl;
    }

    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(COMPOSITECURVE_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
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

    // Storage space for the segments:

    std::vector<RealVector> rarefactionCurve;
    std::vector<RealVector> compositeCurve;

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Chamando com stone" << endl;
    }

    FluxFunction * stoneflux = (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone();

    AccumulationFunction * stoneaccum = (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone();

    Boundary * tempBoundary = RpNumerics::getPhysics().boundary().clone();

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Increase: " << increase << endl;
    }

    //        double deltaxi = 1e-3;
    double deltaxi = 1e-2;

    //Compute rarefaction

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Increase da rarefacao: " << increase << endl;
    }
    vector<RealVector> inflectionPoints;

    Rarefaction::curve(realVectorInput,
            RAREFACTION_INITIALIZE_YES,
            0,
            familyIndex,
            increase,
            RAREFACTION_FOR_ITSELF,
            deltaxi,
            stoneflux, stoneaccum,
            RAREFACTION_GENERAL_ACCUMULATION,
            tempBoundary,
            rarefactionCurve,inflectionPoints);


    if (increase == RAREFACTION_SPEED_INCREASE)

        increase = WAVE_FORWARD;

    if (increase == RAREFACTION_SPEED_DECREASE)

        increase = WAVE_BACKWARD;

    if ( Debug::get_debug_level() == 5 ) {
        cout << "Rarefaction curve" << rarefactionCurve.size() << endl;
    }

    CompositeCurve::curve(rarefactionCurve, COMPOSITE_FROM_NORMAL_RAREFACTION, familyIndex,
            increase, 0,stoneflux, stoneaccum, tempBoundary, compositeCurve);

    delete stoneflux;
    delete stoneaccum;
    delete tempBoundary;

    if (compositeCurve.size() == 0)
        return NULL;


    if ( Debug::get_debug_level() == 5 ) {
        cout << "Tamanho da curva: " << compositeCurve.size() << endl;
    }


    //Orbit members creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(compositeCurve.size(), classOrbitPoint, NULL);

    for (i = 0; i < compositeCurve.size(); i++) {

        RealVector tempVector = compositeCurve.at(i);

        RealVector newVector(3);

        newVector.component(0) = tempVector(0);
        newVector.component(1) = tempVector(1);
        newVector.component(2) = 0; //TODO Substituir pelo indice do ponto correspondente na rarefacao ??

        double * dataCoords = newVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(newVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, newVector.size(), dataCoords);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, increase, familyIndex);


    //Cleaning up

    compositeCurve.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;



}
