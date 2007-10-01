/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpnumerics;


import rpn.controller.ui.*;
import rpn.usecase.BackwardOrbitPlotAgent;
import rpn.usecase.ForwardOrbitPlotAgent;
import wave.multid.Space;
import wave.util.Boundary;

public class RPNumericsProfile {
    
    private Boundary boundary_;
    private boolean hasBoundary_ = false;
    private HugoniotCurveCalc hugoniotCurveCalc_;
    private Integer familyIndex_;
    private static boolean flowInitialized_ = false;
    private static UserInputHandler initialState_ = null;
    private String nativeLibName_;
    private String flowType_;
    private String physicsId_;
    private static Space domain_;
    
    
    
    // Constructors/Initializers
    
    public void initPhysics(String physicsId,String nativeLibName,String domainSize) {
        
        
        System.loadLibrary(nativeLibName);
        
        physicsId_ = physicsId;
        nativeLibName_ = nativeLibName;
        RPNUMERICS.setProfile(this);
        domain_= new Space("", new Integer(domainSize).intValue());
    }
    
    public void initFlowType(String flowType) {
        
        flowType_ = flowType;
        
        if (flowType.equals("shockflow")) {
            //TODO setar o estado inicial para Shock Config
            setInitialState(new SHOCK_CONFIG());
            
            
            //--------TESTE------------------------------------
//            BackwardOrbitPlotAgent.instance().setEnabled(true);
//            ForwardOrbitPlotAgent.instance().setEnabled(true);
            
//            setInitialState(new GEOM_SELECTION());
            //----------------------------------------------------
        }
        
        if (flowType.equals("rarefactionflow")|| flowType.equals("blowuprarefactionflow")) {
            setInitialState(new RAREFACTION_CONFIG());
            
        }
        
        
        if (initialState_ == null) {
            System.out.println("Wrong flow type!");
        }
        
    }
    
    //Acessors
    
    public void setFamilyIndexFlow(int familyIndex) {
        familyIndex_ = new Integer(familyIndex);
    }
    
    public void setBoundary(Boundary boundary) {
        boundary_ = boundary;
        hasBoundary_ = true;
    }
    
    public boolean hasBoundary() {
        return hasBoundary_;
    }
    
    public Boundary getBoundary() {
        
        if (hasBoundary()) {
            
            return boundary_;
        } else {
            
            return RPNUMERICS.boundary();
        }
    }
    
    public String getPhysicsID() {
        return physicsId_;
    }
    
    
    public HugoniotCurveCalc getHugoniotCurveCalc() {
        return hugoniotCurveCalc_;
    }
    
    public String libName() {
        return nativeLibName_;
    }
    
    public boolean isFlowInitialized() {
        return flowInitialized_;
    }
    
    public int getFamilyIndex() {
        return familyIndex_.intValue();
    }
    
    public String getFlowType() {
        return flowType_;
    }
    
    public static UserInputHandler getInitialState() {
        return initialState_;
    }
    
    public  Space getDomain(){return domain_;}
    
    //Mutators
    
    public static void setInitialState(UserInputHandler userInput) {
        initialState_ = userInput;
    }
    
    
}
