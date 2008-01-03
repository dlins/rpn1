#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"
#include "ODEStopGenerator.h"


class ODESolverProfile{
    
public:
    
    ODESolverProfile(const RpFunction &, const ODEStopGenerator &);
    
    ODESolverProfile();
    virtual ~ODESolverProfile();
    
    ODESolverProfile(const ODESolverProfile &);
    ODESolverProfile & operator=(const ODESolverProfile &);
    
    void setStopGenerator(const ODEStopGenerator &);
    
     ODEStopGenerator * getStopGenerator()const ;
    
     RpFunction * getFunction() const ;
    
protected:
    RpFunction * function_;
    ODEStopGenerator * stopGenerator_;
    
    
};

#endif	/* _ODESolverProfile_H */

