/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component.util;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
import rpn.controller.ui.UIController;
import rpn.usecase.RpModelActionAgent;

/**
 *
 * @author moreira
 */
public class VelocityAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Velocity";
    static private VelocityAgent instance_ = null;


    private JToggleButton button_;

    private VelocityAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new VELOCITYAGENT_CONFIG());

    }

    public static VelocityAgent instance() {
        if (instance_ == null) {
            instance_ = new VelocityAgent();
        }
        return instance_;
    }

    public JToggleButton getContainer() {
        return button_;
    }

    @Override
    public void execute() {

    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
