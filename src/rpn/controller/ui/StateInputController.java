/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpn.RPnBifurcationConfigDialog;
import rpn.RPnCurvesConfigDialog;
import rpn.RPnUIFrame;

public class StateInputController implements PropertyChangeListener {

    private RPnUIFrame uiFrame_;
    public static boolean SHOWDIALOG = false;

    public StateInputController(RPnUIFrame frame) {
        uiFrame_ = frame;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("aplication state")) {

            try {
                if ((evt.getNewValue() instanceof CURVES_CONFIG) || (evt.getOldValue() instanceof CURVES_CONFIG)) {
                    RPnUIFrame.setStatusMessage(" ");
                } else {
                    if (evt.getNewValue() instanceof SIGMA_CONFIG) {
                        RPnUIFrame.setStatusMessage("Set Sigma");
                    } else {
                        RPnUIFrame.setStatusMessage("Enter coordinates");
                    }
                }

                uiFrame_.propertyChange(evt);

            } catch (NullPointerException ex) {

                ex.printStackTrace();

            }

            if (evt.getNewValue() instanceof CURVES_CONFIG) {

                RPnCurvesConfigDialog curvesDialog = new RPnCurvesConfigDialog();
                curvesDialog.setLocationRelativeTo(null);
                curvesDialog.setVisible(true);
            }

//            if (evt.getNewValue() instanceof SHOCK_CONFIG) {
////                RPnShockConfigDialog shockDialog = new RPnShockConfigDialog(false, false);
////                shockDialog.setVisible(true);
//                //Setting the default shock flow as the current 
//          
//                
//            }
            if (evt.getNewValue() instanceof RAREFACTION_CONFIG) {
//                RPnRarefactionConfigDialog rarefactionDialog = new RPnRarefactionConfigDialog(false, false);
//                rarefactionDialog.setVisible(true);
            }
            if (evt.getNewValue() instanceof BIFURCATION_CONFIG) {
                RPnBifurcationConfigDialog bifurcationConfigDialog = new RPnBifurcationConfigDialog(false, false);
                bifurcationConfigDialog.setVisible(true);

            }

        }

    }
}
