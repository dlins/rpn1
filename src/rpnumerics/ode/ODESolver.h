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
    
    virtual ~ODESolver();
    virtual ODESolution & solve(const RealVector & , int ) const=0;
    virtual ODESolverProfile & getProfile() =0;
    
    
};

#endif //! _ODESolver_H
