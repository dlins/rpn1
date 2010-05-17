#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H

#include "RpFunction.h"
#include "WaveFlow.h"
#include "Boundary.h"

class ODESolverProfile {
private:


    WaveFlow * waveFlow_;
    int maxStepNumber_;


public:

    ODESolverProfile(const WaveFlow &);

    ODESolverProfile(const ODESolverProfile &);
    ODESolverProfile & operator=(const ODESolverProfile &);

    virtual ~ODESolverProfile();
    int maxStepNumber()const;
    void setMaxStepNumber(const int);
    WaveFlow & getFunction() const;
    void setFunction(const WaveFlow &);
  

   virtual const Boundary & boundary()const;



};


#endif	/* _ODESolverProfile_H */

