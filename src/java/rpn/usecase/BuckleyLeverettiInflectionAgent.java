/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpnumerics.*;
import wave.util.RealVector;

public class BuckleyLeverettiInflectionAgent extends RpModelPlotAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "BuckleyLeverettinInflection Curve";

    //
    // Members
    //
    static private BuckleyLeverettiInflectionAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected BuckleyLeverettiInflectionAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT,new JButton(DESC_TEXT));
    }


     @Override
    public void actionPerformed(ActionEvent event) {

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
//        UIController.instance().setState(new BIFURCATION_CONFIG());
         
        action.userInputComplete(UIController.instance());// No input needed

    }
    public RpGeometry createRpGeometry(RealVector[] input) {

        BuckleyLeverettinCurveGeomFactory factory = new BuckleyLeverettinCurveGeomFactory(new BuckleyLeverettinInflectionCurveCalc());
        return factory.geom();

    }

    static public BuckleyLeverettiInflectionAgent instance() {
        if (instance_ == null) {
            instance_ = new BuckleyLeverettiInflectionAgent();
        }
        return instance_;
    }

   
}
