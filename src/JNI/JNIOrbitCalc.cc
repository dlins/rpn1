/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIOrbitCalc.cc
 **/


//! Definition of JNIOrbitCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
*/


#include "rpnumerics_OrbitCalc.h"
#include "JNIDefs.h"
#include <vector>

using std::vector;

JNIEXPORT jobject JNICALL Java_rpnumerics_OrbitCalc_calc  (JNIEnv * env, jobject obj , jobject initialPoint, jint timeDirection){
    
    int i=0, orbitPointSize=2;
    
    jclass    classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass    classOrbit = (env)->FindClass(ORBIT_LOCATION);
    
    jmethodID    orbitPointConstructor_ = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    jmethodID    orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
    
    //Input processing
    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    double input [env->GetArrayLength(phasePointArray)];
    
    env->GetDoubleArrayRegion(phasePointArray, 0, env->GetArrayLength(phasePointArray), input);
    
    env->DeleteLocalRef(phasePointArray);
    //Calculations
    
    vector <double *> resultList;
    double ix=-0.5;
    
    while (ix < 0.5){
        double * coord = new double [2];
        
        coord[0]=input[0]+ix;
        coord[1]=input[1]+ix;
        resultList.push_back(coord);
        ix+=0.005;
    }
    
    //Orbit memebers creation
    
    jobjectArray  orbitPointArray  =(jobjectArray) (env)->NewObjectArray(resultList.size(), classOrbitPoint, NULL);
    
    for(i=0;i < resultList.size();i++ ){
        
        double * dataCoords = (double *)resultList[i];
        
        jdoubleArray jTempArray = (env)->NewDoubleArray(2);
        
        (env)->SetDoubleArrayRegion(jTempArray, 0, 2, dataCoords);
        
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor_, jTempArray,timeDirection);
        
        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        env->DeleteLocalRef(jTempArray);
        env->DeleteLocalRef(orbitPoint);
        
        delete [] dataCoords;
    }
    
   //Building the orbit

    
    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor_, orbitPointArray, timeDirection);

    //Cleaning up
    
    resultList.clear();
    
    env->DeleteLocalRef(orbitPointArray);
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classOrbit);
    
    
    return orbit;
    
}

//----------------------------------------------------------------Stub------------------------------------------------------------------
//    TODO Obter os pontos do ODESolution retornado pelo ODESolver

//    int orbitPointArraySize =  solution->getCoords().size(); // TODO Substituir pelo tamanho do array de pontos retornado pelo ODESolution
//
//    jobjectArray  orbitPointArray  = (env)->NewObjectArray(orbitPointArraySize, classOrbitPoint, NULL);
//
//
//    for(i=0;i < solution->getCoords().size();i++ ){
//
//        jdoubleArray jTempArray = (env)->NewDoubleArray(profile.getDimension());
//
//        RealVector vectorCoords = solution->getCoords().at(i);
//
//        double bufferArray [profile.getDimension()] ;
//
//        for (j=0; j <profile.getDimension();j++ ){
//
//            bufferArray[j]=vectorCoords.component(j);
//        }
//
//        (env)->SetDoubleArrayRegion(jTempArray, 0, profile.getDimension(), bufferArray);
//
//        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jTempArray);
//
//        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
//
//        (env)->DeleteLocalRef(orbitPoint);
//        (env)->DeleteLocalRef(jTempArray);
//
////        cout << solution->getCoords().at(i) << endl;
//
//    }
//
//    //Building the orbit
//
//    jclass classOrbit = (env)->FindClass("rpnumerics/Orbit");
//
//    jmethodID orbitConstructor = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
//
//    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor, orbitPointArray, timeDirection);
//
//    return orbit;
//---------------------------------------------------------------------------------------------------------------------------------------

//------------------------------- Stub ---------------------------------
//
//    double arrayStub [2];
//
//    arrayStub[0]= 0.10;
//    arrayStub[1]= 0.10;
//
//    jdoubleArray jarrayStub = (env)->NewDoubleArray(2);
//
//    (env)->SetDoubleArrayRegion(jarrayStub, 0, 2, arrayStub);
//
//
//    jobjectArray  orbitPointArray  = (env)->NewObjectArray(1, classOrbitPoint, NULL);
//
//    jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
//
//    jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor, jarrayStub);
//
//    (env)->SetObjectArrayElement(orbitPointArray, 0, orbitPoint);
//
//    //Construindo a Orbita
//
//    jclass classOrbit = (env)->FindClass("rpnumerics/Orbit");
//
//    jmethodID orbitConstructor = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
//
//    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor, orbitPointArray, 0);
//
//    return orbit;
//

//---------------------------------------------------------------------------------



