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
    
    LSODEProfile * profile_;
    
    static  const RpFunction * rpFunction_;
    
    static int function(int *, double *, double *, double *);
    
    static   int jacrarefaction(int *neq, double *t, double *y, int *ml, int *mu, double *pd, int *nrpd);//int *nparam,  double *param);
    
    
    
    int solver(int (*)(int *, double *, double *, double *), int *neq, double *y, double *t, double *tout,
            int *itol, double *rtol, double *atol, int *itask, int *istate, int *iopt, double *rwork, int *lrw,
            int *iwork, int *liw, int(*)(int *, double *, double *, int *, int *, double *, int *),
            int *mf, int *nparam, double *param) const;
    
public:
    
    LSODE(const LSODEProfile &);
    ~LSODE();
    
    ODESolution & solve(const RealVector & , int ) const;
    const ODESolverProfile & getProfile() const ;
    void setProfile(const LSODEProfile &);
    
    
};

#endif //! _LSODE_H
