#include "KovalAccumulationFunction.h"

KovalAccumulationFunction::KovalAccumulationFunction(Parameter *phi): AccumulationFunction(){
    phi_parameter_ = phi;
}

KovalAccumulationFunction::~KovalAccumulationFunction(){
}

int KovalAccumulationFunction::jet(const WaveState &state, JetMatrix &a, int degree) const {
    a.resize(2);

    double phi = phi_parameter_->value();
    double sw = state(0);
    double so = state(1);

    if (degree >= 0){
        a.set(0, phi*sw);
        a.set(1, phi*so);

        if (degree >= 1){
            a.set(0, 0, phi);
            a.set(0, 1, 0.0);
            a.set(1, 0, 0.0);
            a.set(1, 1, phi);

            if (degree >= 2){
                a.set(0, 0, 0, 0.0);
                a.set(0, 0, 1, 0.0);
                a.set(0, 1, 0, 0.0);
                a.set(0, 1, 1, 0.0);

                a.set(1, 0, 0, 0.0);
                a.set(1, 0, 1, 0.0);
                a.set(1, 1, 0, 0.0);
                a.set(1, 1, 1, 0.0);
            }
        }
    }

    return degree;
}
    
