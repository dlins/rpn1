/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) JNIRPnDesktopPlotter.cc.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "rpn_RPnDesktopPlotter.h"
#include "RpNumerics.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */
JNIEXPORT void JNICALL Java_rpn_RPnDesktopPlotter_libraryCleanUp
        (JNIEnv * env , jobject obj){
    
    RpNumerics::clean();
    
    
}

//! Code comes here! daniel@impa.br

