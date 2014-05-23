/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.RiemannProblemConfig;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class SubInflectionPlotCommand extends BifurcationPlotCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "SubInflection Curve";
    //
    // Members
    //
    static private SubInflectionPlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
//        UIController.instance().setState(new BIFURCATION_CONFIG());

        action.userInputComplete(UIController.instance());// No input needed


    }

    protected SubInflectionPlotCommand() {
        super(DESC_TEXT, rpn.configuration.RPnConfig.HUGONIOT, new JButton(DESC_TEXT));
    }

    public RpGeometry createRpGeometry(RealVector[] input) {

        SubInflectionCurveGeomFactory factory = new SubInflectionCurveGeomFactory(new SubInflectionCurveCalc());
        return factory.geom();

    }
    
    public void execute(){
        
        SubInflectionCurveGeomFactory factory = new SubInflectionCurveGeomFactory(new SubInflectionCurveCalc());
        RPnDataModule.PHASESPACE.join(factory.geom());
        
    }

    static public SubInflectionPlotCommand instance() {
        if (instance_ == null) {
            instance_ = new SubInflectionPlotCommand();
        }
        return instance_;
    }
}
