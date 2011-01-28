/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import rpn.RPnCurvesConfigPanel;
import rpn.component.*;
import rpn.controller.ui.BIFURCATION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.*;
import wave.util.RealVector;

public class SubInflectionPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "SubInflection Curve";
    //
    // Members
    //
    static private SubInflectionPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        UIController.instance().setState(new BIFURCATION_CONFIG());

        action.userInputComplete(UIController.instance());// No input needed


    }

    protected SubInflectionPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JToggleButton());
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        SubInflectionCurveGeomFactory factory = new SubInflectionCurveGeomFactory(new SubInflectionCurveCalc());
        return factory.geom();

    }

    static public SubInflectionPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new SubInflectionPlotAgent();
        }
        return instance_;
    }
}
