/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODEStopGenerator.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "LSODEStopGenerator.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

LSODEStopGenerator::LSODEStopGenerator(const int maxPoints):maxPoints_(maxPoints){}

LSODEStopGenerator * LSODEStopGenerator::clone()const  {return new LSODEStopGenerator(*this);}

LSODEStopGenerator::~LSODEStopGenerator(){}

void LSODEStopGenerator::setFunctionStatus(const int fStatus){ functionStatus_=fStatus;}

int LSODEStopGenerator::getFunctionStatus() const {return functionStatus_;}

int LSODEStopGenerator::totalPoints() const {return totalPoints_;}

void LSODEStopGenerator::increaseTotalPoints() { totalPoints_++;}

bool LSODEStopGenerator::getStatus() const {
    
    if ((functionStatus_ == SUCCESSFUL_PROCEDURE) && (totalPoints_ < maxPoints_))
        return true;
    return false;
}


//! Code comes here! daniel@impa.br

