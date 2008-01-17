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

LSODEStopGenerator::LSODEStopGenerator(const int maxPoints):totalPoints_(0), maxPoints_(maxPoints), functionStatus_(SUCCESSFUL_PROCEDURE){}

LSODEStopGenerator::LSODEStopGenerator(const LSODEStopGenerator & copy) {
    
    functionStatus_=copy.getFunctionStatus();
    
    totalPoints_=copy.totalPoints();
    
    maxPoints_=copy.getMaxPoints();
}

LSODEStopGenerator * LSODEStopGenerator::clone()const  {return new LSODEStopGenerator(*this);}

LSODEStopGenerator::~LSODEStopGenerator(){}

void LSODEStopGenerator::setFunctionStatus(const int fStatus){ functionStatus_=fStatus;}

int LSODEStopGenerator::getFunctionStatus() const {return functionStatus_;}

int LSODEStopGenerator::totalPoints() const {return totalPoints_;}

int LSODEStopGenerator::getMaxPoints() const {return maxPoints_;}

void LSODEStopGenerator::increaseTotalPoints() { totalPoints_++;}

bool LSODEStopGenerator::getStatus() const {
    
//    cout <<"Checando status da funcao: "<<functionStatus_<<endl;
    
    if ((functionStatus_ == SUCCESSFUL_PROCEDURE) && (totalPoints_ < maxPoints_))
        
//    if (totalPoints_ < maxPoints_)
        return true;
    return false;
}


//! Code comes here! daniel@impa.br

