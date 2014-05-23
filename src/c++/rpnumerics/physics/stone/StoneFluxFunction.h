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

    double grw, grg, gro;
    double muw, mug, muo;
    double vel;
    
public:
    
    StoneFluxFunction(const StoneParams & params, const StonePermParams & permParams);
    //StoneFluxFunction();
    virtual ~StoneFluxFunction();
    
    StoneFluxFunction(const StoneFluxFunction & );
    
    const StonePermeability & perm() const ;
    
    RpFunction * clone() const;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
      void setPermParams(const StonePermParams &);
    
//
// Methods
//
    
    
    
};

inline const StonePermeability & StoneFluxFunction::perm()const  { return *perm_; }

#endif //! _StoneFluxFunction_H
