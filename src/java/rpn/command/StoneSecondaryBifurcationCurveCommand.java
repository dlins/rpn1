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

public class StoneSecondaryBifurcationCurveCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Stone Secondary Bifurcation Curve";
    //
    // Members
    //
    static private StoneSecondaryBifurcationCurveCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected StoneSecondaryBifurcationCurveCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        SecondaryBifurcationGeomFactory factory = new SecondaryBifurcationGeomFactory(RPNUMERICS.createStoneSecondaryBifurcationCurveCalc());

        return factory.geom();

    }

    @Override
    public void execute() {


        SecondaryBifurcationGeomFactory factory = new SecondaryBifurcationGeomFactory(RPNUMERICS.createStoneSecondaryBifurcationCurveCalc());




        RPnPhaseSpaceAbstraction leftPhaseSpace = RPnDataModule.LEFTPHASESPACE;

        RPnPhaseSpaceAbstraction rightPhaseSpace = RPnDataModule.RIGHTPHASESPACE;

        RpGeometry leftGeometry = factory.leftGeom();
        RpGeometry rightGeometry = factory.rightGeom();

        leftPhaseSpace.join(leftGeometry);
        rightPhaseSpace.join(rightGeometry);


        Iterator oldValue = RPnDataModule.PHASESPACE.getGeomObjIterator();
        PropertyChangeEvent event = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());



        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        logCommand(new RpCommand(event, emptyInput));




        RPnDataModule.PHASESPACE.join(factory.geom());








    }

    static public StoneSecondaryBifurcationCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new StoneSecondaryBifurcationCurveCommand();
        }
        return instance_;
    }
}
