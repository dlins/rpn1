/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.util.Observable;
import javax.swing.JButton;
import rpn.component.*;
import rpn.configuration.Parameter;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.RPNUMERICS;
import rpnumerics.TransitionalLineCalc;
import wave.util.RealVector;

public class TransitionalLinePlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Transitional Line";
    //
    // Members
    //
    static private TransitionalLinePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected TransitionalLinePlotCommand() {
        super(DESC_TEXT, null, new JButton());

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        String transitionalLineName = RPNUMERICS.getParamValue("transitionalline", "name");
        TransitionalLineCalc calc = new TransitionalLineCalc(transitionalLineName);
        OrbitGeomFactory factory = new OrbitGeomFactory(calc);
        return factory.geom();
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);

        action.userInputComplete(UIController.instance());// No input needed

    }

    static public TransitionalLinePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new TransitionalLinePlotCommand();

        }
        return instance_;
    }

    @Override
    public void update(Observable o, Object arg) {

        rpn.configuration.Configuration config = RPNUMERICS.getConfiguration("transitionalline");

        
        Parameter parameter = config.getParameter("name");
        
        if (!parameter.getValue().equals("None")){
            setEnabled(true);
        }
        else {
            setEnabled(false);
        }
        

    }

}
