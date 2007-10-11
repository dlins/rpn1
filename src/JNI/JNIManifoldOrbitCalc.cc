#include "rpnumerics_ManifoldOrbitCalc.h"
#include "JNICurve.h"

/* Signature: (Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;I)Lrpnumerics/RpSolution;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_ManifoldOrbitCalc_calc  (JNIEnv * env, jobject obj, jobject stationaryPoint, jobject phasePoint, jint timeDirection){
    
    jclass   classManifold_= (env)->FindClass(MANIFOLD_LOCATION);
    
    jmethodID manifoldConstructor_=env->GetMethodID(classManifold_, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;Lrpnumerics/Orbit;I)V");
    
    return env->NewObject(classManifold_, manifoldConstructor_, stationaryPoint, phasePoint, timeDirection);
    
    
}
