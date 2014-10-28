#include "FoamViscosity.h"

FoamViscosity::FoamViscosity(Parameter *mug): mug_parameter(mug){
}

FoamViscosity::~FoamViscosity(){
}

int FoamViscosity::gas_viscosity_jet(const WaveState &w, int degree, JetMatrix &mug_jet){
    mug_jet.resize(2);

    if (degree >= 0){
        double mug0 = mug_parameter->value();
        double mug  = mug0*(1.0 /* + ... */);

        mug_jet.set(0, mug);

        if (degree >= 1){
            mug_jet.set(0, 0, 0.0);
            mug_jet.set(0, 1, 0.0);

            if (degree >= 2){
                mug_jet.set(0, 0, 0, 0.0);
                mug_jet.set(0, 1, 0, 0.0);
                mug_jet.set(1, 0, 0, 0.0);
                mug_jet.set(1, 1, 0, 0.0);
            }
        }
    }

    return VISCOSITY_OK;
}

