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


std::vector<Parameter*> *RpNumerics::physicsParams_ = NULL;

std::map<string, AuxiliaryFunction *> *RpNumerics::physicsAuxFunctionsMap_ = NULL;

std::vector<SubPhysics*> * RpNumerics::physicsVector_ = new vector<SubPhysics *>();

map<string, bool (*)(const eigenpair&, const eigenpair&) > * RpNumerics::orderFunctionMap_ = NULL;

double RpNumerics::sigma = 0;

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    readNativePhysicsConfig
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_readNativePhysicsConfig
(JNIEnv * env, jclass cls) {


    jclass configurationClass = env->FindClass(CONFIGURATION_LOCATION);
    jclass physicsConfigurationClass = env->FindClass("rpn/configuration/PhysicsConfiguration");

    jclass curveConfigurationClass = env->FindClass("rpn/configuration/CurveConfiguration");





    jclass physicsConfigurationParamsClass = env->FindClass("rpn/configuration/PhysicsConfigurationParams");

    jclass parameterLeafClass = env->FindClass("rpn/configuration/ParameterLeaf");
    jclass parameterClass = env->FindClass("rpn/configuration/Parameter");
    jclass parameterCompositeClass = env->FindClass("rpn/configuration/ParameterComposite");


    jclass textParameterClass = env->FindClass("rpn/configuration/TextParameter");

    jmethodID textParameterConstructorID = env->GetMethodID(textParameterClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");


    jclass arrayListClass = env->FindClass("java/util/ArrayList");


    jclass rpnumericsClass = env->FindClass(RPNUMERICS_LOCATION);



    jmethodID getConfigurationMethodID = env->GetStaticMethodID(cls, "getConfiguration", "(Ljava/lang/String;)Lrpn/configuration/Configuration;");

    jmethodID setConfigurationMethodID = env->GetStaticMethodID(cls, "setConfiguration", "(Ljava/lang/String;Lrpn/configuration/Configuration;)V");


    jmethodID physicsConfigurationConstructorMethodID = env->GetMethodID(physicsConfigurationClass, "<init>", "(Ljava/lang/String;)V");
    
    
    jmethodID curveConfigurationConstructorMethodID = env->GetMethodID(curveConfigurationClass, "<init>", "(Ljava/lang/String;)V");
    
    
    
    
    jmethodID physicsConfigurationParamsConstructorMethodID = env->GetMethodID(physicsConfigurationParamsClass, "<init>", "(Ljava/lang/String;)V");


    jmethodID parameterLeafConstructorID = env->GetMethodID(parameterLeafClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
    jmethodID parameterCompositeConstructorID = env->GetMethodID(parameterCompositeClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");




    jmethodID parameterAddOptionID = env->GetMethodID(parameterClass, "addOption", "(Ljava/lang/String;)V");

    jmethodID addParameterMethodID = env->GetMethodID(configurationClass, "addParameter", "(Lrpn/configuration/Parameter;)V");

    jmethodID addAssociatedParameterMethodID = env->GetMethodID(parameterClass, "addAssociatedParameter", "(Lrpn/configuration/Parameter;)V");

    jmethodID addConfigurationMethodID = env->GetMethodID(configurationClass, "addConfiguration", "(Lrpn/configuration/Configuration;)V");


    jmethodID setParamValueMethodID = env->GetStaticMethodID(cls, "setParamValue", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");



    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");






    //WaveCurveCases

    jstring waveCurveName = env->NewStringUTF("wavecurve");
    jobject waveCurveConfiguration = env->CallStaticObjectMethod(cls, getConfigurationMethodID, waveCurveName);

    jstring paramName = env->NewStringUTF("origin");



    std::vector<int> type;
    std::vector<std::string> name;

    RpNumerics::physicsVector_->at(0)->wavecurvefactory()->list_of_initial_points(type, name);

    jstring waveCurveDefaultCaseValue = env->NewStringUTF(name.at(0).c_str());

    jobject parameterLeaf = env->NewObject(parameterLeafClass, parameterLeafConstructorID, paramName, waveCurveDefaultCaseValue);


    jobject waveCurveOptiosArray = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    for (int i = 0; i < name.size(); i++) {

        jstring waveCurveOptionName = env->NewStringUTF(name.at(i).c_str());


        env->CallVoidMethod(parameterLeaf, parameterAddOptionID, waveCurveOptionName);


    }



    env->CallVoidMethod(waveCurveConfiguration, addParameterMethodID, parameterLeaf);

    //--------------------------------------------------------------------------------------------------

    //Hugoniot Cases 



    jstring hugoniotName = env->NewStringUTF("hugoniotcurve");

    jobject hugoniotConfiguration = env->CallStaticObjectMethod(cls, getConfigurationMethodID, hugoniotName);

    jstring hugoniotParamName = env->NewStringUTF("method");


    vector<HugoniotCurve*> hugoniotMethodsVector;


    RpNumerics::physicsVector_->at(0)->list_of_Hugoniot_methods(hugoniotMethodsVector);


    jstring hugoniotDefaultParamName = env->NewStringUTF(hugoniotMethodsVector[0]->Hugoniot_info().c_str());


    jobject methodParameter = env->NewObject(parameterCompositeClass, parameterCompositeConstructorID, hugoniotParamName, hugoniotDefaultParamName);



    env->CallVoidMethod(hugoniotConfiguration, addParameterMethodID, methodParameter);



    for (int i = 0; i < hugoniotMethodsVector.size(); i++) {

        jstring hugoniotMethodName = env->NewStringUTF(hugoniotMethodsVector.at(i)->Hugoniot_info().c_str());

        env->CallVoidMethod(methodParameter, parameterAddOptionID, hugoniotMethodName);



    }


    //Hugoniot Cases



    for (int i = 0; i < hugoniotMethodsVector.size(); i++) {

        vector<int> types;
        vector<string> typeNames;


        hugoniotMethodsVector[i]->list_of_reference_points(types, typeNames);


        jstring hugoniotCaseName = env->NewStringUTF("case");

        jstring hugoniotDefaultCaseName = env->NewStringUTF(typeNames[0].c_str());

        jobject caseParameter = env->NewObject(parameterLeafClass, parameterLeafConstructorID, hugoniotCaseName, hugoniotDefaultCaseName);

        for (int j = 0; j < typeNames.size(); j++) {

            jstring paramOptionName = env->NewStringUTF(typeNames.at(j).c_str());

            env->CallVoidMethod(caseParameter, parameterAddOptionID, paramOptionName);


        }

        env->CallVoidMethod(methodParameter, addAssociatedParameterMethodID, caseParameter);

        if (i == 0) {
            env->CallVoidMethod(hugoniotConfiguration, addParameterMethodID, caseParameter);

        }




    }

    //-------------------------------------------------------------------------------------------



    //Transitional lines 

    jstring transitionalName = env->NewStringUTF("transitionalline");

    jobject transitionalConfiguration = env->CallStaticObjectMethod(cls, getConfigurationMethodID, transitionalName);

    jstring transitionalParamName = env->NewStringUTF("name");



    BifurcationCurve * bifurcation = RpNumerics::physicsVector_->at(0)->bifurcation_curve();

    if (bifurcation != NULL) {


        jstring transionalDefaultValue = env->NewStringUTF(name.at(0).c_str());

        jobject transitionalParameter = env->NewObject(parameterLeafClass, parameterLeafConstructorID, transitionalParamName, transionalDefaultValue);

        std::vector<int> type;
        std::vector<std::string> name;
        std::vector<void*> object;
        std::vector<double (*)(void*, const RealVector &) > function;

        bifurcation->list_of_secondary_bifurcation_curves(type, name, object, function);

        for (int i = 0; i < name.size(); i++) {

            jstring transionalOptionName = env->NewStringUTF(name[i].c_str());
            env->CallVoidMethod(transitionalParameter, parameterAddOptionID, transionalOptionName);


        }

        env->CallVoidMethod(transitionalConfiguration, addParameterMethodID, transitionalParameter);

    }
    //-----------------------------------------------------------------------------------------------------------------



    jstring physicsName = env->NewStringUTF(RpNumerics::physicsVector_->at(0)->info_subphysics().c_str());

    jobject physicsConfiguration = env->CallStaticObjectMethod(cls, getConfigurationMethodID, physicsName);

    //MainFunctions

    vector<Parameter *> parameters;
    RpNumerics::physicsVector_->at(0)->equation_parameter(parameters);

    jstring fluxFunctionName = env->NewStringUTF("fluxfunction");

    jobject fluxFunctionConfiguration = env->NewObject(physicsConfigurationParamsClass, physicsConfigurationParamsConstructorMethodID, fluxFunctionName);

    for (int j = 0; j < parameters.size(); j++) {

        Parameter * parameter = parameters.at(j);

        jstring parameterName = env->NewStringUTF(parameter->name().c_str());

        stringstream doubleStream;

        doubleStream << parameter->value();

        string doubleString;

        doubleStream >> doubleString;

        jstring parameterValue = env->NewStringUTF(doubleString.c_str());

        jobject jParameter = env->NewObject(textParameterClass, textParameterConstructorID, parameterName, parameterValue);

        env->CallObjectMethod(fluxFunctionConfiguration, addParameterMethodID, jParameter);


    }


    env->CallObjectMethod(physicsConfiguration, addConfigurationMethodID, fluxFunctionConfiguration);

    //AuxiliaryFunctions 


    vector<AuxiliaryFunction *> auxFuncVector;

    RpNumerics::physicsVector_->at(0)->auxiliary_functions(auxFuncVector);

    for (int i = 0; i < auxFuncVector.size(); i++) {

        AuxiliaryFunction * auxiliarFunction = auxFuncVector[i];

        jstring functionName = env->NewStringUTF(auxiliarFunction->info_auxiliary_function().c_str());

        jobject auxFunctionConfiguration = env->NewObject(physicsConfigurationParamsClass, physicsConfigurationParamsConstructorMethodID, functionName);

        vector<Parameter *> parameterVector;

        auxiliarFunction->parameter(parameterVector);


        for (int j = 0; j < parameterVector.size(); j++) {

            Parameter * parameter = parameterVector.at(j);

            jstring parameterName = env->NewStringUTF(parameter->name().c_str());

            stringstream doubleStream;

            doubleStream << parameter->value();

            string doubleString;

            doubleStream >> doubleString;

            jstring parameterValue = env->NewStringUTF(doubleString.c_str());

            jobject jParameter = env->NewObject(textParameterClass, textParameterConstructorID, parameterName, parameterValue);

            env->CallObjectMethod(auxFunctionConfiguration, addParameterMethodID, jParameter);


        }


        env->CallObjectMethod(physicsConfiguration, addConfigurationMethodID, auxFunctionConfiguration);


    }


    //Grid Resolution 


    cout << "Resolucao do grid" << RpNumerics::physicsVector_->at(0)->gridvalues()->grid_resolution << endl;

    const Boundary * boundary = RpNumerics::physicsVector_->at(0)->boundary();

    RealVector gridRes = RpNumerics::physicsVector_->at(0)->gridvalues()->grid_resolution;

    double nCelsX = (boundary->maximums()(0) - boundary->minimums()(0)) / gridRes(0);


    double nCelsY = (boundary->maximums()(1) - boundary->minimums()(1)) / gridRes(1);




    stringstream doubleStream;

    doubleStream << nCelsX << " " << nCelsY;

    string doubleString;

    doubleStream >> doubleString;


    jstring gridName = env->NewStringUTF("gridresolution");


    jstring gridParamName = env->NewStringUTF("resolution");

    jstring newResolutionString = env->NewStringUTF(doubleString.c_str());



    jobject resolutionConfiguration = env->CallStaticObjectMethod(cls, getConfigurationMethodID, gridName);


    if (resolutionConfiguration != NULL) {

        env->CallStaticVoidMethod(cls, setParamValueMethodID, gridName, gridParamName, newResolutionString);

    } else {

        jobject resConfiguration = env->NewObject(curveConfigurationClass, physicsConfigurationParamsConstructorMethodID, gridName);

        jobject resolutionParameter = env->NewObject(textParameterClass, textParameterConstructorID, gridParamName, newResolutionString);

        env->CallObjectMethod(resConfiguration, addParameterMethodID, resolutionParameter);
        
        env->CallStaticVoidMethod(cls, setConfigurationMethodID,gridName,resConfiguration);



    }








    cout << "x do grid " << nCelsX << " " << nCelsY << endl;



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getAuxFunctionNames
 * Signature: ()Ljava/util/List;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_RPNUMERICS_getAuxFunctionNames
(JNIEnv * env, jclass cls) {

    jclass arrayListClass = env->FindClass("java/util/ArrayList");

    jmethodID arrayListAddMethod = env->GetMethodID(arrayListClass, "add", "(Ljava/lang/Object;)Z");

    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass, "<init>", "()V");

    jobject auxFuncArrayList = env->NewObject(arrayListClass, arrayListConstructor, NULL);

    vector<AuxiliaryFunction *> auxFuncVector;

    RpNumerics::physicsVector_->at(0)->auxiliary_functions(auxFuncVector);

    for (int i = 0; i < auxFuncVector.size(); i++) {

        jstring functionName = env->NewStringUTF(auxFuncVector[i]->info_auxiliary_function().c_str());

        env->CallObjectMethod(auxFuncArrayList, arrayListAddMethod, functionName);

    }


    return auxFuncArrayList;

}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    setAuxFuntionParam
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setAuxFuntionParam
(JNIEnv * env, jclass cls, jstring auxFuncName, jstring auxParamName, jstring paramValue) {






    const char * auxFuncNameChar;

    auxFuncNameChar = env->GetStringUTFChars(auxFuncName, NULL);


    const char * auxFuncParam;

    auxFuncParam = env->GetStringUTFChars(auxParamName, NULL);


    const char * auxFuncParamValue;

    auxFuncParamValue = env->GetStringUTFChars(paramValue, NULL);


    string paramValueString(auxFuncParamValue);

    stringstream paramStream;

    paramStream << paramValueString;

    double paramDoubleValue;

    paramStream >> paramDoubleValue;

    string auxFuncNameString(auxFuncNameChar);

    AuxiliaryFunction * auxFunction = RpNumerics::physicsAuxFunctionsMap_->at(auxFuncNameString);

    vector<Parameter *> paramVector;

    auxFunction->parameter(paramVector);


    string paramNameString(auxFuncParam);






    for (int i = 0; i < paramVector.size(); i++) {

        if (paramVector[i]->name().compare(paramNameString) == 0) {
            //            cout<< "Funcao: "<< auxFuncNameString<<" parametro: "<< paramNameString<< "valor: "<< paramDoubleValue<<endl;
            paramVector[i]->value(paramDoubleValue);


        }

    }


    RpNumerics::physicsVector_->at(0)->gridvalues()->clear_computations();




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

    BifurcationCurve * bifurcation = RpNumerics::physicsVector_->at(0)->bifurcation_curve();

    if (bifurcation != NULL) {

        std::vector<int> type;
        std::vector<std::string> name;
        std::vector<void*> object;
        std::vector<double (*)(void*, const RealVector &) > function;



        bifurcation->list_of_secondary_bifurcation_curves(type, name, object, function);


        for (int i = 0; i < name.size(); i++) {


            jstring paramName = env->NewStringUTF(name[i].c_str());

            env->CallObjectMethod(namesArray, arrayListAddMethod, paramName);

        }
    }

    return namesArray;


}

void RpNumerics::fillPhysicsParams() {

    physicsParams_ = new vector<Parameter *>();

    physicsVector_->at(0)->equation_parameter(*physicsParams_);

    vector<AuxiliaryFunction *> auxFuncVector;

    physicsVector_->at(0)->auxiliary_functions(auxFuncVector);

    physicsAuxFunctionsMap_ = new map<string, AuxiliaryFunction *>();

    for (int i = 0; i < auxFuncVector.size(); i++) {

        physicsAuxFunctionsMap_->insert(std::pair<string, AuxiliaryFunction *>(auxFuncVector.at(i)->info_auxiliary_function(), auxFuncVector.at(i)));

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

//JNIEXPORT jobjectArray JNICALL Java_rpnumerics_RPNUMERICS_getAuxFunctionNames
//(JNIEnv * env, jclass cls) {
//
//    jclass stringClass = env->FindClass("Ljava/lang/String;");
//
//    jobjectArray auxFuncNamesArray = env->NewObjectArray(RpNumerics::physicsAuxFunctionsMap_->size(), stringClass, NULL);
//
//    int i = 0;
//    for (std::map < string, AuxiliaryFunction * >::iterator it = RpNumerics::physicsAuxFunctionsMap_->begin();
//            it != RpNumerics::physicsAuxFunctionsMap_->end(); ++it) {
//
//        jstring functionName = env->NewStringUTF(it->first.c_str());
//
//
//        env->SetObjectArrayElement(auxFuncNamesArray, i, functionName);
//
//        i++;
//
//    }
//
//
//    return auxFuncNamesArray;
//
//}

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
 * Method:    setPhysicsParams
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setPhysicsParams
(JNIEnv * env, jclass cls, jstring paramName, jstring paramValue) {


    const char * paramNativeValue;

    paramNativeValue = env->GetStringUTFChars(paramValue, NULL);


    const char * paramNativeName;

    paramNativeName = env->GetStringUTFChars(paramName, NULL);


    string paramNameString(paramNativeName);


    string paramValueString(paramNativeValue);

    stringstream paramStream;

    paramStream << paramValueString;

    double paramDoubleValue;

    paramStream >> paramDoubleValue;

    for (int i = 0; i < RpNumerics::physicsParams_->size(); i++) {

        if (RpNumerics::physicsParams_->at(i)->name().compare(paramNameString) == 0) {

            RpNumerics::physicsParams_->at(i)->value(paramDoubleValue);


        }



    }



    RpNumerics::physicsVector_->at(0)->gridvalues()->clear_computations();



}

/*
 * Class:     rpnumerics_RPNUMERICS
 * Method:    getPhysicsParam
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_rpnumerics_RPNUMERICS_getPhysicsParam
(JNIEnv * env, jclass cls, jint paramIndex) {

    Parameter * param = RpNumerics::physicsParams_->at(paramIndex);

    double paramValue = param->value();

    stringstream paramStream;

    paramStream << paramValue;

    jstring paramStringValue = env->NewStringUTF(paramStream.str().c_str());

    return paramStringValue;


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
 * Method:    setResolution
 * Signature: (Lwave/util/RealVector;Lwave/util/RealVector;Ljava/lang/String;[I)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_setResolution
(JNIEnv * env, jclass cls, jobject min, jobject max, jstring gridName, jintArray newResolution) {


    cout << "Chamando set resolution" << endl;



    jclass realVectorClass = env->FindClass(REALVECTOR_LOCATION);
    //
    jmethodID toDoubleMethodID = (env)->GetMethodID(realVectorClass, "toDouble", "()[D");
    //
    //
    int dimension = env->GetArrayLength(newResolution);
    //
    //    //    //min  processing
    //        jdoubleArray minLimit = (jdoubleArray) (env)->CallObjectMethod(min, toDoubleMethodID);
    //    //
    //        double minNativeArray[dimension];
    //        env->GetDoubleArrayRegion(minLimit, 0, dimension, minNativeArray);
    //    //    //max processing
    //    //
    //        jdoubleArray maxLimit = (jdoubleArray) (env)->CallObjectMethod(max, toDoubleMethodID);
    //    //
    //        double maxNativeArray[dimension];
    //    //
    //        env->GetDoubleArrayRegion(maxLimit, 0, dimension, maxNativeArray);
    //
    //
    //        Processing resolution
    vector<int>newResolutionVector;

    int tempResolutionArray[dimension];


    env->GetIntArrayRegion(newResolution, 0, dimension, tempResolutionArray);


    for (int i = 0; i < dimension; i++) {
        newResolutionVector.push_back(tempResolutionArray[i]);

    }
    //
    //    const char * gridNameNative = env->GetStringUTFChars(gridName, NULL);




    //    const Boundary * boundary = RpNumerics::getPhysics().getSubPhysics(0).getPreProcessedBoundary();



    const Boundary * boundary = RpNumerics::physicsVector_->at(0)->boundary();

    RpNumerics::physicsVector_->at(0)->gridvalues()->set_grid(boundary, boundary->minimums(), boundary->maximums(), newResolutionVector);


    //        grid->set_grid(boundary, boundary->minimums(), boundary->maximums(), newResolutionVector);

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
 * Method:    clean
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_rpnumerics_RPNUMERICS_clean(JNIEnv * env, jclass cls) {
    RpNumerics::clean();
}

void RpNumerics::clean() {


    clearCurveMap();



    for (int i = 0; i < physicsVector_->size(); i++) {
        delete physicsVector_->at(i);

    }


    delete waveCurveMap_;


    delete physicsParams_;



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


    string physicsStringName(physicsID);



    if (physicsStringName.compare("JDSubPhysics") == 0) {
        RpNumerics::physicsVector_->push_back(new JDSubPhysics());
    }

    if (physicsStringName.compare("Stone") == 0) {
        RpNumerics::physicsVector_->push_back(new StoneSubPhysics());
    }


    if (physicsStringName.compare("CoreyQuad") == 0) {

        RpNumerics::physicsVector_->push_back(new CoreyQuadSubPhysics());
    }

    if (physicsStringName.compare("Koval") == 0) {
        RpNumerics::physicsVector_->push_back(new KovalSubPhysics());
    }



    if (physicsStringName.compare("Brooks-Corey") == 0) {
        RpNumerics::physicsVector_->push_back(new Brooks_CoreySubPhysics());

    }

    if (physicsStringName.compare("DeadVolatileVolatileGasSubPhysics") == 0) {
        RpNumerics::physicsVector_->push_back(new DeadVolatileVolatileGasSubPhysics());
    }




    if (physicsStringName.compare("Quad2SubPhysics") == 0) {
        RpNumerics::physicsVector_->push_back(new Quad2SubPhysics());

    }




    if (physicsStringName.compare("Foam") == 0) {
        RpNumerics::physicsVector_->push_back(new FoamSubPhysics());

    }

    if (physicsStringName.compare("Sorbie") == 0) {
        RpNumerics::physicsVector_->push_back(new SorbieSubPhysics());

    }





    RpNumerics::waveCurveMap_ = new map<int, WaveCurve *>();
    RpNumerics::fillPhysicsParams();




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



