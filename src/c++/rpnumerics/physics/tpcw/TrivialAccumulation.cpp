#include "TrivialAccumulation.h"

TrivialAccumulation::TrivialAccumulation(const TrivialAccumulation &a){
}

TrivialAccumulation::TrivialAccumulation(const TrivialAccumulation_Params &param){
}

TrivialAccumulation * TrivialAccumulation::clone() const {
    return new TrivialAccumulation(*this);
}

TrivialAccumulation::~TrivialAccumulation(){
}

int TrivialAccumulation::jet(const WaveState &w, JetMatrix &m, int degree) const{
    if (degree >= 0){
        for (int i = 0; i < 3; i++) m(i, w(i));

        if (degree >= 1){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++) m(i, j, 0.0);
                m(i, i, 1.0);
            }

            if (degree == 2){
                for (int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        for (int k = 0; k < 3; k++) m(i, j, k, 0.0);
                    }
                }
            }
        }
    }
    return 2;
}

