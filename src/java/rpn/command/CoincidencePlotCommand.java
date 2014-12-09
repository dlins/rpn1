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

public class CoincidencePlotCommand extends BifurcationPlotCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Coincidence Curve";

    //
    // Members
    //
    static private CoincidencePlotCommand instance_ = null;

    //
    // Constructors/Initializers
    //
    protected CoincidencePlotCommand() {
        super(DESC_TEXT, null,new JButton(DESC_TEXT));
    }


     @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
//        UIController.instance().setState(new BIFURCATION_CONFIG());
         
        action.userInputComplete(UIController.instance());// No input needed

    }
    public RpGeometry createRpGeometry(RealVector[] input) {

        CoincidenceCurveGeomFactory factory = new CoincidenceCurveGeomFactory(new CoincidenceCurveCalc());
        return factory.geom();

    }
    
    
    public void execute(){
        
        CoincidenceCurveGeomFactory factory = new CoincidenceCurveGeomFactory(new CoincidenceCurveCalc());
        RPnDataModule.PHASESPACE.join(factory.geom());
        
    }
            

    

    static public CoincidencePlotCommand instance() {
        if (instance_ == null) {
            instance_ = new CoincidencePlotCommand();
        }
        return instance_;
    }

   
}
