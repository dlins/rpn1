

#ifndef _RK4SolverProfile_H
#define	_RK4SolverProfile_H


#include "ODESolverProfile.h"

class RK4SolverProfile:public ODESolverProfile {

    //
    // Members
    //
    
    public:

        RK4SolverProfile(ODEStopEvaluator & );
        double getDeltat();
      
        
        private:
            ODEStopEvaluator stopEvaluator_;

            double deltat_;
        

};   
            
#endif	/* _RK4SolverProfile_H */
            
