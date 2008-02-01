#include "ODESolverProfile.h"


ODESolverProfile::ODESolverProfile(const RpFunction & function, const ODEStopGenerator  &sGenerator):function_(function.clone()), stopGenerator_(sGenerator.clone()){
    
    
}
ODESolverProfile::ODESolverProfile(){}

ODESolverProfile::ODESolverProfile(const ODESolverProfile & copy){ function_=copy.getFunction().clone();}

ODESolverProfile & ODESolverProfile::operator=(const ODESolverProfile & source ){
    
    if (this==&source)
        return *this;
    function_=source.getFunction().clone();
    return *this;
}


ODESolverProfile::~ODESolverProfile(){
    
    delete function_; 
    delete stopGenerator_;
    
}

const RpFunction  & ODESolverProfile::getFunction() const {return *function_;}
const ODEStopGenerator & ODESolverProfile::getStopGenerator()const {return *stopGenerator_;}

void ODESolverProfile::setFunction(const RpFunction & function){
    delete function_;
    function_=function.clone();
}



void ODESolverProfile::setStopGenerator(const ODEStopGenerator & sGenerator){
    delete stopGenerator_;
    stopGenerator_ = sGenerator.clone();
}


