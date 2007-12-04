/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RK4BPMethod.h
 **/

#ifndef _RK4Solver_H
#define	_RK4Solver_H

#include "ODESolver.h"
#include <iostream>
#include "rk4.h"

#define SUCCESSFUL_PROCEDURE 0
#define ABORTED_PROCEDURE !SUCCESSFUL_PROCEDURE

class RK4BPMethod: public ODESolver{
    
public:
    
    //! Runge Kutta's method implementation
    ODESolution  solve(const RealVector & , const int );
    
    //! Constructor
    RK4BPMethod(const ODESolverProfile &);
//        RK4BPMethod();
    
    //! ODE solver profile accessor
    
    ODESolverProfile  getProfile();
    
    virtual ~RK4BPMethod();
    
protected:
    
            int rk4method(int , double , double, const RealVector &, RealVector & , int (*f)(int, double, double*, double*));
//    int rk4method_teste(int , double , double, const RealVector &, RealVector & , RpFunction &function);
    
    ODESolverProfile profile_;
    
};


inline ODESolverProfile  RK4BPMethod::getProfile(){return profile_;}

#endif	/* _RK4Solver_H */

