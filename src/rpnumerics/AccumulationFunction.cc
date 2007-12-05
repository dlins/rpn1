#include "AccumulationFunction.h"

AccumulationFunction::AccumulationFunction(void) : params_(AccumulationParams()) { }

inline AccumulationFunction::AccumulationFunction(const AccumulationParams & params) :	params_(params){}

AccumulationFunction::~AccumulationFunction(void){}

inline void AccumulationFunction::accumulationParams(const AccumulationParams & params){params_ = params;}

inline const AccumulationParams & AccumulationFunction::accumulationParams(void){return params_;}
