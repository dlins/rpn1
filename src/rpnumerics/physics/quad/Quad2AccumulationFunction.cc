#include "Quad2AccumulationFunction.h"

Quad2AccumulationFunction::Quad2AccumulationFunction(void) {
}

Quad2AccumulationFunction * Quad2AccumulationFunction::clone() const {
    return new Quad2AccumulationFunction(*this);
}

inline Quad2AccumulationFunction::Quad2AccumulationFunction(const AccumulationParams & params) :AccumulationFunction(params) {}

Quad2AccumulationFunction::~Quad2AccumulationFunction(void) {}

int Quad2AccumulationFunction::jet(const WaveState&, JetMatrix&, int){
    return 0;
}









