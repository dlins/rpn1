/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneFluxParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StoneParams.h"
#include <iostream>


using namespace std;
/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//const double StoneParams::DEFAULT_FLUX_PARAMS_ARRAY []= {1,1/3,1/3,1/3,0,0,0};


StoneParams::StoneParams():FluxParams(defaultParams()){}

StoneParams::StoneParams(const RealVector & params):FluxParams(params){

}


//const FluxParams & StoneParams::DEFAULT_FLUX_PARAMS=defaultParams();





