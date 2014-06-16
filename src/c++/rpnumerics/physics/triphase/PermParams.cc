/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) PermParams.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include <string.h>

#include "PermParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


const double PermParams::DEFAULT_LW = 0.;
const double PermParams::DEFAULT_LOW = 0.;
const double PermParams::DEFAULT_LG = 0.;
const double PermParams::DEFAULT_LOG = 0.;
const double PermParams::DEFAULT_CNW = 0.;
const double PermParams::DEFAULT_CNO = 0.;
const double PermParams::DEFAULT_CNG = 0.;
const double PermParams::DEFAULT_EPSL = 0.;

PermParams::PermParams(double lw, double low, double lg, double log, double cnw, double cno, double cng, double epsl) : data_(new RealVector(8)) {


    data_->component(0) = lw;
    data_->component(1) = low;
    data_->component(2) = lg;
    data_->component(3) = log;
    data_->component(4) = cnw;
    data_->component(5) = cno;
    data_->component(6) = cng;
    data_->component(7) = epsl;


}

int PermParams::size()const {
    return data_->size();
}

void PermParams::setValue(int index, double value) {
    data_->component(index) = value;
}

double PermParams::getValue(int index) const{
    return data_->component(index);
}

PermParams::PermParams(const PermParams & copy):data_(new RealVector(copy.size())){
    
    
    for (int i = 0; i < copy.size(); i++) {
        data_->component(i)=copy.getValue(i);


    }

    
    
    
}

PermParams::PermParams():data_(new RealVector(8)) {
    reset();
}


PermParams::~PermParams() {
    delete data_;
}


void PermParams::reset() {
    data_->component(0) = DEFAULT_LW;
    data_->component(1) = DEFAULT_LOW;
    data_->component(2) = DEFAULT_LG;
    data_->component(3) = DEFAULT_LOG;
    data_->component(4) = DEFAULT_CNW;
    data_->component(5) = DEFAULT_CNO;
    data_->component(6) = DEFAULT_CNG;
    data_->component(7) = DEFAULT_EPSL;
}



