#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"


class ODESolverProfile{
    
public:
    
    ODESolverProfile(const RpFunction &);
    ODESolverProfile();
    virtual ~ODESolverProfile();
    
    ODESolverProfile(const ODESolverProfile &);
    ODESolverProfile & operator=(const ODESolverProfile &);
    
    RpFunction * getFunction() const ;
    
private:
    
     RpFunction * function_;
    
    
};

#endif	/* _ODESolverProfile_H */

