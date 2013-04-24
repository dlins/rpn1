/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriphaseFluxParams.h
 */

#ifndef _TriPhaseFluxParams_H
#define _TriPhaseFluxParams_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "FluxParams.h"
#include "RealVector.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class TriPhaseParams : public FluxParams {
private:

    double vel_;
    double muw_, muo_, mug_;
    double grw_, gro_, grg_;

    const static FluxParams & defaultParams();



public:

    //    const static FluxParams & DEFAULT_FLUX_PARAMS;

    TriPhaseParams();

    TriPhaseParams(const RealVector & params);

    double vel()const;

    double muw()const;

    double muo()const;

    double mug()const;

    double grw()const;

    double gro()const;

    double grg()const;

};

inline double TriPhaseParams::vel()const {
    return params().component(0);
}

inline double TriPhaseParams::muw()const {
    return
    params().component(1);
}

inline double TriPhaseParams::muo()const {
    return params().component(2);
}

inline double TriPhaseParams::mug()const {
    return params().component(3);
}

inline double TriPhaseParams::grw()const {
    return params().component(4);
}

inline double TriPhaseParams::gro()const {
    return params().component(5);
}

inline double TriPhaseParams::grg()const {
    return params().component(6);
}

inline const FluxParams & TriPhaseParams::defaultParams() {


    RealVector paramsVector(7);

    paramsVector.component(0) = 1;

    paramsVector.component(1) = (double) 1 / 3;
    paramsVector.component(2) = (double) 1 / 3;
    paramsVector.component(3) = (double) 1 / 3;
    paramsVector.component(4) = 0;
    paramsVector.component(5) = 0;
    paramsVector.component(6) = 0;


    TriPhaseParams * fluxParams = new TriPhaseParams(paramsVector); // TODO Index zero ???

    return *fluxParams;

}

#endif //! _TriPhaseFluxParams_H
