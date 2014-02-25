#include "Cub2AccumulationFunction.h"

Cub2AccumulationFunction::Cub2AccumulationFunction(void) {
}

Cub2AccumulationFunction * Cub2AccumulationFunction::clone() const {
    return new Cub2AccumulationFunction(*this);
}
Cub2AccumulationFunction::~Cub2AccumulationFunction(void) {}

int Cub2AccumulationFunction::jet(const WaveState&w, JetMatrix&M, int degree) const {
    if (degree >= 0) {
        for (int i = 0; i < w.stateSpaceDim(); i++) M.set(i, w(i));
        if (degree >= 1) {
            M.set(0, 0, 1.0);
            M.set(0, 1, 0.0);
            M.set(1, 0, 0.0);
            M.set(1, 1, 1.0);
            if (degree == 2) {
                for (int i = 0; i < w.stateSpaceDim(); i++) {
                    for (int j = 0; j < w.stateSpaceDim(); j++) {
                        for (int k = 0; k < w.stateSpaceDim(); k++)
                            M.set(i, j, k, 0.0);
                    }

                }
            }
        }
    }
    return 2;
}









