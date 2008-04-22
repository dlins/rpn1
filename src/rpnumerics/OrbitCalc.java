/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

import rpnumerics.RpException;

public class OrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private OrbitPoint start_;
    private int timeDirection_;

    //
    // Constructors/Initializers
    //
    public OrbitCalc(OrbitPoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }

    //
    // Accessors/Mutators
    //
    public int tDirection() { return timeDirection_; }

    //
    // Methods
    //
    
    
    public  RpSolution recalc() throws RpException{
        
        return calc (start_,timeDirection_);
        
    }
    
     public   RpSolution calc() throws RpException {
         
         
         RpSolution result =calc (start_,timeDirection_);
         
          return result;
     }
//    private native RpSolution recalc(OrbitPoint initialPoint, int timeDirection) throws RpException;

    private native  RpSolution calc(OrbitPoint initialpoint, int timeDirection) throws RpException ;
}
