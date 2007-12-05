
#include "AccumulationParams.h"

AccumulationParams AccumulationParams::defaultParams(void)
{
	return AccumulationParams();
}


inline AccumulationParams::AccumulationParams() :params_(params()){}

inline AccumulationParams::AccumulationParams(const RealVector & params) :params_(params){}

inline AccumulationParams::~AccumulationParams(){}

inline const RealVector & AccumulationParams::params(void) const
{
	return params_;
}

inline const double AccumulationParams::component(int index) const
{
	return params_(index);
}

inline void AccumulationParams::params(int size, double * coords)
{
	RealVector p_(size, coords);
	params_ = p_;

}

inline void AccumulationParams::params(const RealVector & params)
{
	params_ = params;
}

inline void AccumulationParams::component(int index, double value)
{
	params_(index) = value;
}
