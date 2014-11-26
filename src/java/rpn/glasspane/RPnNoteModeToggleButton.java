/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.glasspane;

import javax.swing.JButton;
import rpn.RPnPhaseSpaceFrame;

/**
 *
 * @author mvera
 */
public class RPnNoteModeToggleButton extends JButton {

    private RPnPhaseSpaceFrame parentContainer_;

    public RPnNoteModeToggleButton(RPnPhaseSpaceFrame parentContainer,String title) {


        super(title);
        parentContainer_ = parentContainer;
    }

    public RPnPhaseSpaceFrame getParentContainer() {return parentContainer_;}
}
