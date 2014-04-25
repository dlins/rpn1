/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) StoneNegativeFluxFunction.h
 */

#ifndef _StoneNegativeFluxFunction_H
#define _StoneNegativeFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


#include  "FluxFunction.h"
#include  "RealMatrix2.h"
#include "StoneNegativePermParams.h"
#include "StoneParams.h"
#include "StoneNegativePermeability.h"
#include <math.h>


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class StoneNegativeFluxFunction:public FluxFunction {
    
private:
    StoneNegativePermeability * perm_;

    
public:
    
    StoneNegativeFluxFunction(const StoneParams & params, const StoneNegativePermParams & permParams);

    virtual ~StoneNegativeFluxFunction();
    
    StoneNegativeFluxFunction(const StoneNegativeFluxFunction & );
    
    const StoneNegativePermeability & perm() const ;

    void setPermParams(const StoneNegativePermParams &);
    
    RpFunction * clone() const;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    
    
};

inline const StoneNegativePermeability & StoneNegativeFluxFunction::perm()const  { return *perm_; }

#endif //! _StoneFluxFunction_H
