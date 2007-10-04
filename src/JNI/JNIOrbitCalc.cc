#include "rpnumerics_OrbitCalc.h"
#include "RealVector.h"
#include "RK4BPMethod.h"
#include "ODESolution.h"
#include "JNICurve.h"
#include <deque>

JNIEXPORT jobject JNICALL Java_rpnumerics_OrbitCalc_calc  (JNIEnv * env, jobject obj , jobject initialPoint, jint timeDirection){
    
    int i=0, orbitPointSize=2;
    
    jclass    classOrbitPoint = (env)->FindClass(ORBITPOINT_LOCATION);
    jclass    classOrbit = (env)->FindClass(ORBIT_LOCATION);
    
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    double input [2];
    
    env->GetDoubleArrayRegion(phasePointArray, 0, 2, input);
    
//    cout << "Entrada: " << input[0]<< endl;
//    cout << "Entrada: " << input[1]<< endl;
//    
    vector <double *> resultList;
    double ix=-0.5;
    
//    for (ix=0; ix < 0.5;ix+=0.0001){
        while (ix < 0.5){
        double * coord = new double [2];
        
//        input[0]=1*ix;
//        input[1]=1*ix;
        
        coord[0]=input[0]+ix;
        coord[1]=input[1]+ix;
        
        resultList.push_back(coord);
        ix+=0.005;
        
//        cout << "Saida x: " << input[0] << endl;
//        
//        cout << "Saida y: " << input[1] << endl;
    }
    
// Construindo a orbita
    
    jmethodID    orbitPointConstructor_ = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    
    jmethodID    orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    
    jobjectArray  orbitPointArray  = (env)->NewObjectArray(resultList.size(), classOrbitPoint, NULL);
    
//    cout<< "Tamanho da lista:"<< resultList.size()<<endl;
    
    for(i=0;i < resultList.size();i++ ){
        
        double * dataCoords = (double *)resultList[i];//.at(i);
        
//        cout << "dataCoords[0]: "<< dataCoords[0]<<endl;
//        cout << "dataCoords[1]: "<< dataCoords[1]<<endl;
        
        jdoubleArray jTempArray = (env)->NewDoubleArray(2);
        
        (env)->SetDoubleArrayRegion(jTempArray, 0, 2, dataCoords);
        
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor_, jTempArray);
        
        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        delete [] dataCoords;
    }
    
//Building the orbit
    resultList.clear();
    
    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor_, orbitPointArray, timeDirection);
    
//    env->DeleteLocalRef(orbitPointArray);
    
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



