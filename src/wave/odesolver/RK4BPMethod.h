#include "ODESolver.h"
#include "rk4.h"

#ifndef _RK4Solver_H
#define	_RK4Solver_H

#define SUCCESSFUL_PROCEDURE 0                    
#define ABORTED_PROCEDURE !SUCCESSFUL_PROCEDURE

class RK4BPMethod: public ODESolver{
    
    public:
        ODESolution solve(const RealVector & , int );
        RK4BPMethod(const ODESolverProfile &);
        ODESolverProfile getProfile();

        ~RK4BPMethod();

        protected:

            int rk4(int , double , double, const RealVector &,  RealVector & , int (*f)(int, double, double*, double*));
            ODESolverProfile profile_;
            
};

#endif	/* _RK4Solver_H */

