#include "Quad2AccumulationFunction.h"

Quad2AccumulationFunction::Quad2AccumulationFunction(void) {
}

Quad2AccumulationFunction * Quad2AccumulationFunction::clone() const {
    return new Quad2AccumulationFunction(*this);
}
Quad2AccumulationFunction::~Quad2AccumulationFunction(void) {}

int Quad2AccumulationFunction::jet(const WaveState&w, JetMatrix&M, int degree) const {
    if (degree >= 0) {
        for (int i = 0; i < w.stateSpaceDim(); i++) M(i, w(i));
        if (degree >= 1) {
            M(0, 0, 1.0);
            M(0, 1, 0.0);
            M(1, 0, 0.0);
            M(1, 1, 1.0);
            if (degree == 2) {
                for (int i = 0; i < w.stateSpaceDim(); i++) {
                    for (int j = 0; j < w.stateSpaceDim(); j++) {
                        for (int k = 0; k < w.stateSpaceDim(); k++)
                            M(i, j, k, 0.0);
                    }

                }
            }
        }
    }
    return 2;
}









