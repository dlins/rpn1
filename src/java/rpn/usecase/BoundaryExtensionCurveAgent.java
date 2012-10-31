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
import rpn.controller.ui.BIFURCATION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class BoundaryExtensionCurveAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Boundary Extension Curve";
    //
    // Members
    //
    static private BoundaryExtensionCurveAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BoundaryExtensionCurveAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
//        UIController.instance().setState(new BIFURCATION_CONFIG());

        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        BoundaryExtensionCurveGeomFactory factory = new BoundaryExtensionCurveGeomFactory(RPNUMERICS.createBoundaryExtensionCurveCalc());

        return factory.geom();

    }

    @Override
    public void execute() {

        BoundaryExtensionCurveGeomFactory factory = new BoundaryExtensionCurveGeomFactory(RPNUMERICS.createBoundaryExtensionCurveCalc());


            RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

            RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;

            RpGeometry leftGeometry = factory.leftGeom();
            RpGeometry rightGeometry = factory.rightGeom();

            leftPhaseSpace.join(leftGeometry);
            rightPhaseSpace.join(rightGeometry);

            RPnDataModule.PHASESPACE.join(factory.geom());



    }

    static public BoundaryExtensionCurveAgent instance() {
        if (instance_ == null) {
            instance_ = new BoundaryExtensionCurveAgent();
        }
        return instance_;
    }
}
