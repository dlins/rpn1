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
#include "RectBoundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

//LSODEStopGenerator::LSODEStopGenerator(const int maxPoints):totalPoints_(0), maxPoints_(maxPoints), functionStatus_(SUCCESSFUL_PROCEDURE){
//}

LSODEStopGenerator::LSODEStopGenerator(const LSODEProfile & profile){
    profile_=new LSODEProfile(profile);
}
    



LSODEStopGenerator::LSODEStopGenerator(const LSODEStopGenerator & copy) {
    
    functionStatus_=copy.getFunctionStatus();
    
    totalPoints_=copy.totalPoints();
    
    maxPoints_=copy.getMaxPoints();
}

LSODEStopGenerator * LSODEStopGenerator::clone()const  {return new LSODEStopGenerator(*this);}

LSODEStopGenerator::~LSODEStopGenerator(){delete profile_;}

void LSODEStopGenerator::setFunctionStatus(const int fStatus){ functionStatus_=fStatus;}

int LSODEStopGenerator::getFunctionStatus() const {return functionStatus_;}

int LSODEStopGenerator::totalPoints() const {return totalPoints_;}

int LSODEStopGenerator::getMaxPoints() const {return maxPoints_;}

void LSODEStopGenerator::increaseTotalPoints() { totalPoints_++;}



bool LSODEStopGenerator::check(const RealVector & point) const {
    

    RealVector min(2);

    min.component(0) = -0.5;
    min.component(1) = -0.5;

    RealVector max(2);

    max.component(0) = 0.5;
    max.component(1) = 0.5;

    RectBoundary boundary(min, max);

    return boundary.inside(point);
    
//    return profile_->boundary().inside(point);
    
}




//! Code comes here! daniel@impa.br

