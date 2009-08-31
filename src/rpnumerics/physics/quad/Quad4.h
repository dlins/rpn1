/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad4_H
#define _Quad4_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "Physics.h"
#include "Quad4FluxFunction.h"
#include "Quad4AccumulationFunction.h"
#include "Quad4FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad4 : public Physics {
private:

    Quad4FluxFunction *fluxFunction_;
    Quad4AccumulationFunction * accumulationFunction_;

    Boundary * defaultBoundary();

    Boundary * boundary_;
    Space * space_;
    char * FLUX_ID;

    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;


public:
    Quad4(const Quad4FluxParams &);

    Quad4(const Quad4&);

    void fluxFunction(const FluxFunction &);

    virtual ~Quad4();

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

inline Physics * Quad4::clone()const {
    return new Quad4(*this);
}

inline void Quad4::fluxParams(const FluxParams & p) {

    Quad4FluxParams newparams(p.params());

    fluxFunction_->fluxParams(newparams);


}

inline void Quad4::accumulationParams(const AccumulationParams & p) {

    accumulationFunction_->accumulationParams(p);

}



inline const FluxParams & Quad4::params(void) const {
    return fluxFunction_->fluxParams();
}

inline const FluxFunction & Quad4::fluxFunction(void) const {
    return *fluxFunction_;
}

inline const AccumulationFunction & Quad4::accumulation() const {
    return *accumulationFunction_;
}



inline const Space & Quad4::domain(void) const {
        return *space_;
}

inline const Boundary & Quad4::boundary(void) const {
    return *boundary_;
}

inline void Quad4::boundary(const Boundary & boundary) {

    delete boundary_;
    boundary_ = boundary.clone();
}

inline Boundary * Quad4::defaultBoundary() {

    RealVector min(4);

    min.component(0) = -0.5;
    min.component(1) = -0.5;
    min.component(2) = -0.5;
    min.component(3) = -0.5; 

    RealVector max(4);

    max.component(0) = 0.5;
    max.component(1) = 0.5;
    max.component(2) = 0.5;
    max.component(3) = 0.5; 

    return new RectBoundary(min, max);

}


#endif //! _Quad4_H
