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
#include "CompositeContinuationMethod.h"
#include "LSODE.h"
#include "LSODEProfile.h"
#include "ContinuationShockFlow.h"
#include "ContinuationRarefactionFlow.h"

#include "CompositeFlow.h"

#include "PluginService.h"
#include "RPnPluginManager.h"
#include "RarefactionFlowPlugin.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>


using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_CompositeCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {


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
    /*
     *
     *1 - Pegar a instancia do rarefaction flow (plugin) 
     *2 - Passar a instancia do flow para o construtor do metodo de calculo
     *3- O metodo cria o ODE solver , usando o flow . O profile do solver contem os parametros necessarios para o criterio de para especifico do metodo
     *4 -  o metodo curve da classe do metodo de calculo calcula a curva 
     *
         RarefactionFlow *rarefactionFlow = RarefactionFlowFactory::createRarefactionFlow(flow, familyIndex, timeDirection, RpNumerics::getFlux());
    
         RarefactionMethod * rarefactionMethod=  RarefactionMethodFactory::createRarefactionMethod(method, *rarefactionFlow);
    
         env->ReleaseStringUTFChars(methodName, method);
    
         env->ReleaseStringUTFChars(flowName, flow);
    
         //Calculations
     *
     *
     */
    //    Physics & physics = RpNumerics::getPhysics();
    //
    //


    int dimension = 2;
    //
    int itol = 2;
    //
    double rtol = 1e-4;
    //
    int mf = 22;
    //
    double deltaxi = 0.001;
    //
    int nparam = 1 + dimension;
    //

    double param[nparam];

    //
    int ii;

    for (ii = 0; ii < dimension; ii++) param[1 + ii] = 0.1;
    //
    int maxStepsNumber = 10000;
    //
    //
    ContinuationShockFlow shockFlow(realVectorInput, familyIndex, timeDirection, RpNumerics::getPhysics().fluxFunction());

    ContinuationRarefactionFlow rarefactionFlow(familyIndex, timeDirection, RpNumerics::getPhysics().fluxFunction());


    CompositeFlow flow(familyIndex, rarefactionFlow, shockFlow, RpNumerics::getPhysics().fluxFunction());

    LSODEProfile lsodeProfile(flow, RpNumerics::getPhysics().boundary(), maxStepsNumber, dimension, itol, rtol, mf, deltaxi, nparam, param);

    LSODE odeSolver(lsodeProfile);

    vector <RealVector> coords;

    CompositeContinuationMethod method(odeSolver);

    method.curve(realVectorInput, timeDirection, coords);
    

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit memebers creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {



        //TODO Plotando apenas as duas primeiras coordenadas

        RealVector totalCoords = coords.at(i);
        RealVector tempVector(2);

        tempVector(0) = totalCoords(0);
        tempVector(1) = totalCoords(1);
        

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
