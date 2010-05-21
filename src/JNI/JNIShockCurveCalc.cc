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

    //    /*
    //     *
    //     *1 - Pegar a instancia do rarefaction flow (plugin)
    //     *2 - Passar a instancia do flow para o construtor do metodo de calculo
    //     *3- O metodo cria o ODE solver , usando o flow . O profile do solver contem os parametros necessarios para o criterio de para especifico do metodo
    //     *4 -  o metodo curve da classe do metodo de calculo calcula a curva
    //     *
    //         RarefactionFlow *rarefactionFlow = RarefactionFlowFactory::createRarefactionFlow(flow, familyIndex, timeDirection, RpNumerics::getFlux());
    //
    //         RarefactionMethod * rarefactionMethod=  RarefactionMethodFactory::createRarefactionMethod(method, *rarefactionFlow);
    //
    //         env->ReleaseStringUTFChars(methodName, method);
    //
    //         env->ReleaseStringUTFChars(flowName, flow);
    //
    //         //Calculations
    //     *
    //     *
    //     */
    //    //    Physics & physics = RpNumerics::getPhysics();
    //    //
    //    //
    //
    //
    int dimension = 2;
    //    //
    int itol = 2;
    //    //
    double rtol = 1e-4;
    //    //
    int mf = 22;
    //    //
    double deltaxi = 0.001;
    //    //
    int nparam = 1 + dimension;
    //    //
    //
    double param[nparam];
    //
    //    //
    int ii;
    //
    for (ii = 0; ii < dimension; ii++) param[1 + ii] = 0.1;
    //    //
    int maxStepsNumber = 10000;
    //
    ContinuationShockFlow flow(realVectorInput, familyIndex, timeDirection, RpNumerics::getPhysics().fluxFunction());

    LSODEProfile lsodeProfile(flow, RpNumerics::getPhysics().boundary(), maxStepsNumber, dimension, itol, rtol, mf, deltaxi, nparam, param);

    LSODE odeSolver(lsodeProfile);

    vector <RealVector> coords;

    ShockContinuationMethod method(odeSolver);

    method.curve(realVectorInput, timeDirection, coords);

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
