/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2.h
 **/

#ifndef _Quad2_H
#define _Quad2_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "Physics.h"
#include "Quad2FluxFunction.h"
#include "Quad2AccumulationFunction.h"
#include "Quad2FluxParams.h"
#include "RectBoundary.h"
#include "Multid.h"
#include "Space.h"

class Quad2 :public Physics {
    
private:
    Quad2FluxParams * params_;
    Quad2FluxFunction *fluxFunction_;
    Quad2AccumulationFunction * accumulationFunction_;
    
    Boundary * defaultBoundary();
    
    Boundary * boundary_;

    
public:
    Quad2(const Quad2FluxParams &);
    
    Quad2(const Quad2& );
    
    virtual ~Quad2();
    
    const char * FLUX_ID;
    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;
    const char * ID(void);
    
    Physics * clone() const;
    
    const FluxParams & params(void) const;
    const FluxFunction * fluxFunction(void) const;
    const AccumulationFunction & accumulation() const;


    
    const Space & domain(void) const;
    const Boundary * boundary(void) const;
    void boundary(const Boundary & boundary);
};

inline Physics * Quad2::clone()const {
    return new Quad2(*this);
}

inline const FluxParams & Quad2::params(void) const {
    return *params_;
}

inline const FluxFunction * Quad2::fluxFunction(void) const {
    return fluxFunction_;
}


inline const AccumulationFunction & Quad2::accumulation() const {
    return *accumulationFunction_;
}

inline const Space & Quad2::domain(void) const {
    return Multid::PLANE;
}

inline const Boundary * Quad2::boundary(void) const {
    return boundary_;
}

inline void Quad2::boundary(const Boundary & boundary) {
    //TODO: not working properly - daniel@impa.br
    
    *boundary_ = boundary;
}

inline Boundary * Quad2::defaultBoundary(){
    
    RealVector min(2);
    
    min.component(0)=-0.5;
    min.component(1)=-0.5;
    
    RealVector max(2);
    
    max.component(0)=0.5;
    max.component(1)=0.5;
    
    return  new RectBoundary(min, max);
    
}


#endif //! _Quad2_H
