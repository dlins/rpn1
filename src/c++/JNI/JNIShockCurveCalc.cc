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

#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_ShockCurveCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {

    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classShockCurve = (env)->FindClass(SHOCKCURVE_LOCATION);

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);

    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");

    jmethodID shockCurveConstructor = (env)->GetMethodID(classShockCurve, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "(Lwave/util/RealVector;D)V");
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

    int dimension = realVectorInput.size();
    //    //
    //    int itol = 2;
    //    //    //
    //    double rtol = 1e-4;
    //    //    //
    //    int mf = 22;
    //    //    //
    //    double deltaxi = 0.001;
    //    //    //
    //    int nparam = 1 + dimension;
    //    //    //
    //    //
    //    double param[nparam];
    //    //
    //    //    //
    //    int ii;
    //    //
    //    for (ii = 0; ii < dimension; ii++) param[1 + ii] = 0.1;
    //    //    //
    int maxStepsNumber = 10000;
    //
    //    ContinuationShockFlow flow(realVectorInput, familyIndex, timeDirection, RpNumerics::getPhysics().fluxFunction());

    //TODO Teste para o novo metodo de choque

    //    LSODEProfile lsodeProfile(flow,maxStepsNumber, dimension, itol, rtol, mf, deltaxi, nparam, param);
    //
    //    LSODE odeSolver(lsodeProfile);

    vector <RealVector> coords;

    //    ShockContinuationMethod method(odeSolver,RpNumerics::getPhysics().boundary(),familyIndex);

    //    double Ur [dimension];
    double * Ur;

    Ur = realVectorInput;
    // for (int i = 0; i < dimension; i++) Ur[i] = realVectorInput.component(i);

    //    Ur[0]=0.47;
    //    Ur[1] = 0.182590;
    //    Ur[2] = 1.0;

    double tol = 10e-10;
    double epsilon = 10e-3;
    int t = 11;

    //    FluxFunction * f = (FluxFunction *)RpNumerics::getPhysics().fluxFunction().clone();
    //
    //    AccumulationFunction * a = (AccumulationFunction *) RpNumerics::getPhysics().accumulation().clone();
    //    Boundary * b = (Boundary *) RpNumerics::getPhysics().boundary().clone();

    ShockContinuationMethod3D2D method(dimension, familyIndex, RpNumerics::getPhysics().fluxFunction(), RpNumerics::getPhysics().accumulation(), RpNumerics::getPhysics().boundary(), Ur, tol, epsilon, t);


    // ShockContinuationMethod3D2D method(dimension, RpNumerics::getPhysics().boundary(), familyIndex);

    int edge;
    //    method.curve(familyIndex, maxStepsNumber, timeDirection, coords, edge);
    method.curve(realVectorInput, timeDirection, coords);

    //    delete f;
    //    delete a;
    //    delete b;


    if (coords.size() == 0) {
        return NULL;

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Orbit memebers creation


    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);

        double * dataCoords = tempVector;

        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());

        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);

        double shockSpeed = 0; // dataCoords[tempVector.size() - 1];

        jobject realVector = env->NewObject(realVectorClass, realVectorConstructorDoubleArray, jTempArray);

        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, realVector, shockSpeed);

        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);

        env->DeleteLocalRef(jTempArray);

        env->DeleteLocalRef(orbitPoint);

    }

    //    //Building the orbit

    jobject shockCurve = (env)->NewObject(classShockCurve, shockCurveConstructor, orbitPointArray, timeDirection);

    //Cleaning up

    coords.clear();

    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classShockCurve);

    return shockCurve;

}
