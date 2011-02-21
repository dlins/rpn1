#include "StoneAccumulation.h"

StoneAccumulation::StoneAccumulation(){}

StoneAccumulation::~StoneAccumulation(){}

int StoneAccumulation::jet(const WaveState &w, JetMatrix &m, int degree) const {
    if (degree >= 0){
        m(0, w(0));
        m(1, w(1));

        if (degree >= 1){
            m(0, 0, 1.0);
            m(0, 1, 0.0);
            m(1, 0, 0.0);
            m(1, 1, 1.0);

            if (degree == 2){
                m(0, 0, 0, 0.0);
                m(0, 0, 1, 0.0);
                m(0, 1, 0, 0.0);
                m(0, 1, 1, 0.0);

                m(1, 0, 0, 0.0);
                m(1, 0, 1, 0.0);
                m(1, 1, 0, 0.0);
                m(1, 1, 1, 0.0);
            }
        }
    }
    return 2;
}

RpFunction * StoneAccumulation::clone() const {
    return new StoneAccumulation(*this);
}
