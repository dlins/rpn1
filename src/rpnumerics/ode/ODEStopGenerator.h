#include "RealVector.h"

#ifndef _ODEStopGenerator_H
#define	_ODEStopGenerator_H

class ODEStopEvaluator{
    
    
    public :
        
        int getMaxSteps();
        bool check(const RealVector &);
    
      private:
            
            double epsilon_; // accuracy
            double dYmax_; // maximum step in the state space
            bool checkPoincareSection_; // check ps

            // Ainda não definidos !!
            //            SimplexPoincareSection poincareSection_; // ps
            //            Boundary boundary_; // b
            //
            
            bool checkBoundary_; // check b
            
            RealVector yScales_; // scales for state
            
            int timeDirection_;
            int MaxStepN_; // maximum number of steps
    
    
    
};

inline bool ODEStopEvaluator::check(const RealVector &){return true;}

#endif	/* _ODEStopGenerator_H */

