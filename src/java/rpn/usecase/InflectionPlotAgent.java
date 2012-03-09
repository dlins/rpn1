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

public class InflectionPlotAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Inflection Curve";
    //
    // Members
    //
    static private InflectionPlotAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected InflectionPlotAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
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

        if (UIController.instance().isAuxPanelsEnabled()) {
            RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

            RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;

            RpGeometry leftGeometry = factory.leftGeom();
            RpGeometry rightGeometry = factory.rightGeom();

            leftPhaseSpace.plot(leftGeometry);
            rightPhaseSpace.plot(rightGeometry);
        } else {
            RPnDataModule.PHASESPACE.plot(factory.geom());
        }

     
    }

    static public InflectionPlotAgent instance() {
        if (instance_ == null) {
            instance_ = new InflectionPlotAgent();
        }
        return instance_;
    }
}
