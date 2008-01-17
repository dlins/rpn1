/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;

import rpnumerics.RpException;

public class RarefactionOrbitCalc implements RpCalculation {
    //
    // Constants
    //
    //
    // Members
    //
    private PhasePoint start_;
    private int timeDirection_;
    
    //
    // Constructors/Initializers
    //
    public RarefactionOrbitCalc(PhasePoint point, int timeDirection) {
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
        
        return calc(RpNumerics.getRarefactionMethodName(),start_,timeDirection_);
        
    }
    
    public RpSolution calc() throws RpException {
        
        
        RpSolution result =calc(RpNumerics.getRarefactionMethodName(),start_,timeDirection_);
        
        return result;
    }
    
    
    private native  RpSolution calc(String methodName,PhasePoint initialpoint, int timeDirection) throws RpException ;
}
