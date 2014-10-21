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

std::vector<WaveCurveConfig *> * RpNumerics::waveCurveConfigVector_ = NULL;


std::vector<HugoniotConfig *> * RpNumerics::hugoniotCasesVector_ = NULL;

std::vector<Parameter*> *RpNumerics::physicsParams_ = NULL;

std::map<string, AuxiliaryFunction *> *RpNumerics::physicsAuxFunctionsMap_ = NULL;

std::vector<SubPhysics*> * RpNumerics::physicsVector_ = new vector<SubPhysics *>();

map<string, bool (*)(const eigenpair&, const eigenpair&) > * RpNumerics::orderFunctionMap_ = NULL;

double RpNumerics::sigma = 0;

int RpNumerics::getHugoniotConfigIndex(const string & configName) {

    for (int i = 0; i < RpNumerics::hugoniotCasesVector_->size(); i++) {

        HugoniotConfig * config = RpNumerics::hugoniotCasesVector_->at(i);


        if (config->getName()->compare(configName) == 0) {
            return i;
        }

    }

}

HugoniotConfig * RpNumerics::getHugoniotConfig(const string & configName) {

    for (int i = 0; i < RpNumerics::hugoniotCasesVector_->size(); i++) {

        HugoniotConfig * config = RpNumerics::hugoniotCasesVector_->at(i);


        if (config->getName()->compare(configName) == 0) {
            return config;
        }

    }



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getTransisionalLinesNames
 * Signature: ()Ljava/util/ArrayList;
 */
JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getTransisionalLinesNames
(JNIEnv * env, jclass cls) {


    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");
    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");
    
    
    jobject namesArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    BifurcationCurve * bifurcation=    RpNumerics::physicsVector_->at(0)->bifurcation_curve();
    
    
    std::vector<int> type;
    std::vector<std::string> name;
    std::vector<void*> object;
    std::vector<double (*)(void*, const RealVector &)> function;
    
    
    
    bifurcation->list_of_secondary_bifurcation_curves(type,name,object,function);
    
    
    for (int i = 0; i < name.size(); i++) {

        
        jstring paramName = env->NewStringUTF(name[i].c_str());
        
        env->CallObjectMethod(namesArray, arrayListAddMethod, paramName);

    }
    
    return namesArray;


}

void RpNumerics::fillWaveCurveCases() {


    waveCurveConfigVector_ = new vector<WaveCurveConfig *>();

    std::vector<int> type;
    std::vector<std::string> name;

    RpNumerics::physicsVector_->at(0)->wavecurvefactory()->list_of_initial_points(type, name);

    WaveCurveConfig * config = new WaveCurveConfig(name, type);

    waveCurveConfigVector_->push_back(config);


}

void RpNumerics::fillPhysicsParams() {

    physicsParams_ = new vector<Parameter *>();

    physicsVector_->at(0)->equation_parameter(*physicsParams_);


    vector<AuxiliaryFunction *> auxFuncVector;

    physicsVector_->at(0)->auxiliary_functions(auxFuncVector);


    physicsAuxFunctionsMap_ = new map<string, AuxiliaryFunction *>();


    for (int i = 0; i < auxFuncVector.size(); i++) {


        physicsAuxFunctionsMap_->at(auxFuncVector.at(i)->info_auxiliary_function()) = auxFuncVector.at(i);


    }

}

void RpNumerics::fillHugoniotNames() {

    vector<HugoniotCurve*> hugoniotMethodsVector;

    hugoniotCasesVector_ = new vector<HugoniotConfig *>();


    physicsVector_->at(0)->list_of_Hugoniot_methods(hugoniotMethodsVector);

    for (int i = 0; i < hugoniotMethodsVector.size(); i++) {

        vector<int> types;
        vector<string> typeNames;


        hugoniotMethodsVector[i]->list_of_reference_points(types, typeNames);

        hugoniotCasesVector_->push_back(new HugoniotConfig(hugoniotMethodsVector[i]->Hugoniot_info(), types, typeNames));

        cout << hugoniotMethodsVector[i]->Hugoniot_info() << endl;

    }

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getPhysicsParamsNames
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getPhysicsParamsNames
(JNIEnv * env, jclass cls) {
    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jobjectArray paramsArray = env->NewObjectArray(RpNumerics::physicsParams_->size(), stringClass, NULL);

    for (int i = 0; i < RpNumerics::physicsParams_->size(); i++) {
        jstring paramName = env->NewStringUTF(RpNumerics::physicsParams_->at(i)->name().c_str());
        env->SetObjectArrayElement(paramsArray, i, paramName);
    }

    return paramsArray;


}

JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getAuxFunctionNames
(JNIEnv * env, jclass cls) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jobjectArray auxFuncNamesArray = env->NewObjectArray(RpNumerics::physicsAuxFunctionsMap_->size(), stringClass, NULL);

    int i = 0;
    for (std::map < string, AuxiliaryFunction * >::iterator it = RpNumerics::physicsAuxFunctionsMap_->begin();
            it != RpNumerics::physicsAuxFunctionsMap_->end(); ++it) {

        jstring functionName = env->NewStringUTF(it->first.c_str());


        env->SetObjectArrayElement(auxFuncNamesArray, i, functionName);

        i++;

    }


    return auxFuncNamesArray;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAuxParamsNames
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getAuxParamsNames
(JNIEnv * env, jclass cls, jstring auxFunctionName) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");



    const char * auxFuncNameChar;

    auxFuncNameChar = env->GetStringUTFChars(auxFunctionName, NULL);


    string auxFuncNameString(auxFuncNameChar);

    AuxiliaryFunction * auxFunction = RpNumerics::physicsAuxFunctionsMap_->at(auxFuncNameString);



    vector<Parameter *> parameterVector;


    auxFunction->parameter(parameterVector);


    jobjectArray paramsNamesArray = env->NewObjectArray(parameterVector.size(), stringClass, NULL);

    for (int i = 0; i < parameterVector.size(); i++) {
        jstring paramName = env->NewStringUTF(parameterVector.at(i)->name().c_str());
        env->SetObjectArrayElement(paramsNamesArray, i, paramName);
    }

    return paramsNamesArray;



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAuxParamsNames
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getAuxParamsNames
(JNIEnv *env, jclass cls) {
    jclass stringClass = env->FindClass("Ljava/lang/String;");
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAuxParamValue
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_getAuxParamValue
(JNIEnv * env, jclass cls, jstring auxFuncName, jstring paramName) {
    jclass stringClass = env->FindClass("Ljava/lang/String;");
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setAuxParamValue
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setAuxParamValue
(JNIEnv * env, jclass cls, jstring auxFuncName, jstring paramName, jstring paramValue) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");
}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setPhysicsParams
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setPhysicsParams
(JNIEnv * env, jclass cls, jint paramIndex, jstring paramValue) {


    jclass stringClass = env->FindClass("Ljava/lang/String;");

    const char * paramChar;

    paramChar = env->GetStringUTFChars(paramValue, NULL);


    string paramString(paramChar);

    stringstream paramStream;


    paramStream << paramString;

    double paramDoubleValue;

    paramStream >> paramDoubleValue;


    Parameter * param = RpNumerics::physicsParams_->at(paramIndex);


    param->value(paramDoubleValue);



    RpNumerics::physicsVector_->at(0)->gridvalues()->clear_computations();



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getPhysicsParam
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_getPhysicsParam
(JNIEnv * env, jclass cls, jint paramIndex) {


    jclass stringClass = env->FindClass("Ljava/lang/String;");

    Parameter * param = RpNumerics::physicsParams_->at(paramIndex);


    double paramValue = param->value();


    stringstream paramStream;


    paramStream << paramValue;


    jstring paramStringValue = env->NewStringUTF(paramStream.str().c_str());

    return paramStringValue;





}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getWaveCurveCaseNames
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getWaveCurveCaseNames
(JNIEnv * env, jclass cls) {


    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jobjectArray waveCurveCasesArray = env->NewObjectArray(RpNumerics::waveCurveConfigVector_->at(0)->getNames().size(), stringClass, NULL);


    for (int i = 0; i < RpNumerics::waveCurveConfigVector_->at(0)->getNames().size(); i++) {

        jstring caseName = env->NewStringUTF(RpNumerics::waveCurveConfigVector_->at(0)->getNames().at(i).c_str());
        env->SetObjectArrayElement(waveCurveCasesArray, i, caseName);

    }


    return waveCurveCasesArray;


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getHugoniotNames
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getHugoniotNames
(JNIEnv * env, jclass cls) {



    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jobjectArray hugoniotNames = env->NewObjectArray(RpNumerics::hugoniotCasesVector_->size(), stringClass, NULL);



    for (int i = 0; i < RpNumerics::hugoniotCasesVector_->size(); i++) {
        jstring jhugoniotName = env->NewStringUTF(RpNumerics::hugoniotCasesVector_->at(i)->getName()->c_str());

        env->SetObjectArrayElement(hugoniotNames, i, jhugoniotName);

    }
    return hugoniotNames;


}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getHugoniotCaseNames
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getHugoniotCaseNames
(JNIEnv * env, jclass cls, jstring hugoniotMethodName) {


    const char * hugoniotNameC;

    hugoniotNameC = env->GetStringUTFChars(hugoniotMethodName, NULL);

    jclass stringClass = env->FindClass("Ljava/lang/String;");


    string hugoniotName(hugoniotNameC);

    for (int i = 0; i < RpNumerics::hugoniotCasesVector_->size(); i++) {


        HugoniotConfig *config = RpNumerics::hugoniotCasesVector_->at(i);

        if (config->getName()->compare(hugoniotName) == 0) {
            vector<string> * caseNames = config->getCaseNames();

            jobjectArray caseNamesArray = env->NewObjectArray(caseNames->size(), stringClass, NULL);


            for (int j = 0; j < caseNames->size(); j++) {


                string caseName = caseNames->at(j);

                jstring jcaseName = env->NewStringUTF(caseName.c_str());

                env->SetObjectArrayElement(caseNamesArray, j, jcaseName);


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
 * Method:    getXLabel
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_getXLabel
(JNIEnv * env, jclass cls) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jstring xLabel = env->NewStringUTF(RpNumerics::physicsVector_->at(0)->xlabel().c_str());

    return xLabel;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getYLabel
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_getYLabel
(JNIEnv * env, jclass cls) {

    jclass stringClass = env->FindClass("Ljava/lang/String;");

    jstring yLabel = env->NewStringUTF(RpNumerics::physicsVector_->at(0)->ylabel().c_str());

    return yLabel;

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


    clearCurveMap();

    delete hugoniotCasesVector_;

    for (int i = 0; i < physicsVector_->size(); i++) {
        delete physicsVector_->at(i);

    }



    for (int i = 0; i < waveCurveConfigVector_->size(); i++) {
        delete waveCurveConfigVector_->at(i);

    }


    delete waveCurveMap_;

    delete waveCurveConfigVector_;

    delete physicsParams_;

    delete physicsAuxFunctionsMap_;

    delete physicsVector_;







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



        //        ThreePhaseFlowSubPhysics * subphysics = (ThreePhaseFlowSubPhysics*) new CoreyQuadSubPhysics();




        RpNumerics::physicsVector_->push_back(new CoreyQuadSubPhysics());
    }

    RpNumerics::fillHugoniotNames();
    RpNumerics::fillPhysicsParams();
    RpNumerics::fillWaveCurveCases();

    RpNumerics::waveCurveMap_ = new map<int, WaveCurve *> ();













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



