/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) ODESolver.h
 */

#ifndef _ODESolver_H
#define _ODESolver_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "RealVector.h"
#include "ODESolverProfile.h"
#include "ODESolution.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */


class ODESolver {
    
private:
    
public:


    virtual int  solve(const RealVector & ,RealVector &,double & ) const=0;
    virtual int solve(const RealVector &, ODESolution &) const = 0;
    virtual const ODESolverProfile & getProfile() const=0;
    virtual ODESolver * clone()const =0;
    virtual ~ODESolver();    
    
};

#endif //! _ODESolver_H
