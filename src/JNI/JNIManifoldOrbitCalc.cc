#include "rpnumerics_ManifoldOrbitCalc.h"
#include "JNIDefs.h"

/* Signature: (Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;I)Lrpnumerics/RpSolution;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_ManifoldOrbitCalc_calc  (JNIEnv * env, jobject obj){
            
    // NULL calculations !!
    
    jobject stationaryPoint ;
    jobject phasePoint ;
    jint timeDirection;
    
    jclass   classManifold_= (env)->FindClass(MANIFOLD_LOCATION);
    
    jmethodID manifoldConstructor_=env->GetMethodID(classManifold_, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;Lrpnumerics/Orbit;I)V");
    
    
    return NULL;
    
//    return env->NewObject(classManifold_, manifoldConstructor_, stationaryPoint, phasePoint, timeDirection);
    
    
}
