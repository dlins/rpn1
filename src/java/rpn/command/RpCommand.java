/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.UndoableAction;
import rpn.configuration.Configuration;
import rpnumerics.RpCalculation;
import rpnumerics.RpCurve;
import wave.util.RealVector;

public class RpCommand extends AbstractAction implements UndoableAction {

    private ArrayList<String> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;
    private String xmlOutput_;
    
    private PropertyChangeEvent event_;


    public RpCommand(String name, Icon icon) {
        super(name, icon);
        inputArray_ = new ArrayList<String>();
        xmlOutput_ = "";
    }

    
    
    public RpCommand(PropertyChangeEvent event){
    
        event_=event;

        
    }
      
      
    
    
      
      
    public RpCommand(UI_ACTION_SELECTED actionSelected_, Configuration input) {

        this.actionSelected_ = actionSelected_;

        inputArray_ = new ArrayList<String>();
        HashMap<String, String> paramsMap = input.getParams();

        xmlOutput_ = input.toXML();

        Set<Entry<String, String>> paramsSet = paramsMap.entrySet();
        int i = 0;

        for (Entry<String, String> entry : paramsSet) {
            inputArray_.add(entry.getValue());
            i++;
        }

    }

    public RpCommand(UI_ACTION_SELECTED actionSelected_, RealVector input) {

        this.actionSelected_ = actionSelected_;
        inputArray_ = new ArrayList<String>();
        inputArray_.add(input.toString());
    }

    public RpCommand(UI_ACTION_SELECTED actionSelected_, ArrayList<String> inputArray_) {
        this.inputArray_ = inputArray_;
        this.actionSelected_ = actionSelected_;

    }

    public RpCommand(UI_ACTION_SELECTED actionSelected_) {
        this.inputArray_ = new ArrayList<String>();
        this.actionSelected_ = actionSelected_;

    }

    public void setActionSelected(UI_ACTION_SELECTED actionSelected_) {
        this.actionSelected_ = actionSelected_;
    }

    public PropertyChangeEvent getEvent(){
        return event_;
    }
    
    public UI_ACTION_SELECTED getActionSelected() {
        return actionSelected_;
    }

    public ArrayList<String> getInputArray() {
        return inputArray_;
    }

    public String toXML() {
        
        if (event_.getSource() instanceof RpModelPlotCommand)

        {
            RpGeometry geometry =(RpGeometry)event_.getNewValue();

            RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();
            
            RpCalculation calc = (RpCalculation) factory.rpCalc();
            
            return calc.getConfiguration().toXML();
            
        }
        
        
        if (event_.getSource() instanceof RpModelConfigChangeCommand){
            Configuration configuration = (Configuration)event_.getNewValue();
            return configuration.toXML();
        }
        
        return xmlOutput_;
    }


    public void unexecute() {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
