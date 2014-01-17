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
#include "Debug.h"
#include <vector>

#include "TPCW.h"
#include "RarefactionCurve.h"
#include "LSODE.h"


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


    const Boundary * tempBoundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();

    //    double deltaxi = 1e-3; // This is the original value (Rodrigo/ Panters)


 

    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();


    vector<RealVector> inflectionPoints;

    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);

    RarefactionCurve rc(accum, flux, tempBoundary);


    double deltaxi = 1e-3;
    std::vector<RealVector> inflection_point;
    Curve rarcurve;

    int rar_stopped_because;
    int edge;
    RealVector final_direction;

    LSODE lsode;
    ODE_Solver *odesolver;

    odesolver = &lsode;

    int info_rar = rc.curve(realVectorInput,
            familyIndex,
            timeDirection,
            RAREFACTION_FOR_ITSELF,
            RAREFACTION_INITIALIZE,
            0,
            odesolver,
            deltaxi,
            rarcurve,
            inflection_point,
            final_direction,
            rar_stopped_because,
            edge);

  


   


    

    RpNumerics::getPhysics().getSubPhysics(0).postProcess(coords);
    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(rarcurve.curve.size(), classOrbitPoint, NULL);

    for (i = 0; i < rarcurve.curve.size(); i++) {

        RealVector tempVector = rarcurve.curve.at(i);

        double lambda = rarcurve.speed[i];


        if (Debug::get_debug_level() == 5) {
            cout << tempVector << endl;
        }

        double * dataCoords = tempVector;

        //Reading only coodinates
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        //Lambda is the last component.
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, lambda);

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
