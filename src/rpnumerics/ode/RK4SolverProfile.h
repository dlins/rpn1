#include "ODESolverProfile.h"

#ifndef _RK4SolverProfile_H
#define	_RK4SolverProfile_H

class RK4SolverProfile:public ODESolverProfile {
    //
    // Members
    //
    
    public:

        RK4SolverProfile(ODEStopEvaluator& );
        double getDeltat();
      
        
        private:
            ODEStopEvaluator stopGenerator_;

            double deltat_;
        

};   
            
#endif	/* _RK4SolverProfile_H */
            
