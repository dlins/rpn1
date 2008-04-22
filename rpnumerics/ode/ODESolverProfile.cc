#include "ODESolverProfile.h"

ODESolverProfile::ODESolverProfile(const RpFunction & function):function_(function.clone()){}

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
    
    
}

const RpFunction  & ODESolverProfile::getFunction() const {return *function_;}



void ODESolverProfile::setFunction(const RpFunction & function){
    delete function_;
    function_=function.clone();
}
