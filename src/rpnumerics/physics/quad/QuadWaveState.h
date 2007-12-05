/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) QuadWaveState.h
 **/



//! Definition of QuadWaveState
/*!
 *
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

#ifndef _QUAD2STATE_H
#define	_QUAD2STATE_H
#include "RealVector.h"
#include "WaveState.h"

typedef double sigma;


class QuadWaveState : public WaveState{
    
public:
    
    QuadWaveState(const int dim);
    
    QuadWaveState(const int dim, const sigma sig);
        
    virtual ~QuadWaveState();

    sigma getSigma() const ;
    
    void setSigma(const sigma sig) ;
    
private:
    
    sigma sigma_;
    
    
};




#endif	/* _QUAD2STATE_H */

