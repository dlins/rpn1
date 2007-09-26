#include "rpnumerics_HugoniotCurveCalcND.h"
#include "JNIUtil.h"
#include "JNIDefs.h"


JNIEXPORT void JNICALL Java_rpnumerics_HugoniotCurveCalcND_setUMinus
        (JNIEnv * env, jobject obj , jobject uMinus){

    printf("Chamando setUMinus\n");

//    JNIUtil * utilInstance = JNIUtil::instance(env);
    

//    int size;
    
//    double * nativeUMinus= utilInstance->phasePointToDouble(uMinus, size);
    
//    env->ExceptionDescribe();
    //TODO chamar o metodo uMinus com o arrayNativo obtido
    
    
    
}



JNIEXPORT jobject JNICALL Java_rpnumerics_HugoniotCurveCalcND_calc
        (JNIEnv * env, jobject obj, jobject uMinus){
    
    JNIUtil * utilInstance = new JNIUtil(env);
    
    int i, dimension =2;
    
    double * testeLeftPoint = new double[2];
    
    double * testeRightPoint = new double[2];
    
//    double testeLeftPoint [dimension], testeRightPoint[dimension];
    
    jclass hugoniotSegmentClass = (env)->FindClass(HUGONIOTSEGMENTCLASS_LOCATION);
    
    jmethodID hugoniotSegmentConstructor = (env)->GetMethodID(hugoniotSegmentClass, "<init>", "(Lwave/util/RealVector;DLwave/util/RealVector;DLrpnumerics/HugoniotPointType;)V");
    
//    env->ExceptionDescribe();
    
    jclass hugoniotPointTypeClass = (env)->FindClass(HUGONIOTPOINTTYPE_LOCATION);
    
    jmethodID hugoniotPointTypeConstructor = (env)->GetMethodID(hugoniotPointTypeClass, "<init>", "([D[D)V");
    
//    env->ExceptionDescribe();
    
    jclass hugoniotCurveClass = env->FindClass(HUGONIOTCURVE_LOCATION);
    
    jmethodID hugoniotCurveConstructor = env->GetMethodID(hugoniotCurveClass, "<init>", "(Ljava/util/List;)V");
    
//    env->ExceptionDescribe();
    
    //TODO qual o tamanho do array de autovalores ??
    
    jdoubleArray eigenValRLeft = env->NewDoubleArray(dimension);
    
    jdoubleArray eigenValRRight = env->NewDoubleArray(dimension);
    
    jobject segmentsArray = utilInstance->arrayListConstructor();
    
    
    testeLeftPoint[0]=0.1;
    testeLeftPoint[1]=0.1;
    
    testeRightPoint[0]=0.2;
    testeRightPoint[1]=0.2;
    
    int size=10; //TODO Pegar o tamanho da lista de coordenadas retornado pelo metodo nativo
    
    double leftSigma =1;
    double rightSigma =1;
    
    env->SetDoubleArrayRegion(eigenValRLeft, 0, dimension, testeLeftPoint);
    env->SetDoubleArrayRegion(eigenValRRight, 0, dimension, testeRightPoint);
    
    
    for (i=0;i < size;i++){
        
        //TODO Pegar o segmento calculado pelo metodo nativo
        //TODO Pegar os autovalores de cada ponto
        
        
        //Construindo o point type
        
        jobject pointType = env->NewObject(hugoniotPointTypeClass, hugoniotPointTypeConstructor, eigenValRLeft, eigenValRRight);
        
        //Construindo left e right points
        jobject realVectorLeftPoint =utilInstance->realVectorConstructor(testeLeftPoint, 2);
        
        jobject realVectorRightPoint =utilInstance->realVectorConstructor(testeRightPoint, 2);
        
        //Construindo o segmento
        
        jobject hugoniotSegment = env->NewObject(hugoniotSegmentClass, hugoniotSegmentConstructor, realVectorLeftPoint, leftSigma, realVectorLeftPoint, rightSigma, pointType);
        
        // Adicionando o segmento a lista
        
        utilInstance->arrayListAdd(segmentsArray, hugoniotSegment); //TODO adicionar o segmento a lista de segmentos. Relacionado com o TODO  do .h de JNIUtil
        
        // Limpando
        
        env->DeleteLocalRef(pointType);
        
        env->DeleteLocalRef(realVectorLeftPoint);
        
        env->DeleteLocalRef(realVectorRightPoint);
        
        env->DeleteLocalRef(hugoniotSegment);
    }
    
    
    jobject result = env->NewObject(hugoniotCurveClass, hugoniotCurveConstructor, segmentsArray);
    
    env->DeleteLocalRef(eigenValRLeft);
    env->DeleteLocalRef(eigenValRRight);
    
    printf ("Passei pelo hugoniot curve calc\n");
    return result;
    
    
}
















