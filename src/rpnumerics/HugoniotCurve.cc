#include "rpnumerics_HugoniotCurve.h"

/*
 * Class:     rpnumerics_HugoniotCurve
 * Method:    findSigma
 * Signature: (Lrpnumerics/PhasePoint;)D
 */

JNIEXPORT jdouble JNICALL Java_rpnumerics_HugoniotCurve_findSigma
        (JNIEnv * env , jobject obj, jobject phasePoint){
    
    printf ("Chamando find sigma\n");
    
    return 0.1 ; //Teste
    
}
