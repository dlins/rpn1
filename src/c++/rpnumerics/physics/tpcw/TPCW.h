/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TPCW.h
 */

#ifndef _TPCW_H
#define _TPCW_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "SubPhysics.h"
#include "Accum2Comp2PhasesAdimensionalized.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Physics.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TPCW : public SubPhysics {
private:

    Thermodynamics_SuperCO2_WaterAdimensionalized * TD;
    FracFlow2PhasesVerticalAdimensionalized *fv;
    FracFlow2PhasesHorizontalAdimensionalized * fh;

public:

    TPCW(const FluxFunction &, const AccumulationFunction &,  const Thermodynamics_SuperCO2_WaterAdimensionalized &);

    TPCW(const TPCW &);

    SubPhysics * clone()const;

    Boundary * defaultBoundary()const;

    double T2Theta(double)const;
    double Theta2T(double)const;

    virtual ~TPCW();
};

#endif //! _TPCW_H

