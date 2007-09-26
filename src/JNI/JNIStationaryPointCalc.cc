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
    
    JNIUtil * utilInstance =  new JNIUtil(env);
    
    JNICurve * curveInstance = new JNICurve(env);
    
  double * teste = new double [2];

    teste[0]=0.1;
    teste[1]=0.1;

    
    double  * realMatrixTeste= new double [4];
    
    realMatrixTeste[0]=0.1;
    realMatrixTeste[1]=0.1;
    realMatrixTeste[2]=0.1;
    realMatrixTeste[3]=0.1;
    
    double ** dataTeste= new double *[2];
    
    dataTeste[0]=teste;
    dataTeste[1]=teste;
    
    
    //Parametros para o construtor
    
    
    jobjectArray eigenVec = utilInstance->realVectorArrayConstructor(dataTeste, 2, 2);//TODO Array de RealVectors usados no construtor . Checar o tamanho desse array !!!
    
    int DimP=2;
    
    jobject  schurFormP = utilInstance->realMatrixConstructor(realMatrixTeste, 4);
    
    jobject  schurVecP = utilInstance->realMatrixConstructor(realMatrixTeste, 4);
    
    jobject  schurFormN =  utilInstance->realMatrixConstructor(realMatrixTeste, 4);
    
    jobject  schurVecN = utilInstance->realMatrixConstructor(realMatrixTeste, 4);
    
    
    int integrationFlag=1;
    
    //--------------------------------------------------------------------------------------------------------------------------
    
    
    jobject result = curveInstance->stationaryPointConstructor(teste, 2, teste, 2, teste, 2, eigenVec, 1, schurFormP, schurVecP,DimP, schurFormN, schurVecN, integrationFlag);
    
    return result;
    
    
//    public StationaryPoint(PhasePoint point, double[] eigenValR, double[] eigenValI, RealVector[] eigenVec, int DimP,
//            RealMatrix2 schurFormP, RealMatrix2 schurVecP, int DimN, RealMatrix2 schurFormN,
//            RealMatrix2 schurVecN, int integrationFlag) {
    
    
}
