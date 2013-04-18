/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) Quad2FluxFunction.h
 **/

#ifndef _Quad2FluxFunction_H
#define _Quad2FluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "FluxFunction.h"
#include "Quad2FluxParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class Quad2FluxFunction : public FluxFunction {
public:
    Quad2FluxFunction(const Quad2FluxParams &);
    
    Quad2FluxFunction(const Quad2FluxFunction &);

    virtual ~Quad2FluxFunction(void);

    Quad2FluxFunction * clone() const;

    int jet(const WaveState &u, JetMatrix &m, int degree) const;


};


#endif //! _Quad2FluxFunction_H
