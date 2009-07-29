/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CapilParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "CapilParams.h"
#include  <iostream>

using namespace std;
/*
 * ---------------------------------------------------------------
 * Definitions:
 */



CapilParams::CapilParams( double acw, double acg, double lcw, double lcg ) {

    acw_ = acw;
    acg_ = acg;
    lcw_ = lcw;
    lcg_ = lcg;
}

CapilParams::~CapilParams(){
    
    
}

//
// Accessors/Mutators
//
double CapilParams::acg( )const  { return acg_; }
double CapilParams::acw( )const  { return acw_; }
double CapilParams::lcg( )const  { return lcg_; }
double CapilParams::lcw( )const  { return lcw_; }


CapilParams::CapilParams(const CapilParams & copy  ) {

    acw_ = copy.acw();
    acg_ = copy.acg();
    lcw_ = copy.lcw();
    lcg_ = copy.lcg();
}


void CapilParams::reset( ) {
    acw_ = 1.0;
    acg_ =1.0;//DEFAULT_ACG;
    lcw_ = 0.5;//DEFAULT_LCW;
    lcg_ = 0.5;//DEFAULT_LCG;
}


