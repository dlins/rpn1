/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyFluxParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CoreyParams.h"
#include <iostream>


using namespace std;
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//const double CoreyParams::DEFAULT_FLUX_PARAMS_ARRAY []= {1,1/3,1/3,1/3,0,0,0};


CoreyParams::CoreyParams():FluxParams(defaultParams()){}

CoreyParams::CoreyParams(const RealVector & params, int index):FluxParams(params),index_(index){}


//const FluxParams & CoreyParams::DEFAULT_FLUX_PARAMS=defaultParams();





