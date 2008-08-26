/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;


public class RPnShockMenuManager implements ActionListener {

    private JMenu modelMenu_;

    public RPnShockMenuManager(JMenu menu) {

        modelMenu_ = menu;


    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Shock")) {

            modelMenu_.getComponent().setEnabled(true);

        }

        if (e.getActionCommand().equals("Rarefaction")) {
            
            modelMenu_.getComponent().setEnabled(false);

        }
    }

    
}
