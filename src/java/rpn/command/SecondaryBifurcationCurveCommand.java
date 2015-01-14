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

public class SecondaryBifurcationCurveCommand extends RpModelPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Secondary Bifurcation Curve";
    //
    // Members
    //
    static private SecondaryBifurcationCurveCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected SecondaryBifurcationCurveCommand() {
        super(DESC_TEXT, null, new JButton());
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

        execute(factory);

    }

    static public SecondaryBifurcationCurveCommand instance() {
        if (instance_ == null) {
            instance_ = new SecondaryBifurcationCurveCommand();
        }
        return instance_;
    }
}
