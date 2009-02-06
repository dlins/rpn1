#include "TriPhaseAccumulationFunction.h"

TriPhaseAccumulationFunction::TriPhaseAccumulationFunction(void) {
}

TriPhaseAccumulationFunction * TriPhaseAccumulationFunction::clone() const {
    return new TriPhaseAccumulationFunction(*this);
}



TriPhaseAccumulationFunction::~TriPhaseAccumulationFunction(void) {}

int TriPhaseAccumulationFunction::jet(const WaveState&, JetMatrix&, int) const {
    return 0;
}









