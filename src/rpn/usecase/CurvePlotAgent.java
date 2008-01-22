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
    
    
    private RpGeometry plot (String methodName,String flowName,RealVector [] input){

        type_="rarefaction"; //TODO  Hardcoded !
        
        if (type_.equals("shock")){

            System.out.println("Plotando curva de shock");
            return null;
        }
        
        if (type_.equals("rarefaction")){
              
        PhasePoint oPoint = new PhasePoint(input[input.length - 1]);
        RarefactionOrbitGeomFactory factory = new RarefactionOrbitGeomFactory(new RarefactionOrbitCalc(methodName,flowName,oPoint,-1));
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
}
