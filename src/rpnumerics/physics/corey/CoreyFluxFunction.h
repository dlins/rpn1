/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) CoreyFluxFunction.h
 */

#ifndef _CoreyFluxFunction_H
#define _CoreyFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


#include  "FluxFunction.h"
#include  "RealMatrix2.h"
#include "CapilParams.h"
#include "PermParams.h"
#include "CoreyParams.h"
#include "Capillarity.h"
#include "Permeability.h"
#include "ViscosityParams.h"
#include <math.h>


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class CoreyFluxFunction:public FluxFunction {
    
private:
    
    Capillarity * capil_;
    Permeability * perm_;
    ViscosityParams * viscParams_;
    
public:
    
    CoreyFluxFunction(const CoreyParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);
    
    virtual ~CoreyFluxFunction();
    
    CoreyFluxFunction(const CoreyFluxFunction & );
    
    const Permeability & perm() const ;
    
    const Capillarity  & capil()const ;
    
    const ViscosityParams & visc() const;
    
    RpFunction * clone() const;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    void balance(const RealVector  &, RealMatrix2 & );
    
    void viscosity(const RealVector & , RealMatrix2 & );
    
//
// Methods
//
    
    
    
};

inline const Permeability & CoreyFluxFunction::perm()const  { return *perm_; }
inline const Capillarity & CoreyFluxFunction::capil()const  { return *capil_; }
inline const ViscosityParams & CoreyFluxFunction::visc()const { return *viscParams_;}

#endif //! _CoreyFluxFunction_H
