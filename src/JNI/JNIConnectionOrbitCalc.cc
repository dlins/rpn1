#include "rpnumerics_ConnectionOrbitCalc.h"
#include "JNIDefs.h"

/* Signature: (Lrpnumerics/ManifoldOrbit;Lrpnumerics/ManifoldOrbit;)Lrpnumerics/RpSolution;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_ConnectionOrbitCalc_calc
        (JNIEnv *env , jobject obj , jobject manifoldA, jobject manifoldB){
    
    
    jobject stationaryPointA, stationaryPointB, orbit;
    
    jclass  classConnectionOrbit=env->FindClass(CONNECTIONORBIT_LOCATION);
    
    jmethodID  connectionOrbitConstructor=env->GetMethodID(classConnectionOrbit, "<init>", "(Lrpnumerics/StationaryPoint;Lrpnumerics/StationaryPoint;Lrpnumerics/Orbit;)V");
    
    env->DeleteLocalRef(classConnectionOrbit);
    
    return env->NewObject(classConnectionOrbit, connectionOrbitConstructor, stationaryPointA, stationaryPointB, orbit);
    
}
