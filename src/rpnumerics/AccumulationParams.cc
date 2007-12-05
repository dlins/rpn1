
#include "AccumulationParams.h"

AccumulationParams AccumulationParams::defaultParams(void) {
    return AccumulationParams();
}


inline AccumulationParams::AccumulationParams() :params_(new RealVector(2)){}

inline AccumulationParams::AccumulationParams(const RealVector & params) :params_(new RealVector(params)){}

inline AccumulationParams::~AccumulationParams(){}

inline const RealVector & AccumulationParams::params(void) const {
    return *params_;
}

inline const double AccumulationParams::component(int index) const {
    return params_->component(index);
}

inline void AccumulationParams::params(int size, double * coords) {
    delete params_;
    params_ = new RealVector(size, coords);
    
}

inline void AccumulationParams::params(const RealVector & params) {
    delete params_;
    params_ = new RealVector(params);
}

inline void AccumulationParams::component(int index, double value) {
    params_->component(index) = value;
}
