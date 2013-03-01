
/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) GenericFluxFunction.cc
 **/

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "GenericFluxFunction.h"
#include "GenericFluxCalc.h"


GenericFluxFunction::GenericFluxFunction(const GenericFluxParams & params) : FluxFunction(params) {
}


GenericFluxFunction::GenericFluxFunction(const GenericFluxFunction & copy):FluxFunction(copy.fluxParams()) {
}


GenericFluxFunction * GenericFluxFunction::clone() const {
    return new GenericFluxFunction(*this);
}


GenericFluxFunction::~GenericFluxFunction(void) {
}


int GenericFluxFunction::jet(const WaveState & x, JetMatrix & y, int degree = 2) const {

    int n_comps = y.n_comps();
    int size_v  = n_comps * (1 + n_comps * (1 + n_comps));
    double v[size_v];

    calc_jet(size_v, v);

    JetMatrix J(degree, n_comps, v);
    y = J;

    return 0;
}
