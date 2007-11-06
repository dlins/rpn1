/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveState.h
 **/


#ifndef _WaveState_H
#define	_WaveState_H

#include "RealVector.h"

class WaveState {
    
public:
    WaveState(const int dim);
    //TODO  mudar component para operator 
    
    virtual ~WaveState();
    
    double operator()(const int comp) const;
    double & operator()(const int comp);
    
    
protected:

    RealVector * coords_;
    
};





#endif	/* _WaveState_H */

