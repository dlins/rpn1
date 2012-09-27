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

public class SecondaryBifurcationtAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Secondary Bifurcation";
    //
    // Members
    //
    static private SecondaryBifurcationtAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected SecondaryBifurcationtAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        SecondaryBifurcationGeomFactory factory = new SecondaryBifurcationGeomFactory(RPNUMERICS.createSecondaryBifurcationCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {

        SecondaryBifurcationGeomFactory factory = new SecondaryBifurcationGeomFactory(RPNUMERICS.createSecondaryBifurcationCurveCalc());

       
            RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

            RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;
            
            RpGeometry leftGeometry = factory.leftGeom();
            RpGeometry rightGeometry = factory.rightGeom();

            leftPhaseSpace.join(leftGeometry);
            rightPhaseSpace.join(rightGeometry);
       
            RPnDataModule.PHASESPACE.join(factory.geom());
       







    }

    static public SecondaryBifurcationtAgent instance() {
        if (instance_ == null) {
            instance_ = new SecondaryBifurcationtAgent();
        }
        return instance_;
    }
}
