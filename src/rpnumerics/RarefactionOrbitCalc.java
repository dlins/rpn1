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
    private String methodName_;
    private String flowName_;
    
    
    //
    // Constructors/Initializers
    //
    public RarefactionOrbitCalc(PhasePoint point, int timeDirection) {
        start_ = point;
        timeDirection_ = timeDirection;
    }
    
    public RarefactionOrbitCalc(String methodName, String flowName, PhasePoint point, int timeDirection){
        
        methodName_=methodName;
        flowName_=flowName;
        start_=point;
        timeDirection_=timeDirection;
        
        flowName_="RarefactionFlow";//TODO Remove !
        methodName_="ContinuationRarefactionMethod";
        
    }
    
    //
    // Accessors/Mutators
    //
    public int tDirection() { return timeDirection_; }
    
    //
    // Methods
    //
    
    
    public  RpSolution recalc() throws RpException{
        
        return calc(methodName_,flowName_,start_,timeDirection_);
        
    }
    
    public RpSolution calc() throws RpException {
        
        RpSolution result =calc(methodName_,flowName_,start_,timeDirection_);
        
        return result;
    }
    
    private native  RpSolution calc(String methodName,String flowName,PhasePoint initialpoint, int timeDirection) throws RpException ;
}
