#include "QuadWaveState.h"


QuadWaveState::QuadWaveState(const int dim):WaveState(dim){}

QuadWaveState::QuadWaveState(const int dim,sigma sig):WaveState(dim),sigma_(sig){}

QuadWaveState::~QuadWaveState(){}


