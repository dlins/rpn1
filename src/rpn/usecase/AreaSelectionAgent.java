/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JToggleButton;
import rpn.RPnContourConfigDialog;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputHandler;

public class AreaSelectionAgent extends javax.swing.AbstractAction {

    static public final String DESC_TEXT = "Select Area";
    static private AreaSelectionAgent instance_ = null;
    private UserInputHandler currentState_, areaSelectionSelected_;
    private JToggleButton button_;
    private RPnContourConfigDialog contourConfigDialog_;

    /** Creates a new instance of ScratchAgent */
    public AreaSelectionAgent() {

        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        putValue(Action.SHORT_DESCRIPTION, DESC_TEXT);
        areaSelectionSelected_ = new AREASELECTION_CONFIG();
        setEnabled(true);
    }

    public static AreaSelectionAgent instance() {
        if (instance_ == null) {
            instance_ = new AreaSelectionAgent();
        }
        return instance_;
    }

    public void actionPerformed(ActionEvent e) {

        if (button_.isSelected()) {
            currentState_ = UIController.instance().getState();
            UIController.instance().setState(areaSelectionSelected_);

            // Send the selection to others in needed ??

//            if (UIController.instance().getNetStatusHandler().isOnline()){ //Sending application state
//                RPnActionMediator.instance().setState(DESC_TEXT);
//            }

        } else {
            UIController.instance().setState(currentState_);
            ((AREASELECTION_CONFIG) (areaSelectionSelected_)).reset();
            UIController.instance().getFocusPanel().getCastedUI().getSelectionAreas().clear();
            UIController.instance().getFocusPanel().getCastedUI().pointMarkBuffer().clear();
            BifurcationRefineAgent.instance().setEnabled(true);

        }

    }
    /**
     * @deprecated
     */
    public void eraseArea(){
     UIController.instance().getFocusPanel().eraseSelectedArea();
    }

    public JToggleButton getContainer() {
        return button_;
    }
}


