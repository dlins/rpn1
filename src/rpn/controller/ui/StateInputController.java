/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpn.RPnBifurcationConfigDialog;
import rpn.RPnCurvesConfigDialog;
import rpn.RPnRarefactionConfigDialog;
import rpn.RPnShockConfigDialog;
import rpn.RPnUIFrame;

public class StateInputController implements PropertyChangeListener {

    private RPnUIFrame uiFrame_;
    public static boolean SHOWDIALOG = false;

    public StateInputController() {

    }

    public StateInputController(RPnUIFrame frame) {
        uiFrame_ = frame;
    }

    public void propertyChange(PropertyChangeEvent evt) {

//        System.out.println("Novo valor: " + evt.getNewValue());
        if (evt.getPropertyName().equals("reset aplication")) {

            RPnCurvesConfigDialog curvesDialog = new RPnCurvesConfigDialog();

            curvesDialog.setLocationRelativeTo(null);
            curvesDialog.setVisible(true);

        }

        if (evt.getPropertyName().equals("aplication state")) {

            try {

//                uiFrame_.firePropertyChange("aplication state", 0, 1);
                
                uiFrame_.propertyChange(evt);

            } catch (NullPointerException ex) {

                System.out.println("Nulo Frame!!");

            }

             if (evt.getNewValue() instanceof CURVES_CONFIG) {
            RPnCurvesConfigDialog curvesDialog = new RPnCurvesConfigDialog();

            curvesDialog.setLocationRelativeTo(null);
            curvesDialog.setVisible(true);
                 
                 
             }
            

            
            

            if (evt.getNewValue() instanceof SHOCK_CONFIG) {
                RPnShockConfigDialog shockDialog = new RPnShockConfigDialog();
                shockDialog.setVisible(true);
            }
            if (evt.getNewValue() instanceof RAREFACTION_CONFIG) {
                RPnRarefactionConfigDialog rarefactionDialog = new RPnRarefactionConfigDialog();
                rarefactionDialog.setVisible(true);
            }
            if (evt.getNewValue() instanceof BIFURCATION_CONFIG) {
                RPnBifurcationConfigDialog bifurcationConfigDialog = new RPnBifurcationConfigDialog();
                bifurcationConfigDialog.setVisible(true);
            }

        }

    }
}
