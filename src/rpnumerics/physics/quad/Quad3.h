/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad3.h
 **/

#ifndef _Quad3_H
#define _Quad3_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "Physics.h"
#include "Quad3FluxFunction.h"
#include "Quad3AccumulationFunction.h"
#include "Quad3FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad3 : public Physics {
private:

    Quad3FluxFunction *fluxFunction_;
    Quad3AccumulationFunction * accumulationFunction_;

    Boundary * defaultBoundary();

    Boundary * boundary_;
    Space * space_;
    char * FLUX_ID;

    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;


public:
    Quad3(const Quad3FluxParams &);

    Quad3(const Quad3&);

    void fluxFunction(const FluxFunction &);

    virtual ~Quad3();

    const char * ID(void)const;

    Physics * clone() const;

    const FluxParams & params(void) const;
    void fluxParams(const FluxParams &);
    void accumulationParams(const AccumulationParams &);

    const FluxFunction & fluxFunction(void) const;
    const AccumulationFunction & accumulation() const;

    const Space & domain(void) const;
    const Boundary & boundary(void) const;
    void boundary(const Boundary & boundary);
};

inline Physics * Quad3::clone()const {
    return new Quad3(*this);
}

inline void Quad3::fluxParams(const FluxParams & p) {

    Quad3FluxParams newparams(p.params());

    fluxFunction_->fluxParams(newparams);


}

inline void Quad3::accumulationParams(const AccumulationParams & p) {

    accumulationFunction_->accumulationParams(p);

}



inline const FluxParams & Quad3::params(void) const {
    return fluxFunction_->fluxParams();
}

inline const FluxFunction & Quad3::fluxFunction(void) const {
    return *fluxFunction_;
}

inline const AccumulationFunction & Quad3::accumulation() const {
    return *accumulationFunction_;
}



inline const Space & Quad3::domain(void) const {
        return *space_;
}

inline const Boundary & Quad3::boundary(void) const {
    return *boundary_;
}

inline void Quad3::boundary(const Boundary & boundary) {

    delete boundary_;
    boundary_ = boundary.clone();
}

inline Boundary * Quad3::defaultBoundary() {

    RealVector min(3);

    min.component(0) = -0.5;
    min.component(1) = -0.5;
    min.component(2) = -0.5;

    RealVector max(3);

    max.component(0) = 0.5;
    max.component(1) = 0.5;
    max.component(2) = 0.5;

    return new RectBoundary(min, max);

}


#endif //! _Quad3_H
