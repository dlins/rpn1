/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Cub2FluxFunction.h
 **/

#ifndef _Cub2FluxFunction_H
#define _Cub2FluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxFunction.h"
#include "Cub2FluxParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class Cub2FluxFunction : public FluxFunction {
    
public:
    Cub2FluxFunction(const Cub2FluxParams &);
    
    Cub2FluxFunction(const Cub2FluxFunction &);
    
    virtual ~Cub2FluxFunction(void);
    
    Cub2FluxFunction * clone() const ;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    
};


#endif //! _Cub2FluxFunction_H
