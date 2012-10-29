/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.UndoableAction;
import wave.util.RealVector;


public class RpCommand extends AbstractAction implements UndoableAction{// TODO estender AbstractAction e implementar UndoableAction

    private ArrayList<RealVector> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;

    public RpCommand(String name, Icon icon) {
        super(name, icon);
        inputArray_=new ArrayList<RealVector>();
    }

    
    
    
    public RpCommand(UI_ACTION_SELECTED actionSelected_,RealVector input) {
        this.actionSelected_ = actionSelected_;
        inputArray_=new ArrayList<RealVector>();
        inputArray_.add(input);
    }

    public RpCommand( UI_ACTION_SELECTED actionSelected_,ArrayList<RealVector> inputArray_) {
        this.inputArray_ = inputArray_;
        this.actionSelected_ = actionSelected_;
    }


    public RpCommand(UI_ACTION_SELECTED actionSelected_) {
        this.inputArray_ = new ArrayList<RealVector>();
        this.actionSelected_ = actionSelected_;
    }

    public void setActionSelected(UI_ACTION_SELECTED actionSelected_) {
        this.actionSelected_ = actionSelected_;
    }

    public UI_ACTION_SELECTED getActionSelected() {
        return actionSelected_;
    }

    public ArrayList<RealVector> getInputArray() {
        return inputArray_;
    }

    public String toXML(){

          StringBuffer buffer = new StringBuffer();

            buffer.append("<COMMAND name=\"" +actionSelected_.getAction().toString()+ "\">\n");
            for(RealVector input:inputArray_){
                buffer.append(input.toXML());
                buffer.append("\n");
            }

            buffer.append("</COMMAND>\n");

        return buffer.toString();

    }

    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}
