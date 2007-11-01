
#ifndef _ODEStopGenerator_H
#define	_ODEStopGenerator_H

#include "RealVector.h"
class ODEStopEvaluator{
    
    
    public :
        
        const int getMaxSteps();
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
            
            RealVector & yScales_; // scales for state
            
            int timeDirection_;
            int MaxStepN_; // maximum number of steps
    
    
    
};


inline const int ODEStopEvaluator::getMaxSteps(){return MaxStepN_;}

#endif	/* _ODEStopGenerator_H */

