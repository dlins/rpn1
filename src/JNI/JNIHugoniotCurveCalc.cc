#include "rpnumerics_HugoniotCurveCalcND.h"
#include "JNIDefs.h"
#include <vector>

using std::vector;

JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
        (JNIEnv * env, jobject obj , jobject uMinus){
    
    printf("Seting UMinus\n");
    
}

JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc
        (JNIEnv * env, jobject obj, jobject uMinus){
    
    jclass    classPhasePoint = (env)->FindClass(PHASEPOINT_LOCATION);
    
    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
    
    jclass    realVectorClass_= env->FindClass(REALVECTOR_LOCATION);
    
    jclass   arrayListClass_=env->FindClass("java/util/ArrayList");
    
    jclass hugoniotPointTypeClass = (env)->FindClass(HUGONIOTPOINTTYPE_LOCATION);
    
    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);
    
    jmethodID toDoubleMethodID = (env)->GetMethodID(classPhasePoint, "toDouble", "()[D");
    jmethodID    realVectorConstructorDoubleArray_= env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DLrpnumerics/HugoniotPointType;)V");
    jmethodID arrayListConstructor_= env->GetMethodID(arrayListClass_, "<init>", "()V");
    jmethodID  arrayListAddMethod_=env->GetMethodID(arrayListClass_, "add", "(Ljava/lang/Object;)Z");
    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Ljava/util/List;)V");
    
    int i;
    
    //Input processing
    jdoubleArray phasePointArray =(jdoubleArray) (env)->CallObjectMethod(uMinus, toDoubleMethodID);
    
    int dimension = env->GetArrayLength(phasePointArray);
    
    double input [dimension];
    
    env->GetDoubleArrayRegion(phasePointArray, 0, dimension, input);
    
    env->DeleteLocalRef(phasePointArray);
    
    //Calculations using the input
    
    jobject segmentsArray = env->NewObject(arrayListClass_, arrayListConstructor_, NULL);
    
    vector <double *> coords ;
    double ix=-0.5;
    
    while (ix < 0.5){
        double * coord = new double [dimension];
        
        coord[0]=input[0]+ix;
        coord[1]=input[1]+ix;
        
        coords.push_back(coord);
        ix+=0.005;
    }
    
    double leftSigma =1;
    double rightSigma =1;
    
// public HugoniotSegment (RealVector leftPoint, double leftSigma, RealVector rightPoint, double rightSigma, HugoniotPointType type);
    
    // Hugoniot Segments list creation
    
    //Segment type eigenValLeft and Right initialization
    
    jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
    jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
    
    for (i=0;i < coords.size();i++){
        
        double * dataCoords = (double *)coords[i];
        
        env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, dataCoords);
        env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, dataCoords);
        
        delete [] dataCoords;
        
        //Construindo o point type
        
        jobject pointType = env->NewObject(hugoniotPointTypeClass, hugoniotPointTypeConstructor, eigenValRLeft, eigenValRRight);
        
        //Construindo left e right points
        jobject realVectorLeftPoint = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, eigenValRLeft);
        
        jobject realVectorRightPoint =env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, eigenValRRight);
        
        //Construindo o segmento
        
        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorLeftPoint, rightSigma, pointType);
        
        // Adicionando o segmento a lista
        
        env->CallObjectMethod(segmentsArray, arrayListAddMethod_, hugoniotSegment);
        
        // Limpando
        
        env->DeleteLocalRef(pointType);
        
        env->DeleteLocalRef(realVectorLeftPoint);
        
        env->DeleteLocalRef(realVectorRightPoint);
        
        env->DeleteLocalRef(hugoniotSegment);
    }
    
    
    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, segmentsArray);
    
    env->DeleteLocalRef(eigenValRLeft);
    env->DeleteLocalRef(eigenValRRight);
    env->DeleteLocalRef(hugoniotSegmentClass);
    env->DeleteLocalRef(realVectorClass_);
    env->DeleteLocalRef(arrayListClass_);
    
    coords.clear();
    
    return result;
    
    
}
















