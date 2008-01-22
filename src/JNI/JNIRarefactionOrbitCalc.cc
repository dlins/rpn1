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
#include "RarefactionMethodFactory.h"
#include "RarefactionFlowFactory.h"
#include "RpNumerics.h"
#include "RealVector.h"
#include "JNIDefs.h"
#include <vector>

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionOrbitCalc_calc  (JNIEnv * env, jobject obj, jstring methodName, jstring flowName, jobject initialPoint, jint timeDirection ){
    
    unsigned int i;
    
    jclass    classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass    classRarefactionOrbit = (env)->FindClass(RAREFACTIONORBIT_LOCATION);
    
    jmethodID rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
    //Input processing
    jdoubleArray inputPhasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    double input [env->GetArrayLength(inputPhasePointArray)];
    
    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);
    
    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));
    
    for (i=0;i< (unsigned int)realVectorInput.size();i++){
        
        realVectorInput.component(i)=input[i];
        
    }
    
    env->DeleteLocalRef(inputPhasePointArray);
    
    //Getting the method
    
    const char * method= env->GetStringUTFChars(methodName, NULL);
    
    if (method == NULL) {
        cerr <<"Error in method string name ! "<<endl;
    }
    
    
    cout <<"Method name: "<<method<<endl;
    //Getting the flow
    
    const char * flow= env->GetStringUTFChars(flowName, NULL);
    
    if (flow == NULL) {
        cerr <<"Error in flow string name ! "<<endl;
    }
    
    cout <<"Flow name: "<<flow<<endl;
    
    RarefactionFlow *  rarefactionFlow = RarefactionFlowFactory::getRarefactionFlow(flow, timeDirection);
    
    RarefactionMethod * rarefactionMethod=  RarefactionMethodFactory::getRarefactionMethod(method, *rarefactionFlow);
    
    env->ReleaseStringUTFChars(methodName, method);
    
    env->ReleaseStringUTFChars(flowName, flow);
    
    //Calculations
    
    vector <RealVector> coords = rarefactionMethod->curve(realVectorInput,rarefactionFlow->getFamilyIndex());
    
    delete rarefactionFlow;
    delete rarefactionMethod;
    
    //Orbit memebers creation
    
    jobjectArray  orbitPointArray  =(jobjectArray) (env)->NewObjectArray(coords.size(), classOrbitPoint, NULL);
    
    for(i=0;i < coords.size();i++ ){
        
        RealVector tempVector = coords.at(i);
        
        double * dataCoords = new double [tempVector.size()];
        
        unsigned int j;
        
        for (j=0;j < (unsigned int) tempVector.size();j++){
            dataCoords[j]=tempVector.component(j);
            
        }
        
        jdoubleArray jTempArray = (env)->NewDoubleArray(tempVector.size());
        
        (env)->SetDoubleArrayRegion(jTempArray, 0, tempVector.size(), dataCoords);
        
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray, timeDirection);
        
        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        env->DeleteLocalRef(jTempArray);
        
        env->DeleteLocalRef(orbitPoint);
        
        delete  dataCoords;
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






//! Code comes here! daniel@impa.br

