/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import rpn.RPnCurvesList;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.BifurcationCurveGeomFactory;
import rpn.component.RpGeometry;

import rpn.controller.phasespace.extensions.ExtensionCalcState;
import rpn.controller.phasespace.extensions.ExtensionCalcWaitState;
import rpn.controller.phasespace.extensions.ExtensionCurveReadyState;
import rpn.controller.ui.UIController;
import rpnumerics.ExtensionCurveCalc;
import rpnumerics.RpException;

/**
 *
 * @author moreira
 */
public class GenericExtensionCurveCommand extends BifurcationPlotCommand {
    
    static public final String DESC_TEXT = "Extension Curve";
    //
    // Members
    //
    private static GenericExtensionCurveCommand instance_ = null;
    private RpGeometry curveToProcess_ = null;
    
    private ExtensionCalcState state_;

    //
    // Constructors
    //
    protected GenericExtensionCurveCommand() {
        
        super(DESC_TEXT, null, new JMenuItem(DESC_TEXT));
        
        state_ = new ExtensionCalcWaitState();
    }
    
    public void execute() {
        try {
            ExtensionCurveReadyState state = (ExtensionCurveReadyState) state_;
            
            ExtensionCurveCalc calc = state.createCalc();
            
            BifurcationCurveGeomFactory bifurcationFactory = new BifurcationCurveGeomFactory(calc);
            
            super.execute(bifurcationFactory);
        } catch (RpException ex) {
            Logger.getLogger(GenericExtensionCurveCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public ExtensionCalcState getState() {
        return state_;
    }
    
    public void setState(ExtensionCalcState state_) {
        this.state_ = state_;
    }
    
    public void reset(){
        

        setEnabled(false);
        state_= new ExtensionCalcWaitState();
        UIController.instance().globalInputTable().reset();
        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
        
        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();
            rPnPhaseSpacePanel.getCastedUI().pointMarkBuffer().clear();
        }
        
//        UIController.instance().clearAllAreas();
        
    }
    
    public void unexecute() {
    }
    
    static public GenericExtensionCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new GenericExtensionCurveCommand();
        }
        return instance_;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
        if (UIController.instance().getSelectedGeometriesList().size() == 1) {
            
            GenericExtensionCurveCommand.instance().getState().setCurve(UIController.instance().getSelectedGeometriesList().get(0));
            
        }
        
        if (UIController.instance().getSelectedGeometriesList().isEmpty()){
            reset();
        }
        
        
    }
    
}
