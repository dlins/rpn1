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


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionOrbitCalc_calc(JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint familyIndex, jint timeDirection) {


    unsigned int i;

    jclass classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass classRarefactionOrbit = (env)->FindClass(RAREFACTIONORBIT_LOCATION);

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


   const  Boundary * tempBoundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();

    //    double deltaxi = 1e-3; // This is the original value (Rodrigo/ Panters)


    double deltaxi = 1e-3;

    const FluxFunction * fluxFunction = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accumulationFunction = &RpNumerics::getPhysics().accumulation();
    
    
//    
//    cout << "Flux params " << fluxFunction->fluxParams().params()<<endl;
//    cout << "Accum params " << accumulationFunction->accumulationParams().params() << endl;




    vector<RealVector> inflectionPoints;
    
    RpNumerics::getPhysics().getSubPhysics(0).preProcess(realVectorInput);


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
    
    cout <<"Tamanho da rarefacao: " << coords.size()<<endl;


    if (coords.size() == 0) {
        return NULL;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //Orbit members creation

    jobjectArray orbitPointArray = (jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);

    for (i = 0; i < coords.size(); i++) {

        RealVector tempVector = coords.at(i);
 
        double lambda = tempVector.component(tempVector.size()-1);
        
        
         RpNumerics::getPhysics().getSubPhysics(0).postProcess(tempVector);
         
         cout <<tempVector<<endl;

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
