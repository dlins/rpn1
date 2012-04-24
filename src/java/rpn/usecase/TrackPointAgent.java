/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import rpn.controller.ui.TRACKPOINT_CONFIG;
import rpn.controller.ui.UIController;

public class TrackPointAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Track Point";

    static private TrackPointAgent instance_ = null;
    private JToggleButton button_;

    private TrackPointAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        setEnabled(true);
    }

    public static TrackPointAgent instance() {
        if (instance_ == null) {
            instance_ = new TrackPointAgent();
        }
        return instance_;
    }

    public JToggleButton getContainer() {
        return button_;
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        System.out.println("Chamando execute do track agent");

    }

    @Override
    public void actionPerformed(ActionEvent event) {


        UIController.instance().setState(new TRACKPOINT_CONFIG());


    }
}


