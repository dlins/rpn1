/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RPNUMERICS.cc
 **/


//! Definition of RPNUMERICS.
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

#include "rpnumerics_RpNumerics.h"
#include "RpNumerics.h"
#include "Quad2.h"
#include "Quad2FluxParams.h"

#include "ContinuationRarefactionMethod.h"

#include "LSODEStopGenerator.h"
#include "LSODEProfile.h"
#include "LSODE.h"

#include "JNIDefs.h"
#include <string.h>
#include <iostream>

using namespace std;

const Physics * RpNumerics::physics_=NULL;

const ODESolver * RpNumerics::odeSolver_=NULL;

double RpNumerics::sigma=0;




void RpNumerics::initODESolver(){
    
    LSODEStopGenerator stopGenerator(1000);
    
    int dimension=2;
    
    int itol=2;
    
    double rtol=1e-4;
    
    int mf =22;
    
    double deltaxi = 0.001; // Minha escolha
    
    int nparam = 1 + dimension;
    
    double param [nparam];
    
    param[0] = 1; // Family Minha escolha
    
    int ii;
    
    for (ii = 0; ii < dimension; ii++) param[1 + ii] = 0.1;// Minha escolha  // Reference vector
    
    
    LSODEProfile lsodeProfile(RpNumerics::getFlux(), stopGenerator, dimension, itol, rtol, mf, deltaxi, nparam, param); // o solver apenas passa nparam e param para a funcao
    
    odeSolver_= new LSODE(lsodeProfile);
    
    
    
}
void RpNumerics::clean(){
    
    delete physics_;
    delete odeSolver_;
}


JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_init(JNIEnv * env, jclass cls, jobject numericsProfile){
    
    jclass numericsProfileClass = env->FindClass(NUMERICSPROFILE_LOCATION);
    jmethodID getPhysIDMethod = env->GetMethodID(numericsProfileClass, "getPhysicsID", "()Ljava/lang/String;");
    
//    jmethodID getFamilyIndexMethod = env->GetMethodID(numericsProfileClass, "getFamilyIndex", "()I");
    
    jstring ID=  (jstring)env->CallObjectMethod(numericsProfile, getPhysIDMethod);
    
    const char *physicsID;
    
    physicsID = env->GetStringUTFChars(ID, NULL);
    
    if (physicsID == NULL) {
        return; /* OutOfMemoryError already thrown */
    }
    
    //Physics instantiation
    
    if (!strcmp(physicsID, "QuadraticR2")){
        
        RpNumerics::setPhysics(Quad2(Quad2FluxParams())); 
        
    }
    
    //ODE solver instantiation
    
    RpNumerics::initODESolver();
    
    cout <<"Physics: "<< physicsID <<endl;
    
    env->ReleaseStringUTFChars(ID, physicsID);
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initShockFlow
 * Signature: (Lrpnumerics/PhasePoint;D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_initShockFlow
        (JNIEnv * env, jclass cls, jobject obj , jdouble jdarray){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    initRarefactionFlow
 * Signature: (Lrpnumerics/PhasePoint;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_initRarefactionFlow
        (JNIEnv * env, jclass cls, jobject obj, jstring str){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changePoincareSection
 * Signature: (Lrpnumerics/ConnectionOrbit;)Lwave/util/PoincareSection;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RpNumerics_changePoincareSection
        (JNIEnv * env, jclass cls, jobject obj){return NULL;}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setPoincareSectionFlag
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_setPoincareSectionFlag
        (JNIEnv * env, jclass cls, jboolean bol){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeFluxParams
 * Signature: ([D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_changeFluxParams
        (JNIEnv * env, jclass cls, jdoubleArray jdarray){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeErrorControl
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_changeErrorControl
        (JNIEnv * env, jclass cls, jdouble eps){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeSigma
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_changeSigma__D
        (JNIEnv * env, jclass cls, jdouble sigma){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    changeSigma
 * Signature: (Lwave/util/RealVector;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_changeSigma__Lwave_util_RealVector_2
        (JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getSigma
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_rpnumerics_RpNumerics_getSigma
        (JNIEnv * env, jclass cls){
    
    return 0.1;
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getXZero
 * Signature: ()Lrpnumerics/PhasePoint;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RpNumerics_getXZero
        (JNIEnv * env, jclass cls ){
    
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
    
    jobject phasePoint = env->NewObject(phasePointClass_, phasePointConstructor_, realVector);
    
    env->DeleteLocalRef(tempArray);
    
    //TODO Limpar tambem o RealVector ???
    return phasePoint;
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setXZero
 * Signature: (Lrpnumerics/PhasePoint;)D
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_setXZero
        (JNIEnv * env, jclass cls, jobject obj){ }

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getFluxParams
 * Signature: ()[D
 */
JNIEXPORT jdoubleArray JNICALL Java_rpnumerics_RpNumerics_getFluxParams
        (JNIEnv * env, jclass cls){ return NULL;}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setShockFlow
 * Signature: (Lrpnumerics/PhasePoint;D)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_setShockFlow
        (JNIEnv * env, jclass cls, jobject obj, jdouble sigma){
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setRarefactionFlow
 * Signature: (Lrpnumerics/PhasePoint;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RpNumerics_setRarefactionFlow
        (JNIEnv * env, jclass cls, jobject obj){}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    boundary
 * Signature: ()Lwave/util/Boundary;
 */
//JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary
//        (JNIEnv * env, jclass cls){}


JNIEXPORT jint JNICALL Java_rpnumerics_RpNumerics_domainDim(JNIEnv *, jclass cls){ return  RpNumerics::getPhysics().domain().dim();}



JNIEXPORT jobject JNICALL Java_rpnumerics_RpNumerics_boundary(JNIEnv * env, jclass cls){
    
    jclass realVectorClass = env->FindClass("wave/util/RealVector");
    
    jmethodID realVectorConstructor = (env)->GetMethodID(realVectorClass, "<init>", "([D)V");
    

    const Boundary & boundary= RpNumerics::getPhysics().boundary();
    
    
    int borderLength =2; //TODO How to know if this is a  RectBoundary ???
    
    if (borderLength ==2){
        
        jclass boundaryClass = env->FindClass("wave/util/RectBoundary");
        
        jmethodID boundaryConstructor = (env)->GetMethodID(boundaryClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");
        
        double minimum [2];
        double maximum [2];
        
        minimum[0]=boundary.minimums().component(0);
        
        minimum[1]=boundary.minimums().component(1);
        
        maximum[0]=boundary.maximums().component(0);
        maximum[1]=boundary.maximums().component(1);
        
        
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






