
#ifndef _ODESigmaSolver_H
#define	_ODESigmaSolver_H

#include "RK4BPMethod.h"
#include "VectorField.h"

class ODESigmaSolver: public RK4BPMethod{
    
    public:
        
        ODESigmaSolver (const VectorField &,const ODESolverProfile &);
        ODESolution  solve(const RealVector & , int );
        ODESolverProfile getProfile();
        VectorField getVectorField();
        
//        int rk4(int , double , double, const RealVector &, const RealVector & , int (*f)(int, double, double*, double*));

        private:

            VectorField vf_;
};

#endif	/* _ODESigmaSolver_H */

