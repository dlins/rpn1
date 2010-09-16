
#include "AccumulationParams.h"

AccumulationParams AccumulationParams::defaultParams(void) {
    return AccumulationParams();
}

AccumulationParams::~AccumulationParams(){ delete params_;}

AccumulationParams::AccumulationParams() :params_(new RealVector(2)){}

AccumulationParams::AccumulationParams(const RealVector & params) :params_(new RealVector(params)){}

AccumulationParams::AccumulationParams(const int size , double * data) :params_(new RealVector(size,data)){}



AccumulationParams::AccumulationParams(const AccumulationParams & copy){
 
    params_= new RealVector(copy.params());
    
}

