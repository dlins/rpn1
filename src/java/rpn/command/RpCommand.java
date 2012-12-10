/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.UndoableAction;
import wave.util.RealVector;

public  class RpCommand extends AbstractAction implements UndoableAction {

    private ArrayList<String> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;
    private ArrayList<RealVector> inputPoint_;

    
    protected PropertyChangeEvent event_;


    public RpCommand(String name, Icon icon) {
        super(name, icon);
        inputArray_ = new ArrayList<String>();
        inputPoint_=new ArrayList<RealVector>();

    }
    
    public RpCommand(PropertyChangeEvent event){
        event_=event;
        inputPoint_=new ArrayList<RealVector>();
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
    
    public void setInput(ArrayList<RealVector> inputPoint){
        inputPoint_=inputPoint;
    }
    
    public ArrayList<RealVector> getInput(){
        return inputPoint_;
    }

    public  String toXML() {
        return "<COMMAND not found />\n";
    }
        

    public void unexecute() {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
