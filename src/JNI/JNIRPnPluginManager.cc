/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRPnPluginManager.cc
 **/


//! Definition of JNIRPnPluginManger
/*!
 *
 * TODO:
 *
 * NOTE :
 *
 * @ingroup JNI
 */

#include "JNIDefs.h"
#include "RPnPluginManager.h"
#include "rpnumerics_plugin_RPnPluginManager.h"
#include <iostream>
#include <string>


/*
 * Class:     rpnumerics_plugin_RPnPluginManager
 * Method:    setPluginDir
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_plugin_RPnPluginManager_setPluginDir
(JNIEnv * env , jclass cls , jstring pluginDir){

    const char *pluginDirNative;
    pluginDirNative = env->GetStringUTFChars(pluginDir, NULL);
    
    RPnPluginManager::setPluginDir(string(pluginDirNative));
    
    
}



/*
 * Class:     rpnumerics_plugin_RPnPluginManager
 * Method:    configPlugin
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_plugin_RPnPluginManager_configPlugin
(JNIEnv *env , jclass cls, jstring pluginType, jstring libName, jstring className, jstring constructorMethod){

    const char *libNameNative;
    const char *classNameNative;
    const char *constructorMethodNative;
    const char *pluginTypeNative;

    libNameNative = env->GetStringUTFChars(libName, NULL);
    classNameNative = env->GetStringUTFChars(className, NULL);
    constructorMethodNative = env->GetStringUTFChars(constructorMethod, NULL);

    pluginTypeNative = env->GetStringUTFChars(pluginType, NULL);
    
    RPnPluginManager::configPlugin(string (pluginTypeNative),string(libNameNative), string(classNameNative), string(constructorMethodNative));
    
    
    
    
}



