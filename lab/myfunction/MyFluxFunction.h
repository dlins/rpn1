/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) MyFluxFunction.h
 **/

#ifndef _MyFluxFunction_H
#define _MyFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxFunction.h"
#include "MyFluxParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class MyFluxFunction : public FluxFunction {
    
public:
    MyFluxFunction(const MyFluxParams &);
    
    MyFluxFunction(const MyFluxFunction &);
    
    virtual ~MyFluxFunction(void);
    
    MyFluxFunction * clone() const ;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    
};


#endif //! _MyFluxFunction_H
