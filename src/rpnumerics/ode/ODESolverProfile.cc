#include "ODESolverProfile.h"

ODESolverProfile::ODESolverProfile(const WaveFlow &flow) : waveFlow_(flow.clone()) {
}

ODESolverProfile::ODESolverProfile(const ODESolverProfile & copy) : waveFlow_(copy.getFunction().clone()) {
}

ODESolverProfile & ODESolverProfile::operator=(const ODESolverProfile & source) {

    if (this == &source)
        return *this;
    setFunction(source.getFunction());
    return *this;
}


ODESolverProfile::~ODESolverProfile() {
    delete waveFlow_;
}

const WaveFlow & ODESolverProfile::getFunction() const {
    return *waveFlow_;
}

void ODESolverProfile::setFunction(const WaveFlow & function) {
    delete waveFlow_;
    waveFlow_ = function.clone();
}


