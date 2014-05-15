/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeomFactory;
import rpn.component.RpGeometry;
import rpn.configuration.Configuration;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.controller.ui.UndoableAction;
import rpnumerics.RPnCurve;
import rpnumerics.RpCalculation;
import wave.util.RealVector;

public class RpCommand extends AbstractAction implements UndoableAction, Serializable {

    private ArrayList<String> inputArray_;
    private UI_ACTION_SELECTED actionSelected_;
    private ArrayList<RealVector> inputPointList_;
    private String xmlString_;
    private PropertyChangeEvent event_;

    public RpCommand(String name, Icon icon) {
        super(name, icon);
        inputArray_ = new ArrayList<String>();
        inputPointList_ = new ArrayList<RealVector>();

        StringBuilder buffer = new StringBuilder();

        buffer.append("<COMMAND name=\"").append(name).append("\">");
        buffer.append("</COMMAND>");

        xmlString_ = buffer.toString();

    }

    public RpCommand(int curveID) {

        StringBuilder buffer = new StringBuilder();
        buffer.append("<COMMAND name=\"").append("curveselection\" ");
        buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName()).append("\" ");
        buffer.append("curveid=\"").append(curveID);

        buffer.append("\"/>");

        xmlString_ = buffer.toString();

    }

    public RpCommand(String commandText) {
        inputArray_ = new ArrayList<String>();
        inputPointList_ = new ArrayList<RealVector>();

        StringBuilder buffer = new StringBuilder();
        if (commandText.equals("clear")) {

            buffer.append("<COMMAND name=\"").append("clear\" ");

            buffer.append("/>");

            xmlString_ = buffer.toString();


        } else {

            buffer.append("<COMMAND name=\"").append("selection\" ");
            buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName()).append("\"");
            buffer.append(">");
            buffer.append(commandText);
            buffer.append("</COMMAND>");


        }


        xmlString_ = buffer.toString();


    }

    public RpCommand(PropertyChangeEvent event) {
        event_ = event;
        inputPointList_ = new ArrayList<RealVector>();

        StringBuilder buffer = new StringBuilder();
        Configuration configuration = (Configuration) event_.getNewValue();

        buffer.append("<COMMAND name=\"").append(event_.getSource()).append("\">");
        buffer.append(configuration.toXML());
        buffer.append("</COMMAND>");

        xmlString_ = buffer.toString();



    }

    public RpCommand(PropertyChangeEvent event, ArrayList<RealVector> inputList) {
        event_ = event;
        inputPointList_ = inputList;

        RpGeometry geometry = (RpGeometry) event_.getNewValue();

        RpGeomFactory factory = geometry.geomFactory();

        StringBuilder buffer = new StringBuilder();
        String sourceName = event_.getSource().toString().replace(" ", "");
        String curveName = sourceName.toLowerCase();


        if (factory instanceof RpCalcBasedGeomFactory) {

            RpCalcBasedGeomFactory calFactory = (RpCalcBasedGeomFactory) factory;
            RpCalculation calc = (RpCalculation) calFactory.rpCalc();

            RPnCurve curve = (RPnCurve) calFactory.geomSource();

            buffer.append("<COMMAND name=\"").append(curveName).append("\" ");
            buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName()).append("\" ");
            buffer.append("curveid=\"").append(curve.getId());


            buffer.append("\">");

            if (calc.getConfiguration() != null) {
                String configurationXML = calc.getConfiguration().toXML();
                buffer.append(configurationXML);
            }

        } else {

            buffer.append("<COMMAND name=\"").append(curveName).append("\" ");
            buffer.append("phasespace=\"").append(UIController.instance().getActivePhaseSpace().getName());
            buffer.append("\">");

        }


        for (RealVector realVector : inputPointList_) {
            buffer.append(realVector.toXML());
        }

        buffer.append("</COMMAND>");

        xmlString_ = buffer.toString();

    }

    public void setActionSelected(UI_ACTION_SELECTED actionSelected_) {
        this.actionSelected_ = actionSelected_;
    }

    public PropertyChangeEvent getEvent() {
        return event_;
    }

    public UI_ACTION_SELECTED getActionSelected() {
        return actionSelected_;
    }

    public ArrayList<String> getInputArray() {
        return inputArray_;
    }

    public void setInput(ArrayList<RealVector> inputPoint) {
        inputPointList_ = inputPoint;
    }

    public ArrayList<RealVector> getInput() {
        return inputPointList_;
    }

    public String toXML() {

        return xmlString_;

    }

    public void unexecute() {
    }

    public void actionPerformed(ActionEvent e) {
    }
}
