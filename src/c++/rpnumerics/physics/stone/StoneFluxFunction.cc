/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneFluxFunction.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "StoneFluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

StoneFluxFunction::StoneFluxFunction(const StoneParams & params, const StonePermParams & permParams ) : FluxFunction(FluxParams(params.params())) {
    perm_ = new StonePermeability(permParams);
}

StoneFluxFunction::StoneFluxFunction(const StoneFluxFunction & copy) : FluxFunction(copy.fluxParams()) {
    perm_ = new StonePermeability(copy.perm());
}

RpFunction * StoneFluxFunction::clone() const {
    return new StoneFluxFunction(*this);
}

StoneFluxFunction::~StoneFluxFunction() {
    delete perm_;
}

int StoneFluxFunction::jet(const WaveState &U, JetMatrix &M, int degree) const {



    return 0;

}




