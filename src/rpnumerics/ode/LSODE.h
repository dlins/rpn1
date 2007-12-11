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


extern "C"{
    
    void  lsode_(int (*)(int *, double *, double *, double *), int *, double *, double *, double *,
            int *, double *, double *, int *, int *, int *, double *, int *,
            int *, int *, int(*)(int *, double *, double *, int *, int *, double *, int *), int *, int*, double*);
}

class LSODE:public ODESolver {
    
    
    
    
private:
    
    const LSODEProfile * profile_;
    
    const RpFunction * rpFunction_;
    
    int function(int *, double *, double *, double *);
    
public:
    
    LSODE(const LSODEProfile &);
    ~LSODE();
    
    ODESolution & solve(const RealVector & , int ) const;
    const ODESolverProfile & getProfile() const ;
    void setProfile(const LSODEProfile &);
    
    
};

#endif //! _LSODE_H
