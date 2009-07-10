#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"
#include "WaveFlow.h"


class ODESolverProfile{
    
private:
    

    WaveFlow * waveFlow_;
    
public:

    ODESolverProfile(const WaveFlow &);
    
    ODESolverProfile(const ODESolverProfile &);
    ODESolverProfile & operator=(const ODESolverProfile &);
    
    virtual ~ODESolverProfile();
    virtual int maxStepNumber()const =0;
     WaveFlow & getFunction() const ;
    void setFunction (const WaveFlow & );
    
    
};




#endif	/* _ODESolverProfile_H */

