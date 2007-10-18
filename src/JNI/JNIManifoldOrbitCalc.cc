
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIManifoldOrbitCalc.cc
 **/




//! Definition of JNIManifoldOrbitCalc
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
*/


#include "rpnumerics_ManifoldOrbitCalc.h"
#include "JNIDefs.h"


JNIEXPORT jobject JNICALL Java_rpnumerics_ManifoldOrbitCalc_calc  (JNIEnv * env, jobject obj){
            
    // NULL calculations !!
    
    jobject stationaryPoint ;
    jobject phasePoint ;
    jint timeDirection;
    
    jclass   classManifold_= (env)->FindClass(MANIFOLD_LOCATION);
    
    jmethodID manifoldConstructor_=env->GetMethodID(classManifold_, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;Lrpnumerics/Orbit;I)V");
    
    
    return NULL;
    

    
    
}
