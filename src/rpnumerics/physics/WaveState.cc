#include "WaveState.h"

WaveState::WaveState(const int dim ) :
	coords_(new RealVector(dim))
{
}

WaveState::~WaveState()
{ 
	delete coords_;
}

double WaveState::operator() (const int comp) const 
{
	return coords_->component(comp);
}

double & WaveState::operator() (const int comp) 
{
	return coords_->component(comp);
}

