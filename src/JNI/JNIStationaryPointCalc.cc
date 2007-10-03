#include "rpnumerics_StationaryPointCalc.h"
#include "JNIUtil.h"
#include "JNIDefs.h"
#include "JNICurve.h"
/*
 * Class:     rpnumerics_StationaryPointCalc
 * Method:    calc
 * Signature: (Lrpnumerics/PhasePoint;)Lrpnumerics/RpSolution;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_StationaryPointCalc_calc(JNIEnv *env, jobject obj, jobject initialpoint){
    
    int dimension = 2; //TODO O tamanho eh a dimensao da fisica ???
    
    jclass    realVectorClass_= env->FindClass(REALVECTOR_LOCATION);
    jclass  realMatrix2Class_= env->FindClass(REALMATRIX2_LOCATION);
    jclass  classStationaryPoint_=env->FindClass(STATIONARYPOINT_LOCATION);
    
    jmethodID    stationaryPointConstructor_=env->GetMethodID(classStationaryPoint_, "<init>", "(Lrpnumerics/PhasePoint;[D[D[Lwave/util/RealVector;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;ILwave/util/RealMatrix2;Lwave/util/RealMatrix2;I)V");
    jmethodID    realVectorConstructorDoubleArray_= env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    jmethodID     realMatrix2Constructor_= env->GetMethodID(realMatrix2Class_, "<init>", "(II[D)V");
    
    double * teste = new double [2];
    
    teste[0]=0.1;
    teste[1]=0.1;
    
    
    double  * realMatrixTeste= new double [4];
    
    realMatrixTeste[0]=0.1;
    realMatrixTeste[1]=0.1;
    realMatrixTeste[2]=0.1;
    realMatrixTeste[3]=0.1;
    
    //Parametros para o construtor
    
    
//    jobjectArray eigenVec = utilInstance->realVectorArrayConstructor(dataTeste, 2, 2);//TODO Array de RealVectors usados no construtor . Checar o tamanho desse array !!!
    
    jobjectArray eigenVec = env->NewObjectArray(2, realVectorClass_, NULL);
    
    jdoubleArray tempArray = env->NewDoubleArray(2) ;
    
    env->SetDoubleArrayRegion(tempArray, 0, 2, teste);
    
    jobject tempRealVector1 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    jobject tempRealVector2 = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    env->SetObjectArrayElement(eigenVec, 0, tempRealVector1);
    env->SetObjectArrayElement(eigenVec, 1, tempRealVector2);
    
    int DimP=2;
    
    jdoubleArray tempArrayMATRIX = env->NewDoubleArray(4);
    
    env->SetDoubleArrayRegion(tempArrayMATRIX, 0, 4, realMatrixTeste);
    
    jobject schurFormP=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurVecP=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurFormN=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    jobject schurVecN=  env->NewObject(realMatrix2Class_, realMatrix2Constructor_, 2, 2, tempArrayMATRIX );
    
    
    int integrationFlag=1;
    
    jobject result = env->NewObject(classStationaryPoint_, stationaryPointConstructor_, initialpoint, tempArray, tempArray, eigenVec, DimP, schurFormP, schurVecP, DimP, schurFormN, schurVecN, integrationFlag);
    
    return result;
    
//    return NULL;
    
    
//    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec, int DimP,
//            RealMatrix2 schurFormP, RealMatrix2 schurVecP, int DimN, RealMatrix2 schurFormN,
//            RealMatrix2 schurVecN, int integrationFlag) {
    
    
}
