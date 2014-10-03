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

//#include "rpnumerics_RpNumerics.h"
#include <stdlib.h>
#include "rpnumerics_RPNUMERICS.h"
#include "RpNumerics.h"


//-------------------------------------
// PHYSICS
//-------------------------------------



//-------------------------------------

#include "JNIDefs.h"


using namespace std;





vector<StationaryPoint *> * RpNumerics::stationaryPointVector_ = NULL;
map<int, WaveCurve *> * RpNumerics::waveCurveMap_ = NULL;

std::vector<string> * RpNumerics::hugoniotNamesVector_ = NULL;


std::vector<HugoniotConfig *> * RpNumerics::hugoniotCasesVector_ = NULL;



std::vector<SubPhysics*> * RpNumerics::physicsVector_ = new vector<SubPhysics *>();

map<string, bool (*)(const eigenpair&, const eigenpair&) > * RpNumerics::orderFunctionMap_ = NULL;


double RpNumerics::sigma = 0;

int RpNumerics::curveCounter = 0;

void RpNumerics::fillHugoniotNames() {

    vector<HugoniotCurve*> hugoniotMethodsVector;

    hugoniotNamesVector_ = new vector<string>();

    hugoniotCasesVector_ = new vector<HugoniotConfig *>();


    physicsVector_->at(0)->list_of_Hugoniot_methods(hugoniotMethodsVector);



    for (int i = 0; i < hugoniotMethodsVector.size(); i++) {

        hugoniotNamesVector_->push_back(hugoniotMethodsVector[i]->Hugoniot_info());


        vector<int> types;
        vector<string> typeNames;


        hugoniotMethodsVector[i]->list_of_reference_points(types, typeNames);

        hugoniotCasesVector_->push_back(new HugoniotConfig(hugoniotMethodsVector[i]->Hugoniot_info(), types, typeNames));

        cout << hugoniotMethodsVector[i]->Hugoniot_info() << endl;

    }




}


