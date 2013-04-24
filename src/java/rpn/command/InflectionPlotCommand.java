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
import java.util.Vector;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpn.configuration.Configuration;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class InflectionPlotCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Inflection Curve";
    //
    // Members
    //
    static private InflectionPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected InflectionPlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);

        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        InflectionCurveGeomFactory factory = new InflectionCurveGeomFactory(RPNUMERICS.createInflectionCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {

        InflectionCurveGeomFactory factory = new InflectionCurveGeomFactory(RPNUMERICS.createInflectionCurveCalc());
        execute(factory);

    }

    static public InflectionPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new InflectionPlotCommand();
        }
        return instance_;
    }
}
