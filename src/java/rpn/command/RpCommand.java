/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.UndoableAction;
import rpnumerics.Configuration;
import wave.util.RealVector;

public class RpCommand extends AbstractAction implements UndoableAction {

    private ArrayList<String> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;
    private String xmlOutput_;

    public RpCommand(String name, Icon icon) {
        super(name, icon);
        inputArray_ = new ArrayList<String>();
        xmlOutput_ = "";
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
        createXMLOutput();
    }

    public RpCommand(UI_ACTION_SELECTED actionSelected_, ArrayList<String> inputArray_) {
        this.inputArray_ = inputArray_;
        this.actionSelected_ = actionSelected_;
        createXMLOutput();
    }

    public RpCommand(UI_ACTION_SELECTED actionSelected_) {
        this.inputArray_ = new ArrayList<String>();
        this.actionSelected_ = actionSelected_;
        createXMLOutput();
    }

    public void setActionSelected(UI_ACTION_SELECTED actionSelected_) {
        this.actionSelected_ = actionSelected_;
    }

    public UI_ACTION_SELECTED getActionSelected() {
        return actionSelected_;
    }

    public ArrayList<String> getInputArray() {
        return inputArray_;
    }

    public String toXML() {
        return xmlOutput_;
    }

    private void createXMLOutput() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("<COMMAND name=\"" + actionSelected_.getAction().toString() + "\">\n");
        for (String input : inputArray_) {
            RealVector inputVector = new RealVector(input);
            buffer.append(inputVector.toXML());
            buffer.append("\n");
        }

        buffer.append("</COMMAND>\n");

        xmlOutput_ = buffer.toString();

    }

    public void unexecute() {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
