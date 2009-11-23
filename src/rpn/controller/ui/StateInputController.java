/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.controller.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import rpn.RPnBifurcationConfigDialog;
import rpn.RPnUIFrame;

public class StateInputController implements PropertyChangeListener {

    private RPnUIFrame uiFrame_;
    public static boolean SHOWDIALOG = false;

    public StateInputController(RPnUIFrame frame) {
        uiFrame_ = frame;
    }

    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals("aplication state")) {
                uiFrame_.propertyChange(evt);
       
//            if (evt.getNewValue() instanceof BIFURCATION_CONFIG) {
//                RPnBifurcationConfigDialog bifurcationConfigDialog = new RPnBifurcationConfigDialog(false, false);
//                bifurcationConfigDialog.setVisible(true);
//
//            }

        }

    }
}
