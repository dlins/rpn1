#include "JNIUtil.h"
#include "JNIDefs.h"
#include "math.h"
#include <iostream>

//Constructors utils

JNIUtil::~JNIUtil(){}


JNIUtil::JNIUtil(JNIEnv * env):envPointer(env){
    
    realVectorClass_= env->FindClass(REALVECTOR_LOCATION);
    
    realMatrix2Class_= env->FindClass(REALMATRIX2_LOCATION);
    
    phasePointClass_= env->FindClass(PHASEPOINT_LOCATION);
    
    orbitPointClass_ = (env)->FindClass(ORBITPOINT_LOCATION);
    
    arrayListClass_=env->FindClass("java/util/ArrayList");
    
    arrayListAddMethod_=env->GetMethodID(arrayListClass_, "add", "(Ljava/lang/Object;)Z"); 
    
    phasePointConstructor_ = (env)->GetMethodID(phasePointClass_, "<init>", "(Lwave/util/RealVector;)V");
    
    realVectorConstructorDoubleArray_= env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    
    realVectorConstructorSize_= env->GetMethodID(realVectorClass_, "<init>", "(I)V");
    
    realMatrix2Constructor_= env->GetMethodID(realMatrix2Class_, "<init>", "(II[D)V");
    
    arrayListConstructor_= env->GetMethodID(arrayListClass_, "<init>", "(I)V");
    
}



// JNIUtil * JNIUtil::instance(JNIEnv * env){
//    
////    if (instance_==NULL)
//        instance_= new JNIUtil(env);
//        return instance_;
////    }
////    return instance_;
//}


jobject JNIUtil::arrayListConstructor() {
    
    
    if (envPointer==NULL)
        
        printf("nulo\n");
    
    envPointer->ExceptionDescribe();
    
//    jobject result2=  envPointer->NewObject(arrayListClass_, arrayListConstructor_);
    
    jobject result= envPointer->NewObject(arrayListClass_, arrayListConstructor_, 10);
    
    envPointer->ExceptionDescribe();
    
    if (result==NULL)
        
        printf("result nulo");
    
    printf("Chamando arrayListConstructor\n");
    
    return result;
    
    
}

void JNIUtil::arrayListAdd(jobject list , jobject element )const {

    envPointer->CallBooleanMethod(list, arrayListAddMethod_, element);

}

jobject JNIUtil::phasePointConstructor(const double * coords, const int coordsSize ) const {
    
    jdoubleArray tempArray = envPointer->NewDoubleArray(coordsSize);
    
    envPointer->SetDoubleArrayRegion(tempArray, 0, coordsSize, coords);
    
    jobject realVector=  envPointer->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
   
    
//    envPointer->ExceptionDescribe();
    jobject phasePoint = envPointer->NewObject(phasePointClass_, phasePointConstructor_, realVector);

    envPointer->ExceptionDescribe();
    
    envPointer->DeleteLocalRef(tempArray);

    //TODO Limpar tambem o RealVector ???
    return phasePoint;
    
}


jobjectArray JNIUtil::realVectorArrayConstructor( double ** data , const int dataSize,  const int elementsSize) const {
    
    int i;
    
    jobjectArray result = envPointer->NewObjectArray(dataSize, realVectorClass_, NULL);
    
    for (i=0; i < dataSize;i++){
        
        jdoubleArray tempArray = envPointer->NewDoubleArray(elementsSize) ;
        
        envPointer->SetDoubleArrayRegion(tempArray, 0, elementsSize, data[i]);
        
        jobject tempRealVector = envPointer->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
        
        envPointer->SetObjectArrayElement(result, i, tempRealVector);
        
        envPointer->DeleteLocalRef(tempArray);
        envPointer->DeleteLocalRef(tempRealVector);
    }
    
    return result;
}

jobject JNIUtil::realMatrixConstructor(double * data, int dataSize) const{
    
    jdoubleArray tempArray = envPointer->NewDoubleArray(dataSize);
    
    envPointer->SetDoubleArrayRegion(tempArray, 0, dataSize, data);
    
    jobject result=  envPointer->NewObject(realMatrix2Class_, realMatrix2Constructor_,2, 2, tempArray );
    
    envPointer->DeleteLocalRef(tempArray);
    
    return result;
    
    
}


jobject JNIUtil::realVectorConstructor(double * data, const int size) const {
    
    jdoubleArray tempArray = envPointer->NewDoubleArray(size);
    
    envPointer->SetDoubleArrayRegion(tempArray, 0, size, data);
    
    printf("Tamanho do tempArray: %d \n", envPointer->GetArrayLength(tempArray));
    
    jobject result = envPointer->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    envPointer->ExceptionDescribe();
    
//    envPointer->DeleteLocalRef(tempArray);
    
    return result;
    
}

//jobject JNIUtil::realVectorConstructor(const int size) const {
//
//    return envPointer->NewObject(realVectorClass_, realVectorConstructorSize_, size);
//
//}

double * JNIUtil::phasePointToDouble(const jobject phasePoint, int & size)const {
    
    
    printf("Chamando phasePoint to double\n");
    envPointer->ExceptionDescribe();
    
    jmethodID toDoubleMethodID = (envPointer)->GetMethodID(phasePointClass_, "toDouble", "()[D");
    
    jdoubleArray phasePointArray =(jdoubleArray) (envPointer)->CallObjectMethod(phasePoint, toDoubleMethodID);
    
    size = (envPointer)->GetArrayLength(phasePointArray);
    
    double * result = new double [size];
    
    (envPointer)->GetDoubleArrayRegion(phasePointArray, 0, size, result); /* Copy the java primitive array passed as a parameter to the C buffer array */
    
    envPointer->DeleteLocalRef(phasePointArray);
    
    return result;
    
}

double * JNIUtil::orbitPointToDouble(const jobject orbitPoint, int & size) const { // TODO pq nao funciona na segunda chamada do metodo calc de orbit calc
    
    jmethodID toDoubleMethodID = (envPointer)->GetMethodID(orbitPointClass_, "toDouble", "()[D");
    
    jdoubleArray phasePointArray =(jdoubleArray) (envPointer)->CallObjectMethod(orbitPoint, toDoubleMethodID);
    
    size = (envPointer)->GetArrayLength(phasePointArray);
    
    double * result = new double [size];
    
    (envPointer)->GetDoubleArrayRegion(phasePointArray, 0, size, result);

    envPointer->DeleteLocalRef(phasePointArray);
    
    return result;
}


