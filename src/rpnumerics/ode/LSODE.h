/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) LSODE.h
 */

#ifndef _LSODE_H
#define _LSODE_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "ODESolver.h"
#include "LSODEProfile.h"


/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class LSODE:public ODESolver {
    
private:
    
    LSODEProfile * profile_;
    const RpFunction * rpFunction_;
    int function (int *,double *,double *,double *);
    
public:
    
    LSODE(const LSODEProfile &);
     ~LSODE();
    
    
    ODESolution & solve(const RealVector & , int ) const;
    ODESolverProfile & getProfile() ;
    void setProfile(const LSODEProfile &);
    
    
};

#endif //! _LSODE_H
