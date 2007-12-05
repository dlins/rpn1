#include "QuadWaveState.h"

inline sigma QuadWaveState::getSigma()const {return sigma_;}
inline void QuadWaveState::setSigma(const sigma sig ) { sigma_=sig;}

QuadWaveState::QuadWaveState(const int dim):WaveState(dim){}

QuadWaveState::QuadWaveState(const int dim,sigma sig):WaveState(dim),sigma_(sig){}

QuadWaveState::~QuadWaveState(){}


