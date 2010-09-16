#include "CoreyAccumulationFunction.h"

CoreyAccumulationFunction::CoreyAccumulationFunction(void) {
}

CoreyAccumulationFunction * CoreyAccumulationFunction::clone() const {
    return new CoreyAccumulationFunction(*this);
}



CoreyAccumulationFunction::~CoreyAccumulationFunction(void) {}

int CoreyAccumulationFunction::jet(const WaveState&, JetMatrix&, int) const {
    return 0;
}









