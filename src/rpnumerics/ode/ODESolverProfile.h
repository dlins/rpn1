#ifndef _ODESolverProfile_H
#define	_ODESolverProfile_H


#include "ODEStopEvaluator.h"
#include "RpFunction.h"
//#include "ShockRarefaction.h"

class ODESolverProfile{
    
    public:
//        ODESolverProfile();
        
        ODESolverProfile(int ,double ,RpFunction &);
//        ODESolverProfile(ODEStopEvaluator &);
//        ODESolverProfile();
        
        ODEStopEvaluator & getStopEvaluator() ;
        double getDeltat();
        int getDimension();
        
        RpFunction & getFunction();
        
        private:
            
            
            ODEStopEvaluator evaluator_;
            double delta_;
            int dimension_;
            RpFunction & function_;
            
            
};

inline  ODEStopEvaluator & ODESolverProfile::getStopEvaluator() {return evaluator_;}

inline double ODESolverProfile::getDeltat(){return delta_;}

inline int ODESolverProfile::getDimension(){return dimension_;}

inline RpFunction &  ODESolverProfile::getFunction() {return  function_;}
#endif	/* _ODESolverProfile_H */

