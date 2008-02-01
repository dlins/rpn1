#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"
#include "ODEStopGenerator.h"


class ODESolverProfile{
    
private:
    
    RpFunction * function_;
    ODEStopGenerator * stopGenerator_;
    
    
public:
    
    ODESolverProfile(const RpFunction &, const ODEStopGenerator &);
    
    ODESolverProfile();
    
    virtual ~ODESolverProfile();
    
    ODESolverProfile(const ODESolverProfile &);
    
    ODESolverProfile & operator=(const ODESolverProfile &);
    
    void setStopGenerator(const ODEStopGenerator &);
    
    const ODEStopGenerator & getStopGenerator()const ;
    
    const RpFunction & getFunction() const ;
    
    void setFunction(const RpFunction &);
    
};




#endif	/* _ODESolverProfile_H */

