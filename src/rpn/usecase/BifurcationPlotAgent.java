/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.BIFURCATION_CONFIG;
import rpn.controller.ui.Command;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.*;
import wave.util.*;

public class BifurcationPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Bifurcation Curve";
    // Members
    //
    static private BifurcationPlotAgent instance_ = null;
    //
    // Constructors/Initializers
    //

    protected BifurcationPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        UIController.instance().setState(new BIFURCATION_CONFIG());

        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        System.out.println("Calling createGeometry from BifurcationPlotAgent");

        BifurcationCurveCalc curveCalc = RPNUMERICS.createBifurcationCalc();
        BifurcationCurveGeomFactory factory = new BifurcationCurveGeomFactory(curveCalc);
        BifurcationRefineAgent.instance().setEnabled(true);


        return factory.geom();
    }

    static public BifurcationPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new BifurcationPlotAgent();
        }
        return instance_;
    }


}
