/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ConservationShockFlow.h
 */

#ifndef _ConservationShockFlow_H
#define _ConservationShockFlow_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RpFunction.h"
#include "FluxFunction.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */



/*! Definition of class ConservationShockFlow.
 * TODO:
 * NOTE :
 *
 * @ingroup rpnumerics
 */

#include "ShockFlowParams.h"
#include "WaveFlow.h"

class ConservationShockFlow : public WaveFlow {
private:


    ShockFlowParams * flowParams_;
    int flux(const WaveState &, JetMatrix &) const;
    int fluxDeriv(const WaveState &, JetMatrix &)const;
    int fluxDeriv2(const WaveState &, JetMatrix &)const;
    RealVector * fx0_;

    //    void updateXZeroTerms(); TODO Translate from Java

public:

    ConservationShockFlow(const ShockFlowParams &, const FluxFunction &);

    ConservationShockFlow(const ConservationShockFlow&);

    int jet(const WaveState &u, JetMatrix &m, int degree) const;

    const ShockFlowParams & getParams()const;

    virtual ~ConservationShockFlow();

    ConservationShockFlow * clone() const;

};

inline ConservationShockFlow * ConservationShockFlow::clone()const {
    return new ConservationShockFlow(*this);
}

inline const ShockFlowParams & ConservationShockFlow::getParams()const {
    return *flowParams_;
}

#endif //! _ConservationShockFlow_H
