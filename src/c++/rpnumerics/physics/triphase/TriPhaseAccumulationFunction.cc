#include "TriPhaseAccumulationFunction.h"

TriPhaseAccumulationFunction::TriPhaseAccumulationFunction(void) {
}

TriPhaseAccumulationFunction * TriPhaseAccumulationFunction::clone() const {
    return new TriPhaseAccumulationFunction(*this);
}

TriPhaseAccumulationFunction::~TriPhaseAccumulationFunction(void) {
}

int TriPhaseAccumulationFunction::jet(const WaveState &w, JetMatrix &M, int degree) const {

    IF_DEBUG
        cout << "Entrando em jet acc triphase: " << w.stateSpaceDim() << endl;
        cout << "Entrando em tamanho m: " << M.size() << endl;
    END_DEBUG
    if (degree >= 0) {
        for (int i = 0; i < w.stateSpaceDim(); i++) M(i, w(i));

      IF_DEBUG
          cout << "Passei por F" << endl;
      END_DEBUG

        if (degree >= 1) {
//            for (int i = 0; i < w.stateSpaceDim(); i++) {
//                for (int j = 0; j < w.stateSpaceDim(); j++) {
//                    M(i, j, 0);
//                    IF_DEBUG
//                        cout << "i: " << i << endl;
//                        cout << "j: " << j << endl;
//                    END_DEBUG
//                }
//
//                M(i, i, 1);
//            }
            M(0, 0, 1.0);
            M(0, 1, 0.0);
            M(1, 0, 0.0);
            M(1, 1, 1.0);
          IF_DEBUG
              cout << "Passei por J" << endl;
          END_DEBUG
            if (degree == 2) {
                for (int i = 0; i < w.stateSpaceDim(); i++) {
                    for (int j = 0; j < w.stateSpaceDim(); j++) {
                        for (int k = 0; k < w.stateSpaceDim(); k++)
                            M(i, j, k, 0.0);
                    }

                }
            }
            IF_DEBUG
                cout << "Passei por H" << endl;
            END_DEBUG
        }
    }
    return 2;
}











