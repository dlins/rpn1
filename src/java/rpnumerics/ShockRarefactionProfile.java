/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpnumerics;

public abstract class ShockRarefactionProfile {
    
    private PhasePoint xZero_;
    private String flowName_;


    public ShockRarefactionProfile(PhasePoint xZero_) {
        this.xZero_ = xZero_;

    }
    
    
    public  PhasePoint getXZero() {return xZero_;}
    
    public  void setXZero(PhasePoint xZero){
        
        xZero_=xZero;}

    public String getFlowName() {
        return flowName_;
    }

    public void setFlowName(String flowName_) {
        this.flowName_ = flowName_;
    }

}
