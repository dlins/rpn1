/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ViscosityParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ViscosityParams.h"
#include <iostream>

using namespace std;

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



ViscosityParams::ViscosityParams(double eps) : epsl_(eps) {
}
ViscosityParams::ViscosityParams():epsl_(DEFAULT_EPS){
    
    
    
}

ViscosityParams:: ~ViscosityParams(){}

