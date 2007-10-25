#include "QuadWaveState.h"

QuadWaveState::QuadWaveState(const int dim){ data_= new RealVector(dim);}


QuadWaveState::QuadWaveState(const int dim,sigma sig){
    
    data_= new RealVector(dim);   
    sigma_=sig;
    
}


QuadWaveState::~QuadWaveState(){delete data_;}



double QuadWaveState::operator ()(const int comp) const {return data_->component(comp);}

