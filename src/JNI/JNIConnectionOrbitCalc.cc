#include "rpnumerics_ConnectionOrbitCalc.h"
#include "JNICurve.h"
#include "RealVector.h"
#include "RK4BPMethod.h"
#include "ODESolution.h"

/* Signature: (Lrpnumerics/ManifoldOrbit;Lrpnumerics/ManifoldOrbit;)Lrpnumerics/RpSolution;
 */

JNIEXPORT jobject JNICALL Java_rpnumerics_ConnectionOrbitCalc_calc
        (JNIEnv *env , jobject obj , jobject manifoldA, jobject manifoldB){
    
    JNICurve * curveInstance= new JNICurve(env);
    
    jobject stationaryPointA, stationaryPointB, orbit;
    
//    RealVector nativeRealVector(2);
//    
//    nativeRealVector(0)=0.25;
//    nativeRealVector(1)=0.25;
//    
//    int timeDirection =0;
    
//    ShockRarefaction function ;
//    
//    ODESolverProfile  profile(2, 0.01, function);
//    
//    RK4BPMethod odeSolver(profile);
//    
//    ODESolution * solution = odeSolver.solve(nativeRealVector, timeDirection);
//    
//    orbit =  curveInstance->orbitConstructor(solution->getCoords(), timeDirection);
    
    
    
    return curveInstance->connectionOrbitConstructor(stationaryPointA, stationaryPointB, orbit);
    
    
}
