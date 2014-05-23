/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class PhysicalBoundaryPlotCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Physical Boundary";
    //
    // Members
    //
    static private PhysicalBoundaryPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected PhysicalBoundaryPlotCommand() {
        super(DESC_TEXT, null, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        PhysicalBoundaryFactory factory = new PhysicalBoundaryFactory(new PhysicalBoundaryCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        PhysicalBoundaryFactory factory = new PhysicalBoundaryFactory(new PhysicalBoundaryCalc());

        RPnDataModule.LEFTPHASESPACE.join(factory.geom());
        RPnDataModule.RIGHTPHASESPACE.join(factory.geom());
        RPnDataModule.PHASESPACE.join(factory.geom());



    }

    static public PhysicalBoundaryPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new PhysicalBoundaryPlotCommand();
        }
        return instance_;
    }
}
