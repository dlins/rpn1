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

    cout <<"Chamando setPluginDir"<<"\n";
    const char *pluginDirNative;
    pluginDirNative = env->GetStringUTFChars(pluginDir, NULL);
    
    RPnPluginManager::setPluginDir(string(pluginDirNative));
    
    
}

/*
 * Class:     rpnumerics_plugin_RPnPluginManager
 * Method:    addClass
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_rpnumerics_plugin_RPnPluginManager_addClass
(JNIEnv *env , jclass cls , jstring libName, jstring className, jstring constructorMethod) {

    cout << "Chamando addClass" << "\n";
    const char *libNameNative;
    const char *classNameNative;
    const char *constructorMethodNative;


    libNameNative = env->GetStringUTFChars(libName, NULL);
    classNameNative = env->GetStringUTFChars(className, NULL);
    constructorMethodNative = env->GetStringUTFChars(constructorMethod, NULL);

    RPnPluginManager::addClass(string(libNameNative), string(classNameNative), string(constructorMethodNative));
    
    
    
    
}



