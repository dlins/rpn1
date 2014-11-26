/* IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) MyWaveState.h
 **/

#ifndef _MYSTATE_H
#define	_MYSTATE_H
#include "RealVector.h"
#include "WaveState.h"

typedef double sigma;

class MyWaveState : public WaveState {
    
public:
    
    MyWaveState(const int dim);
    MyWaveState(const int dim, const sigma sig);
    MyWaveState(const RealVector & vector);
    MyWaveState(const RealVector & vector, const sigma sig);
        
    virtual ~MyWaveState();

    sigma getSigma() const;
    
    void setSigma(const sigma sig);
    
private:
    
    sigma sigma_;

};

inline sigma MyWaveState::getSigma()const {return sigma_;}
inline void MyWaveState::setSigma(const sigma sig ) {sigma_=sig;}


#endif	/* _MYSTATE_H */

