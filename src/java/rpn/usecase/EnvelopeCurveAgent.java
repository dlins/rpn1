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

public class EnvelopeCurveAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Envelope Curve";
    //
    // Members
    //
    static private EnvelopeCurveAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected EnvelopeCurveAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        EnvelopeGeomFactory factory = new EnvelopeGeomFactory(RPNUMERICS.createEnvelopeCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        EnvelopeGeomFactory factory = new EnvelopeGeomFactory(RPNUMERICS.createEnvelopeCurveCalc());

            RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

            RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;

            RpGeometry leftGeometry = factory.leftGeom();
            RpGeometry rightGeometry = factory.rightGeom();

            leftPhaseSpace.join(leftGeometry);
            rightPhaseSpace.join(rightGeometry);

            RPnDataModule.PHASESPACE.join(factory.geom());








    }

    static public EnvelopeCurveAgent instance() {
        if (instance_ == null) {
            instance_ = new EnvelopeCurveAgent();
        }
        return instance_;
    }
}
