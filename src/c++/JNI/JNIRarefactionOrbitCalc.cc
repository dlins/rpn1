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
#include "rpnumerics_RarefactionOrbitCalc.h"
#include "RarefactionContinuationMethod.h"
#include "LSODESolver.h"
#include "LSODEProfile.h"

#include "ContinuationRarefactionFlow.h"

#include "PluginService.h"
#include "RPnPluginManager.h"
#include "RarefactionFlowPlugin.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>
#include <time.h>
#include "TPCW.h"
#include "Rarefaction.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionOrbitCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONORBIT_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
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

    vector <RealVector> coords;

    //    clock_t begin = clock();



    const Boundary & physicsBoundary = RpNumerics::getPhysics().boundary();

    RealVector min(physicsBoundary.minimums());
    RealVector max(physicsBoundary.maximums());



    SubPhysics & physics = RpNumerics::getPhysics().getSubPhysics(0);


    physics.preProcess(min);
    physics.preProcess(max);


    vector<bool> testBoundary;

    testBoundary.push_back(true);
    testBoundary.push_back(true);
    testBoundary.push_back(false);



    RectBoundary tempBoundary(min, max, testBoundary);

    double deltaxi = 1e-3;

    physics.preProcess(realVectorInput);


    cout << "Time direction :" << timeDirection << endl;
    int info = Rarefaction::curve(realVectorInput,
            RAREFACTION_INITIALIZE_YES,
            (const RealVector *) 0,
            familyIndex,
            timeDirection,
            deltaxi,
            (FluxFunction *) RpNumerics::getPhysics().fluxFunction().clone(),
            (AccumulationFunction*) RpNumerics::getPhysics().accumulation().clone(),
            RAREFACTION_GENERAL_ACCUMULATION,
            &tempBoundary,
            coords);


    physics.postProcess(coords);
    for (int i = 0; i < coords.size(); i++) cout << "coords(" << i << ") = " << coords[i] << endl;

    cout << "Resultado da rarefacao: " << info << ", size = " << coords.size() << endl;
    //    method.curve(realVectorInput, timeDirection, coords);

    //cout << "Depois de chamar curve: " << (double) (clock() - begin) / (double) CLOCKS_PER_SEC << endl;

    if (coords.size() == 0) {
        return NULL;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit memebers creation

    cout << "Tamanho da curva: " << coords.size() << endl;

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        //        RealVector tempVector (2);
        //
        //        tempVector.component(0)=tempVectorT(0);
        //        tempVector.component(1) = tempVectorT(1);

                cout<<tempVector<<endl;

        //        cout << "Tamanho de tempVector: "<<tempVector.size()<<endl;

        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, timeDirection);


    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;

}
