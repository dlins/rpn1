#include "Quad2AccumulationFunction.h"

Quad2AccumulationFunction::Quad2AccumulationFunction(void) {
}

Quad2AccumulationFunction * Quad2AccumulationFunction::clone() const {
    return new Quad2AccumulationFunction(*this);
}



Quad2AccumulationFunction::~Quad2AccumulationFunction(void) {}

int Quad2AccumulationFunction::jet(const WaveState&, JetMatrix&, int) const {
    return 0;
}









