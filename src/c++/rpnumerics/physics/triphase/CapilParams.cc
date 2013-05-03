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



CapilParams::CapilParams(double acw, double acg, double lcw, double lcg) : data_(new RealVector(4)) {


    data_->component(0) = acw;
    data_->component(1) = acg;
    data_->component(2) = lcw;
    data_->component(3) = lcg;
    //    
    //    acw_ = acw;
    //    acg_ = acg;
    //    lcw_ = lcw;
    //    lcg_ = lcg;
}

CapilParams::~CapilParams() {

    delete data_;
}

void CapilParams::setParam(int index, double value) {
    data_->component(index) = value;
}

double CapilParams::getParam(int index) {
    return data_->component(index);
}

//
// Accessors/Mutators
//

double CapilParams::acg()const {
    return data_->component(1);
}

double CapilParams::acw()const {
    return data_->component(0);
}

double CapilParams::lcg()const {
    return data_->component(3);
}

double CapilParams::lcw()const {
    return data_->component(2);
}

CapilParams::CapilParams(const CapilParams & copy) : data_(new RealVector(*copy.data_)) {

    //    acw_ = copy.acw();
    //    acg_ = copy.acg();
    //    lcw_ = copy.lcw();
    //    lcg_ = copy.lcg();
}

int CapilParams::size() {
    return data_->size();
}

CapilParams::CapilParams() : data_(new RealVector(4)) {
    reset();
}

void CapilParams::reset() {

    data_->component(0) = 1.0;
    data_->component(1) = 1.0;
    data_->component(2) = 0.5;
    data_->component(3) = 0.5;

    //    acw_ = 1.0;
    //    acg_ =1.0;//DEFAULT_ACG;
    //    lcw_ = 0.5;//DEFAULT_LCW;
    //    lcg_ = 0.5;//DEFAULT_LCG;
}


