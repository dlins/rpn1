/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class HysteresisPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Hysteresis Curve";
    //
    // Members
    //
    static private HysteresisPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected HysteresisPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
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

        RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

        RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;

        RpGeometry leftGeometry = factory.leftGeom();
        RpGeometry rightGeometry = factory.rightGeom();

        leftPhaseSpace.plot(leftGeometry);
        rightPhaseSpace.plot(rightGeometry);

        System.out.println("Chamando execute");
    }

    static public HysteresisPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new HysteresisPlotAgent();
        }
        return instance_;
    }
}
