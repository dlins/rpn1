#include "TriPhaseAccumulationFunction.h"
#include "Debug.h"

TriPhaseAccumulationFunction::TriPhaseAccumulationFunction(void) {
}

TriPhaseAccumulationFunction * TriPhaseAccumulationFunction::clone() const {
    return new TriPhaseAccumulationFunction(*this);
}

TriPhaseAccumulationFunction::~TriPhaseAccumulationFunction(void) {
}

int TriPhaseAccumulationFunction::jet(const WaveState &w, JetMatrix &M, int degree) const {

//    if ( Debug::get_debug_level() == 5 ) {
//        //cout << "Entrando em jet acc triphase: " << w.stateSpaceDim() << endl;
//        //cout << "Entrando em tamanho m: " << M.size() << endl;
//    }
    if (degree >= 0) {
        for (int i = 0; i < w.stateSpaceDim(); i++) M.set(i, w(i));

     

        if (degree >= 1) {
//            for (int i = 0; i < w.stateSpaceDim(); i++) {
//                for (int j = 0; j < w.stateSpaceDim(); j++) {
//                    M(i, j, 0);
//                    if ( Debug::get_debug_level() == 5 ) {
//                        //cout << "i: " << i << endl;
//                        //cout << "j: " << j << endl;
//                    }
//                }
//
//                M(i, i, 1);
//            }
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











