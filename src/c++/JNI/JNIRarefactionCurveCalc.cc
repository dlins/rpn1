/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRarefactionCurveCalc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpnumerics_RarefactionCurveCalc.h"

#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>

#include "TPCW.h"
#include "Rarefaction.h"


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONCURVE_LOCATION);

    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;II)V");
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
    //

    vector <RealVector> coords;


    Boundary * tempBoundary = RpNumerics::getPhysics().boundary().clone();

    //    double deltaxi = 1e-3; // This is the original value (Rodrigo/ Panters)


    double deltaxi = 1e-3;

    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();
    
    
//    
//    cout << "Flux params " << fluxFunction->fluxParams().params()<<endl;
//    cout << "Accum params " << accumulationFunction->accumulationParams().params() << endl;




    vector<RealVector> inflectionPoints;


    int info = Rarefaction::curve(realVectorInput,
            RAREFACTION_INITIALIZE_YES,
            (const RealVector *) 0,
            familyIndex,
            timeDirection,
            RAREFACTION_FOR_ITSELF,
            deltaxi,
            fluxFunction,
            accumulationFunction,
            RAREFACTION_GENERAL_ACCUMULATION,
            tempBoundary,
            coords, inflectionPoints);
    delete tempBoundary;

    if (coords.size() == 0) {
        return NULL;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;

        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size() - 1);

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size() - 1, dataCoords);

        //Lambda is the last component.
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, tempVector.component(tempVector.size() - 1));

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //Building the orbit

    jobject rarefactionOrbit = (env)->NewObject(classRarefactionOrbit, rarefactionOrbitConstructor, orbitPointArray, familyIndex, timeDirection);


    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classRarefactionOrbit);

    return rarefactionOrbit;

}
