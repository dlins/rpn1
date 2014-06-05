/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Triphase.h
 */

#ifndef _Triphase_H
#define _Triphase_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "IsoTriang2DBoundary.h"
#include  "CapilParams.h"
#include  "PermParams.h"
#include "Double_Contact.h"
#include "HugoniotContinuation2D2D.h"
#include "ViscosityParams.h"
#include "TriPhaseParams.h"
#include  "TriPhaseFluxFunction.h"
#include "TriPhaseAccumulationFunction.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TriPhase : public SubPhysics {
private:

    FluxFunction * fluxFunction_;
    Boundary * boundary_;
    AccumulationFunction * accFunction_;



public:

    TriPhase(const TriPhaseParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);
    TriPhase();
    TriPhase(const TriPhase &);

    virtual ~TriPhase();
    
    vector<double> *  getParams();
    
    void setParams(vector<string>);

    SubPhysics * clone()const;


    Boundary * defaultBoundary()const;

};

#endif //! _Triphase_H
