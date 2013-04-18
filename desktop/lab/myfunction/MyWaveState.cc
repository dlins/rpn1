#include "MyWaveState.h"


MyWaveState::MyWaveState(const int dim) :
	WaveState(dim){}

MyWaveState::MyWaveState(const int dim,sigma sig) :
	WaveState(dim),sigma_(sig){}

MyWaveState::MyWaveState(const RealVector & vector) :
	WaveState(vector){}

MyWaveState::MyWaveState(const RealVector & vector, const sigma sig) :
	WaveState(vector),sigma_(sig){}


MyWaveState::~MyWaveState(){}


