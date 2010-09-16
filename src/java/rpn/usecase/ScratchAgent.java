/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.usecase;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JToggleButton;
import rpn.controller.ui.SCRATCH_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputHandler;
import rpn.message.RPnActionMediator;


public class ScratchAgent extends javax.swing.AbstractAction {
    
    static public final String DESC_TEXT = "Scratch";
    static private ScratchAgent instance_ = null;
    
    private UserInputHandler currentSelection_,scratchSelected_;
    
    private JToggleButton button_;
    
    /** Creates a new instance of ScratchAgent */
    public ScratchAgent() {
        
        super(DESC_TEXT,null);
        
        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        putValue(Action.SHORT_DESCRIPTION, DESC_TEXT);
        scratchSelected_=  new SCRATCH_CONFIG();
        
        setEnabled(false);
        
        
    }
    
    public static ScratchAgent instance() {
        if (instance_ == null)
            instance_ = new ScratchAgent();
        return instance_;
    }
    
    
    public void actionPerformed(ActionEvent e) {
        
        if (button_.isSelected()){
            currentSelection_= UIController.instance().getState();
            UIController.instance().setState(scratchSelected_);
            
            
            if (UIController.instance().getNetStatusHandler().isOnline()){ //Sending application state
                RPnActionMediator.instance().setState(DESC_TEXT);
            }

        }
        
        else{
            UIController.instance().setState(currentSelection_);
            
        }
        
    }
    
    public JToggleButton getContainer(){return button_;}
}
