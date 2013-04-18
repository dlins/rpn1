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

public class BoundaryExtensionCurveCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Boundary Extension Curve";
    //
    // Members
    //
    static private BoundaryExtensionCurveCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BoundaryExtensionCurveCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
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



        Iterator oldValue = RPnDataModule.PHASESPACE.getGeomObjIterator();
        PropertyChangeEvent event_ = new PropertyChangeEvent(this, UIController.instance().getActivePhaseSpace().getName(), oldValue, factory.geom());

        ArrayList<RealVector> emptyInput = new ArrayList<RealVector>();
        logCommand(new RpCommand(event_, emptyInput));



        RPnDataModule.PHASESPACE.join(factory.geom());



    }

    static public BoundaryExtensionCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new BoundaryExtensionCurveCommand();
        }
        return instance_;
    }
}
