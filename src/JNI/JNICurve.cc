#include "JNICurve.h"

JNICurve * JNICurve::instance_=NULL;

//JNICurve * JNICurve::instance(JNIEnv * env){
//    
////    if (instance_==NULL){
//        instance_= new JNICurve(env);
////        return instance_;
////    }
////    return instance_;
//}

JNICurve::~JNICurve(){}


JNICurve::JNICurve(JNIEnv * env):envPointer(env),utilInstance( new JNIUtil(env)){
    

    
//    utilInstance = JNIUtil(env);
    
    classOrbitPoint_ = (env)->FindClass(ORBITPOINT_LOCATION);
    
    classOrbit_ = (env)->FindClass(ORBIT_LOCATION);
    
    classManifold_= (env)->FindClass(MANIFOLD_LOCATION);
    
    classConnectionOrbit_=env->FindClass(CONNECTIONORBIT_LOCATION);
    
    classStationaryPoint_=env->FindClass(STATIONARYPOINT_LOCATION);
    
    orbitPointConstructor_ = (envPointer)->GetMethodID(classOrbitPoint_, "<init>", "([D)V");
    
    orbitConstructor_ = (envPointer)->GetMethodID(classOrbit_, "<init>", "([Lrpnumerics/OrbitPoint;I)V");
    
    manifoldConstructor_=(envPointer)->GetMethodID(classManifold_, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;Lrpnumerics/Orbit;I)V");
    
    connectionOrbitConstructor_=(envPointer)->GetMethodID(classConnectionOrbit_, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/StationaryPoint;Lrpnumerics/Orbit;)V");
    
    stationaryPointConstructor_=(envPointer)->GetMethodID(classStationaryPoint_, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;I)V");
    
    
}


jobject JNICurve::connectionOrbitConstructor(const jobject stationaryPointA, const jobject stationaryPointB, const jobject orbit) const {
    
    return envPointer->NewObject(classConnectionOrbit_, connectionOrbitConstructor_, stationaryPointA, stationaryPointB, orbit);
    
}

jobject JNICurve::manifoldConstructor(const jobject stationaryPoint, const jobject phasePoint , int timeDirection)const {
    
    return envPointer->NewObject(classManifold_, manifoldConstructor_, stationaryPoint, phasePoint, timeDirection);
    
}

jobject JNICurve::orbitConstructorTeste(const vector<double *> coords, const int timeDirection) const {
    
    int i;
    
    jobjectArray  orbitPointArray  = (jobjectArray)(envPointer)->NewObjectArray(coords.size(), classOrbitPoint_, NULL);
    
    
    for(i=0;i < coords.size();i++ ){
        
        double * dataCoords = (double *)coords[i];//.at(i);
        
        
        jdoubleArray jTempArray = (envPointer)->NewDoubleArray(2);
        
        (envPointer)->SetDoubleArrayRegion(jTempArray, 0, 2, dataCoords);
        
        jobject orbitPoint = (envPointer)->NewObject(classOrbitPoint_, orbitPointConstructor_, jTempArray);
        
        (envPointer)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        (envPointer)->DeleteLocalRef(orbitPoint);
        (envPointer)->DeleteLocalRef(jTempArray);
        
        
    }
    
    //Building the orbit
    
    
    jobject orbit = (envPointer)->NewObject(classOrbit_, orbitConstructor_, orbitPointArray, timeDirection);
    
    return orbit;
    
    
}


jobject JNICurve::orbitConstructorTesteArray( double ** coords, const int timeDirection) const {
    
    int i,size=100;// Tamanho 100 !!!
    
    
    jobjectArray  orbitPointArray  = (jobjectArray)(envPointer)->NewObjectArray(size, classOrbitPoint_, NULL);
    
    
    for(i=0;i < size;i++ ){ 
        
       double * dataCoords = coords[i];//.at(i);
        
        
        jdoubleArray jTempArray = (envPointer)->NewDoubleArray(2);
        
        (envPointer)->SetDoubleArrayRegion(jTempArray, 0, 2, dataCoords);
        
        jobject orbitPoint = (envPointer)->NewObject(classOrbitPoint_, orbitPointConstructor_, jTempArray);
        
        (envPointer)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        (envPointer)->DeleteLocalRef(orbitPoint);
        (envPointer)->DeleteLocalRef(jTempArray);
        
        
    }
    
    //Building the orbit
    
    
    jobject orbit = (envPointer)->NewObject(classOrbit_, orbitConstructor_, orbitPointArray, timeDirection);
    
    return orbit;
    
    
}


jobject JNICurve::orbitConstructor(const vector<RealVector> coords, const int timeDirection) const {
    
    int i, j;
    
    jobjectArray  orbitPointArray  = (jobjectArray)(envPointer)->NewObjectArray(coords.size(), classOrbitPoint_, NULL);
    
    for(i=0;i < coords.size();i++ ){
        
        RealVector vectorCoords = coords.at(i);
        
        jdoubleArray jTempArray = (envPointer)->NewDoubleArray(vectorCoords.size());
        
        double bufferArray [vectorCoords.size()] ;
        
        for (j=0; j < vectorCoords.size();j++ ){
            
            bufferArray[j]=vectorCoords.component(j);
        }
        
        (envPointer)->SetDoubleArrayRegion(jTempArray, 0, vectorCoords.size(), bufferArray);
        
        jobject orbitPoint = (envPointer)->NewObject(classOrbitPoint_, orbitPointConstructor_, jTempArray);
        
        (envPointer)->SetObjectArrayElement(orbitPointArray, i, orbitPoint);
        
        (envPointer)->DeleteLocalRef(orbitPoint);
        (envPointer)->DeleteLocalRef(jTempArray);
        
        
    }
    
    //Building the orbit
    
    
    jobject orbit = (envPointer)->NewObject(classOrbit_, orbitConstructor_, orbitPointArray, timeDirection);
    
    return orbit;
    
    
}

jobject JNICurve::stationaryPointConstructor(double * phasePoint,  int phasePointSize,  double * eigenValR,  int eigenValRSize,
        
         double *eigenValI,  int eigenValISize, jobjectArray realVectorArray,  int DimP ,
        
        jobject schurFormP , jobject schurVecP, const int DimN, jobject schurFormN,
        
        jobject schurVecN,  int integrationFlag)  {
    
    
    jobject localPhasePoint = utilInstance->phasePointConstructor(phasePoint, phasePointSize); //phasePoint
    
    jdoubleArray eigenValRLocal= envPointer->NewDoubleArray(eigenValRSize);//eigenValR
    
    envPointer->SetDoubleArrayRegion(eigenValRLocal, 0, eigenValRSize, eigenValR);
    
    jdoubleArray eigenValILocal= envPointer->NewDoubleArray(eigenValISize);//eigenValI
    
    envPointer->SetDoubleArrayRegion(eigenValILocal, 0, eigenValISize, eigenValI);
    
    
    jobject result = envPointer->NewObject(classStationaryPoint_, stationaryPointConstructor_, localPhasePoint, eigenValRLocal, eigenValILocal, realVectorArray, DimP, schurFormP,
            schurVecP, DimN , schurFormN, schurVecN, integrationFlag);
    
    envPointer->DeleteLocalRef(eigenValRLocal);
    envPointer->DeleteLocalRef(eigenValILocal);
    
    return result;
    
}

