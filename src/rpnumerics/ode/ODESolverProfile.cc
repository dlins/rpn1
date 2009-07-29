#include "ODESolverProfile.h"

ODESolverProfile::ODESolverProfile(const WaveFlow &flow) : waveFlow_(flow.clone()) {
    maxStepNumber_=10000;
}

ODESolverProfile::ODESolverProfile(const ODESolverProfile & copy) : waveFlow_(copy.getFunction().clone()) {
    setMaxStepNumber(copy.maxStepNumber());
}

ODESolverProfile & ODESolverProfile::operator=(const ODESolverProfile & source) {

    if (this == &source)
        return *this;
    setFunction(source.getFunction());
    setMaxStepNumber(source.maxStepNumber());
    return *this;
}


ODESolverProfile::~ODESolverProfile() {
    delete waveFlow_;
}

 WaveFlow & ODESolverProfile::getFunction() const {
    return *waveFlow_;
}

void ODESolverProfile::setFunction(const WaveFlow & function) {
    delete waveFlow_;
    waveFlow_ = function.clone();
}

int ODESolverProfile::maxStepNumber()const {
    return maxStepNumber_;
}

void ODESolverProfile::setMaxStepNumber(const int maxStepNumber) {
    maxStepNumber_ = maxStepNumber;
}







