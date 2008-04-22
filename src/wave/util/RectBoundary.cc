/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RectBoundary.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RectBoundary.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


RectBoundary::RectBoundary(const RectBoundary & copy){
    
    minimums_= new RealVector(copy.minimums());
    maximums_= new RealVector(copy.maximums());
    
}

RectBoundary & RectBoundary::operator=(const RectBoundary & source){

    if (this==&source)
        return *this;
    
    delete minimums_;
    delete maximums_;
    
    minimums_= new RealVector(source.minimums());
    maximums_= new RealVector(source.maximums());
    
    return *this;
    
    
}

 RectBoundary::RectBoundary(const RealVector & minimums, const RealVector & maximums)
: minimums_(new RealVector(minimums)),
        maximums_(new RealVector(maximums)),
        size_(minimums.size()) {
}




RectBoundary::~RectBoundary(){
    delete minimums_;
    delete maximums_;
}



