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

public class DoubleContactAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "DoubleContact Curve";
    //
    // Members
    //
    static private DoubleContactAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected DoubleContactAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
//        UIController.instance().setState(new BIFURCATION_CONFIG());

        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());

        return factory.geom();

    }

    public void execute() {
        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());


//        RPnPhaseSpaceAbstraction auxPhaseSpace = RPnDataModule.PHASESPACE;//AUXPHASESPACE;

        RPnPhaseSpaceAbstraction auxPhaseSpace = RPnDataModule.PHASESPACE;

        RpGeometry geometry = factory.geom();

        auxPhaseSpace.plot(geometry);

        System.out.println("Chamando execute");
    }

    static public DoubleContactAgent instance() {
        if (instance_ == null) {
            instance_ = new DoubleContactAgent();
        }
        return instance_;
    }
}
