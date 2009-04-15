#include "ODESolverProfile.h"


ODESolverProfile::ODESolverProfile(){}

//TODO Implements !!!

//ODESolverProfile::ODESolverProfile(const ODESolverProfile & copy){ waveFlow_=new WaveFlow(copy.getFunction().fluxFunction());}
//
//ODESolverProfile & ODESolverProfile::operator=(const ODESolverProfile & source ){
//    
//    if (this==&source)
//        return *this;
//    waveFlow_=source.getFunction().clone();
//    return *this;
//}


ODESolverProfile::~ODESolverProfile(){

    delete waveFlow_;
    
}

const WaveFlow  & ODESolverProfile::getFunction() const {return *waveFlow_;}

void  ODESolverProfile::setFunction(const WaveFlow & function){//TODO Create a clone method in WaveFlow class !!!
    
}


