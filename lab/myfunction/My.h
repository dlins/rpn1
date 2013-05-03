/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) My.h
 **/

#ifndef _My_H
#define _My_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */

#include "Physics.h"
#include "MyFluxFunction.h"
#include "MyFluxParams.h"

class My :public Physics {
    
private:
    MyFluxParams * params_;
    MyFluxFunction * fluxFunction_;
    
    const char * DEFAULT_SIGMA;
    const char * DEFAULT_XZERO;
    
public:
    My(const MyFluxParams &);
    My(const My&);
    virtual ~My();
    
    const FluxParams & params(void) const;
    const FluxFunction & fluxFunction(void) const;
    
};

inline const FluxParams & My::params(void) const {
    return *params_;
}

inline const FluxFunction & My::fluxFunction(void) const {
    return *fluxFunction_;
}

#endif //! _My_H