/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getHugoniotNames
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getHugoniotNames
  (JNIEnv * env , jclass cls){
    
    
    
    jclass stringClass = env->FindClass("Ljava/lang/String;");
    
    jobjectArray hugoniotNames = env->NewObjectArray( RpNumerics::hugoniotCasesVector_->size(),stringClass,NULL);
    
    
    
    for (int i = 0; i <     RpNumerics::hugoniotCasesVector_->size(); i++) {
          jstring jhugoniotName = env->NewStringUTF(RpNumerics::hugoniotCasesVector_->at(i)->getName()->c_str());
                
          env->SetObjectArrayElement(hugoniotNames,i,jhugoniotName);
        
    }
    return hugoniotNames;
    
    
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getHugoniotCaseNames
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getHugoniotCaseNames
  (JNIEnv * env , jclass cls, jstring hugoniotMethodName){
    
    
    const char * hugoniotNameC;

    hugoniotNameC = env->GetStringUTFChars(hugoniotMethodName, NULL);
    
    jclass stringClass = env->FindClass("Ljava/lang/String;");
    
    
    string hugoniotName(hugoniotNameC);
    
    for (int i = 0; i <     RpNumerics::hugoniotCasesVector_->size(); i++) {

        
        HugoniotConfig  *config  =   RpNumerics::hugoniotCasesVector_->at(i);
        
        if(config->getName()->compare(hugoniotName)==0){
            vector<string>  * caseNames =  config->getCaseNames();
            
            jobjectArray caseNamesArray = env->NewObjectArray(caseNames->size(),stringClass,NULL);
            
            
            for (int j = 0; j < caseNames->size(); j++) {

                
                string caseName = caseNames->at(j);
                
                jstring jcaseName = env->NewStringUTF(caseName.c_str());
                
               env->SetObjectArrayElement(caseNamesArray,j,jcaseName);
                

            }
            
            return caseNamesArray;

            
        }
        
        
        
    }

    
    return NULL;
    
}









/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setRPnHome
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setRPnHome
(JNIEnv * env, jclass cls, jstring rpnHome) {

    const char *rpnHomeC;

    rpnHomeC = env->GetStringUTFChars(rpnHome, NULL);

    string rpnHomeString(rpnHomeC);

    //    Physics::setRPnHome(rpnHomeString);


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setParams
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setParams
(JNIEnv * env, jclass cls, jobjectArray stringParamsArray) {

    const char *paramNative;

    int paramSize = env->GetArrayLength(stringParamsArray);

    vector<string> paramVector;

    for (int i = 0; i < paramSize; i++) {
        jstring paramString = (jstring) (env)->GetObjectArrayElement(stringParamsArray, i);

        paramNative = env->GetStringUTFChars(paramString, NULL);

        string paramElementString(paramNative);
        paramVector.push_back(paramElementString);

    }
    //    RpNumerics::getPhysics().setParams(paramVector);


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getFluxParams
 * Signature: ()Lrpnumerics/FluxParams;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getFluxParams
(JNIEnv * env, jclass cls) {

    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);
    //
    //
    //    jmethodID fluxParamsConstructorID = (env)->GetMethodID(fluxParamsClass, "<init>", "(Lwave/util/RealVector;)V");
    //    jmethodID realVectorConstructorID = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    //
    //
    //
    ////    vector<double> * paramsVector = RpNumerics::getPhysics().getSubPhysics(0).getParams();
    //
    //    //    const FluxParams & nativeFluxParams = RpNumerics::getPhysics().fluxFunction().fluxParams();
    //
    //    double nativeRealVectorArray[paramsVector->size()];
    //
    //    for (int i = 0; i < paramsVector->size(); i++) {
    //
    //        nativeRealVectorArray[i] = paramsVector->at(i);
    //    }
    //
    //    jdoubleArray realVectorArray = env->NewDoubleArray(paramsVector->size());
    //    env->SetDoubleArrayRegion(realVectorArray, 0, paramsVector->size(), nativeRealVectorArray);
    //
    //    jobject realVector = (env)->NewObject(realVectorClass, realVectorConstructorID, realVectorArray);
    //    jobject fluxParams = (env)->NewObject(fluxParamsClass, fluxParamsConstructorID, realVector);
    //
    //    return fluxParams;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setAccumulationParams
 * Signature: (Lwave/util/RealVector;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setAccumulationParams
(JNIEnv * env, jclass cls, jobject newParamsVector) {


    //
    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    //    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    //
    //
    //    //Input processing
    //    jdoubleArray newVectorArray = (jdoubleArray) (env)->CallObjectMethod(newParamsVector, toDoubleMethodID);
    //
    //    int dimension = env->GetArrayLength(newVectorArray);
    //
    //    double input [dimension];
    //
    //    env->GetDoubleArrayRegion(newVectorArray, 0, dimension, input);
    //
    //    RealVector newAccumulationParamsVector(dimension, input);
    //
    //    AccumulationParams newAccumulationParams(newAccumulationParamsVector);
    //    RpNumerics::getPhysics().accumulationParams(newAccumulationParams);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setMethod
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setMethod
(JNIEnv * env, jclass cls, jstring methodType, jstring methodName) {

    //    string nativeMethodType(env->GetStringUTFChars(methodType, NULL));
    //    string nativeMethodName(env->GetStringUTFChars(methodName, NULL));

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAccumulationParams
 * Signature: ()Lwave/util/RealVector;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getAccumulationParams
(JNIEnv * env, jclass cls) {

    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    //    jmethodID realVectorConstructorID = env->GetMethodID(realVectorClass, "<init>", "([D)V");
    //
    ////    const AccumulationParams & nativeAccumulationParams = RpNumerics::getPhysics().accumulation().accumulationParams();
    //
    //    const RealVector & nativeRealVectorParams = nativeAccumulationParams.params();
    //
    //    int paramsSize = nativeRealVectorParams.size();
    //
    //
    //    double nativeRealVectorArray[paramsSize];
    //
    //    for (int i = 0; i < paramsSize; i++) {
    //
    //        nativeRealVectorArray[i] = nativeRealVectorParams.component(i);
    //    }
    //
    //    jdoubleArray realVectorArray = env->NewDoubleArray(paramsSize);
    //    env->SetDoubleArrayRegion(realVectorArray, 0, paramsSize, nativeRealVectorArray);
    //
    //    jobject realVector = (env)->NewObject(realVectorClass, realVectorConstructorID, realVectorArray);
    //
    //
    //    return realVector;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setResolution
 * Signature: (Lwave/util/RealVector;Lwave/util/RealVector;Ljava/lang/String;[I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setResolution
(JNIEnv * env, jclass cls, jobject min, jobject max, jstring gridName, jintArray newResolution) {

    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    //    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    //
    //
    //    int dimension = env->GetArrayLength(newResolution);
    //
    //    //min  processing
    //    jdoubleArray minLimit = (jdoubleArray) (env)->CallObjectMethod(min, toDoubleMethodID);
    //
    //    double minNativeArray[dimension];
    //    env->GetDoubleArrayRegion(minLimit, 0, dimension, minNativeArray);
    //    //max processing
    //
    //    jdoubleArray maxLimit = (jdoubleArray) (env)->CallObjectMethod(max, toDoubleMethodID);
    //
    //    double maxNativeArray[dimension];
    //
    //    env->GetDoubleArrayRegion(maxLimit, 0, dimension, maxNativeArray);
    //
    //
    //    //Processing resolution
    //    vector<int>newResolutionVector;
    //
    //    int tempResolutionArray[dimension];
    //
    //
    //    env->GetIntArrayRegion(newResolution, 0, dimension, tempResolutionArray);
    //
    //
    //    for (int i = 0; i < dimension; i++) {
    //        newResolutionVector.push_back(tempResolutionArray[i]);
    //
    //    }
    //
    //    const char * gridNameNative = env->GetStringUTFChars(gridName, NULL);




    //    const Boundary * boundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();



    //    grid->set_grid(boundary, boundary->minimums(), boundary->maximums(), newResolutionVector);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFluxParams
 * Signature: (Lrpnumerics/FluxParams;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFluxParams(JNIEnv * env, jclass cls, jobject fluxParams) {


    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //    jclass fluxParamsClass = env->FindClass(FLUXPARAMS_LOCATION);
    //
    //    jmethodID getParamsMethodID = (env)->GetMethodID(fluxParamsClass, "getParams", "()Lwave/util/RealVector;");
    //    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    //
    //    jobject fluxParamRealVector = env->CallObjectMethod(fluxParams, getParamsMethodID);
    //    jdoubleArray fluxParamRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(fluxParamRealVector, toDoubleMethodID);
    //
    //    int fluxParamSize = env->GetArrayLength(fluxParamRealVectorArray);
    //    double nativeFluxParamArray[fluxParamSize];
    //
    //    env->GetDoubleArrayRegion(fluxParamRealVectorArray, 0, fluxParamSize, nativeFluxParamArray);
    //
    //    RealVector nativeFluxParamRealVector(fluxParamSize, nativeFluxParamArray);
    //    FluxParams newFluxParams(nativeFluxParamRealVector);
    //    RpNumerics::getPhysics().fluxParams(newFluxParams);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setBoundary
 * Signature: (Lwave/util/Boundary;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setBoundary
(JNIEnv * env, jclass cls, jobject newBoundary) {

    //    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //    jclass boundaryClass = env->FindClass(BOUNDARY_LOCATION);
    //
    //    jclass rectBoundaryClass = env->FindClass(RECTBOUNDARY_LOCATION);
    //
    //    jclass isoTriangBoundaryClass = env->FindClass(ISOTRIANG2DBOUNDARY_LOCATION);
    //
    //    jboolean testRectBoundary = env->IsInstanceOf(newBoundary, rectBoundaryClass);
    //
    //    jboolean testIsoTriangBoundary = env->IsInstanceOf(newBoundary, isoTriangBoundaryClass);
    //
    //    jmethodID getMinimumsMethodID = env->GetMethodID(boundaryClass, "getMinimums", "()Lwave/util/RealVector;");
    //    jmethodID getMaximumsMethodID = env->GetMethodID(boundaryClass, "getMaximums", "()Lwave/util/RealVector;");
    //    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    //
    //
    //    jmethodID getAMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getA", "()Lwave/util/RealVector;");
    //    jmethodID getBMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getB", "()Lwave/util/RealVector;");
    //    jmethodID getCMethodID = (env)->GetMethodID(isoTriangBoundaryClass, "getC", "()Lwave/util/RealVector;");
    //
    //
    //    jobject minRealVector = env->CallObjectMethod(newBoundary, getMinimumsMethodID);
    //    jobject maxRealVector = env->CallObjectMethod(newBoundary, getMaximumsMethodID);
    //
    //    jdoubleArray minRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(minRealVector, toDoubleMethodID);
    //    jdoubleArray maxRealVectorArray = (jdoubleArray) (env)->CallObjectMethod(maxRealVector, toDoubleMethodID);
    //
    //    int minSize = env->GetArrayLength(minRealVectorArray);
    //    int maxSize = env->GetArrayLength(maxRealVectorArray);
    //
    //    double minNativeArray [minSize];
    //    double maxNativeArray [maxSize];
    //
    //    env->GetDoubleArrayRegion(minRealVectorArray, 0, minSize, minNativeArray);
    //    env->GetDoubleArrayRegion(maxRealVectorArray, 0, maxSize, maxNativeArray);
    //
    //    if (testRectBoundary) { //  RectBoundary
    //
    //        RealVector minNativeRealVector(minSize, minNativeArray);
    //        RealVector maxNativeRealVector(maxSize, maxNativeArray);
    //        RectBoundary nativeBoundary(minNativeRealVector, maxNativeRealVector);
    ////        RpNumerics::getPhysics().boundary(nativeBoundary);
    //
    //
    //
    //    }
    //
    //    if (testIsoTriangBoundary) { //   IsoTriang2DBoundary
    //
    //
    //        RealVector minNativeRealVector(minSize, minNativeArray);
    //        RealVector maxNativeRealVector(maxSize, maxNativeArray);
    //
    //
    //
    //
    //        Three_Phase_Boundary triangBoundary(minNativeRealVector, maxNativeRealVector);
    //
    //
    ////        RpNumerics::getPhysics().boundary(triangBoundary);


    //    }



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setFamilyIndex
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setFamilyIndex
(JNIEnv *env, jobject obj, jint familyIndex) {


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setTimeDirection
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setTimeDirection
(JNIEnv * env, jobject obj, jint timeDirection) {
    //WaveFlowFactory::setTimeDirection(timeDirection);

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    clean
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_clean(JNIEnv * env, jclass cls) {
    RpNumerics::clean();
}

void RpNumerics::clean() {

    delete hugoniotNamesVector_;
    delete hugoniotCasesVector_;

    //    delete stationaryPointVector_;
    //
    //    delete orderFunctionMap_;
    //    delete physics_;

}

/* Class:     rpnumerics_RPNUMERICS
 * Method:    physicsID
 * Signature: ()Ljava/lang/String;
 */


JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_physicsID(JNIEnv * env, jclass cls) {

    return env->NewStringUTF(RpNumerics::physicsVector_->at(0)->info_subphysics().c_str());
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domain
 * Signature: ()Lwave/multid/Space;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_domain(JNIEnv * env, jclass cls) {

    jclass spaceClass = env->FindClass("wave/multid/Space");
    jmethodID spaceConstructor = (env)->GetMethodID(spaceClass, "<init>", "(Ljava/lang/String;I)V");

    jstring spaceName = env->NewStringUTF(RpNumerics::physicsVector_->at(0)->info_subphysics().c_str());

    int dimension = RpNumerics::physicsVector_->at(0)->boundary()->minimums().size();

    jobject space = env->NewObject(spaceClass, spaceConstructor, spaceName, dimension);

    return space;

}

/* Class:     rpnumerics_RPNUMERICS
 * Method:    initNative
 * Signature: (Ljava/lang/String;)V
 */

JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_initNative(JNIEnv * env, jclass cls, jstring physicsName) {

    
   
    
    
    
    
    
    const char *physicsID;

    physicsID = env->GetStringUTFChars(physicsName, NULL);


    if (physicsID == NULL) {
        return; /* OutOfMemoryError already thrown */
    }

    //    RpNumerics::setPhysics(Physics(physicsID));



    string physicsStringName(physicsID);



    if (physicsStringName.compare("JD") == 0) {
        RpNumerics::physicsVector_->push_back(new JDSubPhysics());
    }

    if (physicsStringName.compare("Stone") == 0) {
        RpNumerics::physicsVector_->push_back(new StoneSubPhysics());
    }


    if (physicsStringName.compare("CoreyQuad") == 0) {
        RpNumerics::physicsVector_->push_back(new CoreyQuadSubPhysics());
    }

    RpNumerics::fillHugoniotNames();




    








    //
    //    if (physicsID.compare("QuadraticR4") == 0) {
    //        physicsVector_->push_back(new Quad4(Quad4FluxParams()));
    //    }
    //
    //    if (physicsID.compare("Stone") == 0) {
    //        physicsVector_->push_back(new Stone());
    //    }
    //
    //    
    //    if (physicsID.compare("StoneNegative") == 0) {
    //        physicsVector_->push_back(new NegativeStone());
    //    }
    //    
    //
    //    if (physicsID.compare("Polydisperse") == 0) {
    //
    //        physicsVector_->push_back(new PolydispersePhysics());
    //    }
    //
    //
    //     if (physicsID.compare("CoreyQuad") == 0) {
    //
    //        physicsVector_->push_back(new CoreyQuadPhysics());
    //    }
    //
    //    
    //    
    //     if (physicsID.compare("TriPhase") == 0) {
    //
    //        physicsVector_->push_back(new TriPhase());
    //    }
    //
    //
    //
    //
    //
    //    if (physicsID.compare("Cub2") == 0) {
    //        physicsVector_->push_back(new Cub2(Cub2FluxParams()));
    //    }

























}

JNIEXPORT jobject JNICALL Java_rpnumerics_RpNumerics_getXZero(JNIEnv * env, jclass cls) {

    //    double teste[2];
    //
    //    teste[0] = 0.1;
    //    teste[1] = 0.1;
    //
    //    int coordsSize = 2;
    //
    //    jclass realVectorClass_ = env->FindClass("wave/util/RealVector");
    //    jclass phasePointClass_ = env->FindClass("rpnumerics/PhasePoint");
    //
    //    jmethodID phasePointConstructor_ = (env)->GetMethodID(phasePointClass_, "<init>", "(Lwave/util/RealVector;)V");
    //    jmethodID realVectorConstructorDoubleArray_ = env->GetMethodID(realVectorClass_, "<init>", "([D)V");
    //
    //    jdoubleArray tempArray = env->NewDoubleArray(coordsSize);
    //    env->SetDoubleArrayRegion(tempArray, 0, coordsSize, teste);
    //
    //    jobject realVector = env->NewObject(realVectorClass_, realVectorConstructorDoubleArray_, tempArray);
    //    jobject phasePoint = env->NewObject(phasePointClass_, phasePointConstructor_, realVector);
    //
    //    env->DeleteLocalRef(tempArray);
    //
    //    return phasePoint;
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getEigenSortFunctionNames
 * Signature: ()Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getEigenSortFunctionNames
(JNIEnv * env, jclass cls) {
    //
    //    jclass arrayListClass = env->FindClass("java/util/ArrayList");
    //    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    //    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    //    
    //    jobject functionNamesArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);
    //
    //    for (std::map < string, bool (*)(const eigenpair&, const eigenpair&) >::iterator it = RpNumerics::orderFunctionMap_->begin(); it != RpNumerics::orderFunctionMap_->end(); ++it) {
    //        
    //        jstring functionOrderName =   env->NewStringUTF(it->first.c_str());
    //        env->CallObjectMethod(functionNamesArray, arrayListAddMethod, functionOrderName);
    //
    //    }
    //
    //    return functionNamesArray;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setEigenSortFunction
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setEigenSortFunction
(JNIEnv * env, jclass cls, jstring functionName) {


    //
    //    const char *functionID;
    //
    //    functionID = env->GetStringUTFChars(functionName, NULL);
    //
    //
    //    string nativeFunctionName(functionID);
    //
    //
    //    RpNumerics::setEigenOrderFunction(nativeFunctionName);
    //
    //
    //



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    domainDim
 * Signature: ()I
 */

JNIEXPORT jint JNICALL Java_rpnumerics_RPNUMERICS_domainDim(JNIEnv * env, jclass cls) {
    return RpNumerics::physicsVector_->at(0)->boundary()->minimums().size();
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    boundary
 * Signature: ()Lwave/util/Boundary;
 */



JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_boundary(JNIEnv * env, jclass cls) {

    jclass realVectorClass = env->FindClass("wave/util/RealVector");
    jmethodID realVectorConstructor = (env)->GetMethodID(realVectorClass, "<init>", "([D)V");

    const Boundary * boundary = RpNumerics::physicsVector_->at(0)->boundary();
    const char * boundaryType = boundary->boundaryType();

    if (!strcmp(boundaryType, "rect")) {

        jclass boundaryClass = env->FindClass("wave/util/RectBoundary");
        jmethodID boundaryConstructor = (env)->GetMethodID(boundaryClass, "<init>", "(Lwave/util/RealVector;Lwave/util/RealVector;)V");

        const RectBoundary * rectBoundary = (RectBoundary *) boundary;
        int boundaryDimension = rectBoundary->minimums().size();



        double minimum [boundaryDimension];
        double maximum [boundaryDimension];

        for (int i = 0; i < boundaryDimension; i++) {
            minimum[i] = boundary->minimums().component(i);
            maximum[i] = boundary->maximums().component(i);

        }

        //-----------------------------------------------------------------------


        jdoubleArray min = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray max = (env)->NewDoubleArray(boundaryDimension);

        (env)->SetDoubleArrayRegion(min, 0, boundaryDimension, minimum);
        (env)->SetDoubleArrayRegion(max, 0, boundaryDimension, maximum);

        jobject minRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, min);
        jobject maxRealVector = (env)->NewObject(realVectorClass, realVectorConstructor, max);

        jobject boundary = (env)->NewObject(boundaryClass, boundaryConstructor, minRealVector, maxRealVector);

        (env)->DeleteLocalRef(min);
        (env)->DeleteLocalRef(max);
        (env)->DeleteLocalRef(minRealVector);
        (env)->DeleteLocalRef(maxRealVector);


        return boundary;

    }

    if (!strcmp(boundaryType, "Three_Phase_Boundary")) {

        const Three_Phase_Boundary * boundary = (const Three_Phase_Boundary *) RpNumerics::physicsVector_->at(0)->boundary();

        jclass isoRect2DBboundaryClass = env->FindClass("wave/util/IsoTriang2DBoundary");
        jmethodID isoTriang2DBoundaryConstructor = (env)->GetMethodID(isoRect2DBboundaryClass, "<init>",
                "(Lwave/util/RealVector;Lwave/util/RealVector;Lwave/util/RealVector;)V");

        int boundaryDimension = boundary->minimums().size();



        jdoubleArray A = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray B = (env)->NewDoubleArray(boundaryDimension);
        jdoubleArray C = (env)->NewDoubleArray(boundaryDimension);

        // Getting A,B and C
        double Anative [boundaryDimension];
        double Bnative [boundaryDimension];
        double Cnative [boundaryDimension];

        for (int i = 0; i < boundaryDimension; i++) {
            Anative[i] = boundary->getA().component(i);
            Bnative[i] = boundary->getB().component(i);
            Cnative[i] = boundary->getC().component(i);
        }


        //-----------------------------------------------------------------------


        (env)->SetDoubleArrayRegion(A, 0, boundaryDimension, Anative);
        (env)->SetDoubleArrayRegion(B, 0, boundaryDimension, Bnative);
        (env)->SetDoubleArrayRegion(C, 0, boundaryDimension, Cnative);

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

JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_clearCurvesCache(JNIEnv * env, jclass cls) {
    //    RpNumerics::clearCurveMap();
}

JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_removeCurve(JNIEnv * env, jclass cls, jint curveID) {
    //    RpNumerics::removeCurve(curveID);
}



