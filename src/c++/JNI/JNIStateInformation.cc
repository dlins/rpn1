/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIHugoniotCurveCalc.cc
 **/


//! Definition of JNIHugoniotCurveCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */


#include "rpnumerics_StateInformation.h"
#include "JNIDefs.h"
#include "RpNumerics.h"
#include <vector>
#include <iostream>


using std::vector;
using namespace std;

JNIEXPORT jobject JNICALL Java_rpnumerics_StateInformation_getStateInformation
(JNIEnv * env, jobject obj, jobject inputPoint) {

    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    jclass stateInfoClass = env->FindClass(RPNSTATEINFO);



    //
    jclass hashMapClass = env->FindClass("java/util/HashMap");
    //
    //    jclass ellipticBoundaryClass = env->FindClass(ELLIPTICBOUNDARY_LOCATION);
    //
    //
    //    jmethodID realVectorConstructorDoubleArray = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    //


    //

    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    jmethodID hashMapConstructor = env->GetMethodID(hashMapClass, "<init>", "()V");

    jmethodID arrayListAddMethod = env->GetMethodID(hashMapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");




    //Input processing
    jdoubleArray inputPointArray = (jdoubleArray) (env)->CallObjectMethod(inputPoint, toDoubleMethodID);

    int dimension = env->GetArrayLength(inputPointArray);

    double input [dimension];

    env->GetDoubleArrayRegion(inputPointArray, 0, dimension, input);

    env->DeleteLocalRef(inputPointArray);


    const FluxFunction * flux = &RpNumerics::getPhysics().fluxFunction();
    const AccumulationFunction * accum = &RpNumerics::getPhysics().accumulation();


    WaveState inputState(RealVector(dimension,input));

    JetMatrix output(dimension);


    int jetOutput = flux->jet(inputState, output, 2);

    cout<<"Chamando nativo"<<endl;


    return NULL;



}
















