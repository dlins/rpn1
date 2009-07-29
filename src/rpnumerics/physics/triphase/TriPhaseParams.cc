/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriphaseFluxParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "TriPhaseParams.h"
#include <iostream>


using namespace std;
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//const double TriPhaseParams::DEFAULT_FLUX_PARAMS_ARRAY []= {1,1/3,1/3,1/3,0,0,0};


TriPhaseParams::TriPhaseParams():FluxParams(defaultParams()){}

TriPhaseParams::TriPhaseParams(const RealVector & params, int index):FluxParams(params),index_(index){}


//const FluxParams & TriPhaseParams::DEFAULT_FLUX_PARAMS=defaultParams();





