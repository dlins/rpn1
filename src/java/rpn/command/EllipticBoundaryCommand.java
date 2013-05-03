/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class EllipticBoundaryCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Elliptic Boundary";
    //
    // Members
    //
    static private EllipticBoundaryCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected EllipticBoundaryCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        EllipticBoundaryFactory factory = new EllipticBoundaryFactory(RPNUMERICS.createEllipticBoundaryCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        EllipticBoundaryFactory factory = new EllipticBoundaryFactory(RPNUMERICS.createEllipticBoundaryCalc());

        RPnDataModule.PHASESPACE.plot(factory.geom());

    }

    static public EllipticBoundaryCommand instance() {
        if (instance_ == null) {
            instance_ = new EllipticBoundaryCommand();
        }
        return instance_;
    }
}
