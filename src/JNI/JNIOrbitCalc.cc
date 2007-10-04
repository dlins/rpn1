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
    
//    JNIUtil *utilInstance = new JNIUtil(env);
    
//    JNICurve * curveInstance = new JNICurve(envPointer);
    
    
//    double * orbitPointArrayBuffer = utilInstance->orbitPointToDouble(initialPoint, orbitPointSize);
    
//    cout <<"Tamanho do array: " <<orbitPointSize<<endl;   jmethodID orbitPointConstructor = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    
//    env->ExceptionDescribe();
    
    jmethodID toDoubleMethodID = (env)->GetMethodID(classOrbitPoint, "toDouble", "()[D");
    
    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(initialPoint, toDoubleMethodID);
    
    double input [2];
    
    env->GetDoubleArrayRegion(phasePointArray,0,2,input);
    
    
//
//    orbitPointSize = (env)->GetArrayLength(phasePointArray);
//
//    double phasePointArrayBuffer[orbitPointSize];
    
//    RealVector nativeRealVector(orbitPointSize);
//
//    for (i =0 ; i < orbitPointSize;i++){
//        nativeRealVector(i)=orbitPointArrayBuffer[i];
//    }
    
    //TODO Chamando o ODESolver com nativeRealVector e o timeDirection
    
//    ShockRarefaction function ;
//
//    ODESolverProfile  profile(2, 0.01, function);
//
//    RK4BPMethod odeSolver(profile);
//
//    ODESolution * solution = odeSolver.solve(nativeRealVector, timeDirection);
    
    
//    vector<double *> resultList;
    
    deque<double *> resultList;
    
    while ( i < 1000){ //Criterio de parada
        
//        rk4method_teste(profile_.getDimension(), 0, profile_.getDeltat(), in, in, profile_.getFunction());
        
        
//        double * coord = new double [2];
//        double coord[2];
        
//        coord[0]=orbitPointArrayBuffer[0]+0.00001*i;
//        coord[1]=orbitPointArrayBuffer[1]+0.00001*i;
        
//        coord[0]=0.001*i;
//        coord[1]=0.001*i;
        
        input[0]=0.001*i;
        input[1]=0.001*i;
        

        
//        resultList
        resultList.push_back(input);
        
//        ret->addCoords(in);
        
//        ret->addCoords(teste);
        
        cout << "Saida x: " << input[0] << endl;
//
        cout << "Saida y: " << input[1] << endl;
        
        i++;
        
    }
    
// Construindo a orbita
//
//    jclass    classOrbitPoint_ = (env)->FindClass(ORBITPOINT_LOCATION);
//    jclass    classOrbit_ = (env)->FindClass(ORBIT_LOCATION);
    
    jmethodID    orbitPointConstructor_ = (env)->GetMethodID(classOrbitPoint, "<init>", "([D)V");
    
    jmethodID    orbitConstructor_ = (env)->GetMethodID(classOrbit, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    
    jobjectArray  orbitPointArray  = (env)->NewObjectArray(resultList.size(), classOrbitPoint, NULL);
    
    for(i=0;i < resultList.size();i++ ){
        
        double * dataCoords = (double *)resultList[i];//.at(i);
        
        jdoubleArray jTempArray = (env)->NewDoubleArray(2);
        
        (env)->SetDoubleArrayRegion(jTempArray, 0, 2, dataCoords);
        
        jobject orbitPoint = (env)->NewObject(classOrbitPoint, orbitPointConstructor_, jTempArray);
        
        (env)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
//        (env)->DeleteLocalRef(orbitPoint);
//        (env)->DeleteLocalRef(jTempArray);
        
        
    }
    
//Building the orbit
    resultList.clear();
    
    jobject orbit = (env)->NewObject(classOrbit, orbitConstructor_, orbitPointArray, timeDirection);
    
//    env->DeleteLocalRef(orbitPointArray);
    
    env->DeleteLocalRef(classOrbitPoint);
    env->DeleteLocalRef(classOrbit);
    
    
    return orbit;
    
}


//    jobject result =  curveInstance->orbitConstructor(solution->getCoords(), timeDirection);

//    jobject result =  curveInstance->orbitConstructorTeste(resultList, timeDirection);

//    delete [] orbitPointArrayBuffer;

//    resultList.clear();

//    return result;





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



