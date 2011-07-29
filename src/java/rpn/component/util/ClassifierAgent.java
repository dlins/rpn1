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
public class ClassifierAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Classify";
    static private ClassifierAgent instance_ = null;


    private JToggleButton button_;

    private ClassifierAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        //button_.setToolTipText(DESC_TEXT);

        button_.setToolTipText("<html>After selecting this option, you<br>"              //*** Leandro
                                   + "should take two clicks on the panel:<br>"
                                   + "the first click determine the point<br>"
                                   + "of interest in a curve; the second<br>"
                                   + "click determine the location of the label.</html>");

        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new CLASSIFIERAGENT_CONFIG());

    }

    public static ClassifierAgent instance() {
        if (instance_ == null) {
            instance_ = new ClassifierAgent();
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
