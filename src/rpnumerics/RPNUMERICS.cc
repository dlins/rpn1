#include "rpnumerics_RPNUMERICS.h"
#include "JNIUtil.h"

/* Inaccessible static: profile_ */
/* Inaccessible static: hugoniotCurveCalc_ */
/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    init
 * Signature: (Lrpnumerics/RPNumericsProfile{})V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_init(JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initPhysics
 * Signature: ()Lrpnumerics/Physics;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_initPhysics
        (JNIEnv * env, jclass cls){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initShockFlow
 * Signature: (Lrpnumerics/PhasePoint;D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_initShockFlow
        (JNIEnv * env, jclass cls, jobject obj , jdouble jdarray){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initRarefactionFlow
 * Signature: (Lrpnumerics/PhasePoint;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_initRarefactionFlow
        (JNIEnv * env, jclass cls, jobject obj, jstring str){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changePoincareSection
 * Signature: (Lrpnumerics/ConnectionOrbit;)Lwave/util/PoincareSection;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_changePoincareSection
        (JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setPoincareSectionFlag
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setPoincareSectionFlag
        (JNIEnv * env, jclass cls, jboolean bol){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeFluxParams
 * Signature: ([D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_changeFluxParams
        (JNIEnv * env, jclass cls, jdoubleArray jdarray){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeErrorControl
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_changeErrorControl
        (JNIEnv * env, jclass cls, jdouble eps){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeSigma
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_changeSigma__D
        (JNIEnv * env, jclass cls, jdouble sigma){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeSigma
 * Signature: (Lwave/util/RealVector;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_changeSigma__Lwave_util_RealVector_2
        (JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getSigma
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_rpnumerics_RPNUMERICS_getSigma
        (JNIEnv * env, jclass cls){

    printf ("Chamando getSigma\n");
    
    return 0.1;
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getXZero
 * Signature: ()Lrpnumerics/PhasePoint;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getXZero
        (JNIEnv * env, jclass cls ){
    
    JNIUtil *utilInstance = new JNIUtil(env);
    
    printf("Chamando get XZero\n");
    double teste[2];
    
    teste[0]=0.1;
    teste[1]=0.1;
    
    int coordsSize=2;
    
    jclass realVectorClass_ = env->FindClass("wave/util/RealVector");
    
    jclass phasePointClass_ = env->FindClass("rpnumerics/PhasePoint");
    
    jmethodID phasePointConstructor_ = (env)->GetMethodID(phasePointClass_, "<init>", "(Lwave/util/RealVector;)V");
    
    jmethodID  realVectorConstructorDoubleArray_= env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    
    jdoubleArray tempArray = env->NewDoubleArray(coordsSize);
    
    env->SetDoubleArrayRegion(tempArray, 0, coordsSize, teste);
    
    jobject realVector=  env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    
    
//    envPointer->ExceptionDescribe();
    jobject phasePoint = env->NewObject(phasePointClass_, phasePointConstructor_, realVector);
    
    
    
    env->DeleteLocalRef(tempArray);
    
    //TODO Limpar tambem o RealVector ???
    return phasePoint;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    return    utilInstance->phasePointConstructor(teste, 2);
    
    
    
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setXZero
 * Signature: (Lrpnumerics/PhasePoint;)D
 */
JNIEXPORT jdouble JNICALL Java_rpnumerics_RPNUMERICS_setXZero
        (JNIEnv * env, jclass cls, jobject obj);

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getFluxParams
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_rpnumerics_RPNUMERICS_getFluxParams
        (JNIEnv * env, jclass cls){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setShockFlow
 * Signature: (Lrpnumerics/PhasePoint;D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setShockFlow
        (JNIEnv * env, jclass cls, jobject obj, jdouble sigma){
    
    printf("Setando shockFlow\n");
    
    
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setRarefactionFlow
 * Signature: (Lrpnumerics/PhasePoint;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setRarefactionFlow
        (JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    boundary
 * Signature: ()Lwave/util/Boundary;
 */
//JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary
//        (JNIEnv * env, jclass cls){}


JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary
        (JNIEnv * env, jclass cls){
    jclass realVectorClass = env->FindClass("wave/util/RealVector");
    
    jmethodID realVectorConstructor = (env)->GetMethodID(realVectorClass, "<init>", "([D)V");
    
    //Hardcoded para quad2 !!
    
    int borderLength =2;
    
    //TODO pegar os limites do boundary da fisica
    
    //  Boundary boundary = physics_->getBoundary();
    
    //  borderLength =boundary->getMinimums().size();
    
    if (borderLength ==2){
        
        jclass boundaryClass = env->FindClass("wave/util/RectBoundary");
        
        jmethodID boundaryConstructor = (env)->GetMethodID(boundaryClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
        
        //Hardcoded para quad2 !!
        
        double minimum [2]={-0.5, -0.5};
        double maximum [2]={0.5, 0.5};
        
        //---------------------------------
        
        jdoubleArray min = (env)->NewDoubleArray(2);
        jdoubleArray max = (env)->NewDoubleArray(2);
        
        (env)->SetDoubleArrayRegion(min, 0, 2, minimum);
        (env)->SetDoubleArrayRegion(max, 0, 2, maximum);
        
        jobject minRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, min);
        jobject maxRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, max);
        
        jobject boundary = (env)->NewObject(boundaryClass, boundaryConstructor, minRealVector, maxRealVector);
        
        (env)->DeleteLocalRef(min);
        (env)->DeleteLocalRef(max);
        (env)->DeleteLocalRef(minRealVector);
        (env)->DeleteLocalRef(maxRealVector);
        
        
        return boundary;
        
    }
    
    
    else {
        
        jclass isoRect2DBboundaryClass = env->FindClass("wave/util/IsoTriang2DBoundary");
        
        jmethodID isoTriang2DBoundaryConstructor = (env)->GetMethodID(isoRect2DBboundaryClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;Lwave/util/RealVector;)V");
        
        jdoubleArray A = (env)->NewDoubleArray(2);
        jdoubleArray B = (env)->NewDoubleArray(2);
        jdoubleArray C = (env)->NewDoubleArray(2);
        
        //Hardcoded para triphase !!
        double Anative [2]={0, 0};
        double  Bnative [2]={0, 1};
        double  Cnative [2] ={1, 0};
        //---------------------------
        
        (env)->SetDoubleArrayRegion(A, 0, 2, Anative);
        (env)->SetDoubleArrayRegion(B, 0, 2, Bnative);
        (env)->SetDoubleArrayRegion(C, 0, 2, Cnative);
        
        jobject ArealVector = (env)->NewObject(realVectorClass, realVectorConstructor, A);
        jobject BrealVector = (env)->NewObject(realVectorClass, realVectorConstructor, B);
        jobject CrealVector = (env)->NewObject(realVectorClass, realVectorConstructor, C);
        
        jobject isoTriang2DBoundary = (env)->NewObject(isoRect2DBboundaryClass, isoTriang2DBoundaryConstructor, ArealVector, BrealVector, CrealVector);
        
        (env)->DeleteLocalRef(A);
        (env)->DeleteLocalRef(B);
        (env)->DeleteLocalRef(C);
        
        (env)->DeleteLocalRef(ArealVector);
        (env)->DeleteLocalRef(BrealVector);
        (env)->DeleteLocalRef(CrealVector);
        
        return isoTriang2DBoundary;
        
    }
    
}






