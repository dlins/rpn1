/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneFluxFunction.h
 */

#ifndef _StoneFluxFunction_H
#define _StoneFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


#include  "FluxFunction.h"
#include  "RealMatrix2.h"
#include "StonePermParams.h"
#include "StoneParams.h"
#include "StonePermeability.h"
#include <math.h>


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StoneFluxFunction:public FluxFunction {
    
private:
    StonePermeability * perm_;

    
public:
    
    StoneFluxFunction(const StoneParams & params, const StonePermParams & permParams);

    virtual ~StoneFluxFunction();
    
    StoneFluxFunction(const StoneFluxFunction & );
    
    const StonePermeability & perm() const ;

    void setPermParams(const StonePermParams &);
    
    RpFunction * clone() const;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    
    
};

inline const StonePermeability & StoneFluxFunction::perm()const  { return *perm_; }

#endif //! _StoneFluxFunction_H
