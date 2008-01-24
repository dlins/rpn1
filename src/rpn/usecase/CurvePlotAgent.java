/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import rpn.component.*;
import rpnumerics.PhasePoint;
import rpnumerics.RarefactionOrbitCalc;
import wave.util.RealVector;

public class CurvePlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Curve";
    
    private String methodName_;
    private String type_;
    private String flowName_;
    private int familyIndex_;
    private int timeDirection_;
    
    
    
    //
    // Members
    //
    static private CurvePlotAgent instance_ = null;
    
    //
    // Constructors/Initializers
    //
    protected CurvePlotAgent() {
        super(DESC_TEXT, rpn.RPnConfigReader.ORBIT_FWD);
    }
    
    public RpGeometry createRpGeometry(RealVector[] input) {
        return  plot(methodName_,flowName_,input);
        
    }
    
    static public CurvePlotAgent instance() {
        if (instance_ == null)
            instance_ = new CurvePlotAgent();
        return instance_;
    }
    
    
    private RpGeometry plot(String methodName,String flowName,RealVector [] input){
        
//        setType("rarefaction"); //TODO  Hardcoded !
        
        if (getType().equals("shock")){
            
            System.out.println("Plotando curva de shock");
            return null;
        }
        
        if (getType().equals("rarefaction")){
            
            PhasePoint oPoint = new PhasePoint(input[input.length - 1]);
            RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(new RarefactionOrbitCalc(methodName_,flowName_,oPoint,familyIndex_,timeDirection_));
            return factory.geom();
        }
        
        return null;
    }
    
    public void setMethodName(String methodName) {
        methodName_ = methodName;
    }
    
    public void setFlowName(String flowName) {
        flowName_ = flowName;
    }
    
    public String getType() {
        return type_;
    }
    
    public void setType(String type) {
        type_ = type;
    }
    
    public int getFamilyIndex() {
        return familyIndex_;
    }
    
    public void setFamilyIndex(int familyIndex) {
        familyIndex_ = familyIndex;
    }

    public int getTimeDirection() {
        return timeDirection_;
    }

    public void setTimeDirection(int timeDirection) {
        this.timeDirection_ = timeDirection;
    }
}
