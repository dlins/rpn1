/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) TriphaseFluxFunction.h
 */

#ifndef _TriphaseFluxFunction_H
#define _TriphaseFluxFunction_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */


#include  "FluxFunction.h"
#include  "RealMatrix2.h"
#include "CapilParams.h"
#include "PermParams.h"
#include "TriPhaseParams.h"
#include "Capillarity.h"
#include "Permeability.h"
#include "ViscosityParams.h"
#include <math.h>


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class TriPhaseFluxFunction:public FluxFunction {
    
private:
    
    Capillarity * capil_;
    Permeability * perm_;
    ViscosityParams * viscParams_;
    
public:
    
    TriPhaseFluxFunction(const TriPhaseParams & params, const PermParams & permParams, const CapilParams & capilParams, const ViscosityParams & viscParams);
    
    virtual ~TriPhaseFluxFunction();
    
    TriPhaseFluxFunction(const TriPhaseFluxFunction & );
    
    Permeability & perm() const  ;
    
     Capillarity  & capil()const ;
    
    ViscosityParams & visc() const;
    
    void setPermParams(const PermParams &);
    
    RpFunction * clone() const;
    
    int jet(const WaveState &u, JetMatrix &m, int degree) const;
    
    void balance(const RealVector  &, RealMatrix2 & );
    
    void viscosity(const RealVector & , RealMatrix2 & );
    
//
// Methods
//
    
    
    
};

inline Permeability & TriPhaseFluxFunction::perm() const  { return *perm_; }
inline  Capillarity & TriPhaseFluxFunction::capil()const  { return *capil_; }
inline ViscosityParams & TriPhaseFluxFunction::visc()const { return *viscParams_;}

#endif //! _TriphaseFluxFunction_H
