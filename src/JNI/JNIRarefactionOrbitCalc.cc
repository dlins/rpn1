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

using std::vector;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


JNIEXPORT jobject JNICALL Java_rpnumerics_RarefactionOrbitCalc_calc  (JNIEnv * env, jobject obj, jstring methodName, jobject initialPoint, jint timeDirection ){
    
    unsigned int i;
    
    jclass    classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass    classRarefactionOrbit = (env)->FindClass(RAREFACTIONORBIT_LOCATION);
    jclass    classRpNumerics = env->FindClass(RPNUMERICS_LOCATION);
    
    jmethodID    rarefactionOrbitConstructor = (env)->GetMethodID(classRarefactionOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
    jmethodID familyIndexMethod = env->GetStaticMethodID(classRpNumerics, "getFamilyIndex", "()I");
    
    
    
    //Input processing
    jdoubleArray inputPhasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    double input [env->GetArrayLength(inputPhasePointArray)];
    
    env->GetDoubleArrayRegion(inputPhasePointArray, 0, env->GetArrayLength(inputPhasePointArray), input);
    
    RealVector realVectorInput(env->GetArrayLength(inputPhasePointArray));
    
    for (i=0;i< (unsigned int)realVectorInput.size();i++){
        
        realVectorInput.component(i)=input[i];
        
    }
    
    env->DeleteLocalRef(inputPhasePointArray);
    
    //Choosing the calculations method
    const char * method = env->GetStringUTFChars(methodName, NULL);
    
    //Getting the family index
    
    RpNumerics::setFamilyIndex(env->CallStaticIntMethod(classRpNumerics, familyIndexMethod));
    
    if (method == NULL) {
        cout <<"Error in method name !"<<endl;
        /* OutOfMemoryError already thrown */
    }
    
    vector<RealVector>coords;
    
    if (!strcmp(method, "rarefaction")){ //TODO Choose a name for each method
        
        RarefactionMethod * rarefactionMethod=  RpNumerics::getRarefactionMethod();
        
        //Calculations
        coords = rarefactionMethod->curve(realVectorInput, timeDirection);
        
    }
    env->ReleaseStringUTFChars(methodName, method);
    
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

