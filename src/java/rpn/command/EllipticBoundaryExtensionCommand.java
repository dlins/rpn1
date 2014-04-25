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
import rpnumerics.*;
import wave.util.RealVector;

public class EllipticBoundaryExtensionCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Elliptic Extension Boundary";
    //
    // Members
    //
    static private EllipticBoundaryExtensionCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected EllipticBoundaryExtensionCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton());

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        EllipticBoundaryExtensionFactory factory = new EllipticBoundaryExtensionFactory(RPNUMERICS.createEllipticExtensionCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        EllipticBoundaryExtensionFactory factory = new EllipticBoundaryExtensionFactory(RPNUMERICS.createEllipticExtensionCalc());
        execute(factory);

    }

    static public EllipticBoundaryExtensionCommand instance() {
        if (instance_ == null) {
            instance_ = new EllipticBoundaryExtensionCommand();
        }
        return instance_;
    }
}
