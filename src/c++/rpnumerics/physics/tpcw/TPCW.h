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
#include "Physics.h"
#include "Accum2Comp2PhasesAdimensionalized.h"
#include "Flux2Comp2PhasesAdimensionalized.h"
#include "RectBoundary.h"
#include "Multid.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */
//#define RP_... 1

class TPCW : public Physics {
private:

    Thermodynamics_SuperCO2_WaterAdimensionalized * TD;
    FracFlow2PhasesVerticalAdimensionalized *fv;
    FracFlow2PhasesHorizontalAdimensionalized * fh;
    Flux2Comp2PhasesAdimensionalized *flux;

    Accum2Comp2PhasesAdimensionalized_Params *accum_params;

    Accum2Comp2PhasesAdimensionalized *accum;

    Flux2Comp2PhasesAdimensionalized_Params *flux_params;


    Boundary * boundary_;

    Boundary * defaultBoundary();

    const char * ID_;

    Space * space_;

public:

    TPCW();

    TPCW(const TPCW &);

    Physics * clone()const;

    const AccumulationFunction & accumulation() const;

    const Boundary & boundary() const;

    void boundary(const Boundary & boundary);

    const FluxFunction & fluxFunction() const;

    void fluxParams(const FluxParams &);

    void accumulationParams(const AccumulationParams &);

    const Space & domain() const;

    const char * ID() const;

    double T2Theta(double)const;
    double Theta2T(double)const;

    virtual ~TPCW();
};

#endif //! _TPCW_H

