#include "ODEStopGenerator.h"
class ODESolverProfile {
    
    
    public :
        
        
        // RK4 Engine. The arguments are:
        //
        //          n: Dimension of the problem
        //          t: The time of this step
        //     deltat: Time step
        //          p: Pointer to the array of coordinates where the integration takes place
        //        out: Pointer to the array of coordinates where the solution lies
        //          f: Pointer to the function that defines the problem, and returns an array of doubles
        //             (later used by rk4).
        //
        // The functions that define the problem must be grouped under f. An array will be given
        // as input and another one as output, to be used by the RK4 proper.
        //
        // RETURNS:
        // ABORTED_PROCEDURE    (default: 1): An internal result met the stop criterions (defined elsewhere).
        // SUCCESSFUL_PROCEDURE (default: 0): Everything's OK.
        //
        
        // O criterio de parada esta definido dentro da propria funcao usada pelo metodo . Se ela retornar ABORT_PROCEDURE o metodo eh interrompido
        
//        public Rk4BPProfile(VectorField f, double epsilon, double dYmax, Boundary boundary, RealVector yScales, int MaxStepN) ;
        
        
        ~ODESolverProfile();
        ODESolverProfile(const ODEStopEvaluator &);
        ODEStopEvaluator getStopEvaluator();
        
        double getDeltat();
        
        private:
        
        ODEStopEvaluator stopEvaluator_;
        

        
     
        
       
        


        
};

