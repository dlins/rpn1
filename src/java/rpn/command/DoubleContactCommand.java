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

public class DoubleContactCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "DoubleContact Curve";
    //
    // Members
    //
    static private DoubleContactCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected DoubleContactCommand() {
        super(DESC_TEXT, null, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
        return factory.geom();

    }

    @Override
    public void execute() {


        DoubleContactGeomFactory factory = new DoubleContactGeomFactory(RPNUMERICS.createDoubleContactCurveCalc());
    	execute(factory);
    }


    static public DoubleContactCommand instance() {
        if (instance_ == null) {
            instance_ = new DoubleContactCommand();
        }
        return instance_;
    }
}
