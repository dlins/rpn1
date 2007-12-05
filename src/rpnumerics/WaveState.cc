/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveState.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveState.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

inline int WaveState::stateSpace() const {
    
    return coords_->size();
}

WaveState & WaveState::operator =(const WaveState & state){
    
    if (this==&state)
        return *this;
    
    delete coords_;
    
    int i;
    
    coords_ = new RealVector(state.stateSpace());
    
    for (i=0;i < coords_->size();i++){
        coords_->component(i)=state.operator ()(i);
    }
    
    return *this;
    
    
}


WaveState::WaveState(const int dim ):coords_(new RealVector(dim)) {
}

WaveState::~WaveState() {
    delete coords_;
}

double WaveState::operator() (const int comp) const {
    return coords_->component(comp);
}

double & WaveState::operator () (const int comp) {
    return coords_->component(comp);
}

