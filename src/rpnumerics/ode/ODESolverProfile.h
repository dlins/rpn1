#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"
//#include "ODEStopGenerator.h"


class ODESolverProfile{
    
private:
    
    RpFunction * function_;
    
public:
    
    
    ODESolverProfile(const RpFunction &);
    
    ODESolverProfile();
    
    virtual ~ODESolverProfile();
    
    ODESolverProfile(const ODESolverProfile &);
    
    ODESolverProfile & operator=(const ODESolverProfile &);
    
    const RpFunction & getFunction() const ;
    
    void setFunction(const RpFunction &);
    
};




#endif	/* _ODESolverProfile_H */

