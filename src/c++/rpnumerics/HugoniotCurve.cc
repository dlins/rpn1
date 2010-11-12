/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) HugoniotCurve.cc
 **/



//! Definition of HugoniotCurve
/*!
	
TODO:
	
NOTE : 

@ingroup JNI
 */




#include "HugoniotCurve.h"

HugoniotCurve::HugoniotCurve(const RealVector & xZero,const vector<HugoniotSegment> & segmentsList) : segmentsList_(new vector<HugoniotSegment>()),xZero_(new RealVector(xZero)) {

    * segmentsList_ = segmentsList;

}

HugoniotCurve::~HugoniotCurve() {
    delete segmentsList_;
    delete xZero_;
}

HugoniotCurve::HugoniotCurve(const HugoniotCurve & copy) : segmentsList_(new vector<HugoniotSegment>()),xZero_(new RealVector(copy.getXZero())) {
    *segmentsList_=copy.segments();
}

const vector<HugoniotSegment> & HugoniotCurve::segments() const {
    return * segmentsList_;
}

const RealVector & HugoniotCurve::getXZero()const {
    return *xZero_;
}