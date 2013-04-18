/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveFlow.cc
 */

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

WaveFlow::WaveFlow(const FluxFunction & flux) : fluxFunction_((FluxFunction *) flux.clone()) {

}

WaveFlow::~WaveFlow() {
    delete fluxFunction_;
}

