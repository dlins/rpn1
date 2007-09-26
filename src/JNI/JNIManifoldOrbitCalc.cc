#include "rpnumerics_ManifoldOrbitCalc.h"
#include "JNICurve.h"

/* Signature: (Lrpnumerics/StationaryPoint;Lrpnumerics/PhasePoint;I)Lrpnumerics/RpSolution;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_ManifoldOrbitCalc_calc  (JNIEnv * env, jobject obj, jobject stationaryPoint, jobject phasePoint, jint timeDirection){
    
    
    JNICurve *curveInstance = new JNICurve(env);
    
    return  curveInstance->manifoldConstructor(stationaryPoint, phasePoint, timeDirection);
    
    
}
