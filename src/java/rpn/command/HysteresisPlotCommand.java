/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class HysteresisPlotCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Hysteresis Curve";
    //
    // Members
    //
    static private HysteresisPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected HysteresisPlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);

        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        HysteresisCurveGeomFactory factory = new HysteresisCurveGeomFactory(RPNUMERICS.createHysteresisCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {

        HysteresisCurveGeomFactory factory = new HysteresisCurveGeomFactory(RPNUMERICS.createHysteresisCurveCalc());

        execute(factory);
        

    }

    static public HysteresisPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new HysteresisPlotCommand();
        }
        return instance_;
    }
}
