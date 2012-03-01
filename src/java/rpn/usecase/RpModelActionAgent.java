/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.controller.ui.*;
import rpn.message.RPnActionMediator;
import rpn.parser.RPnDataModule;

public abstract class RpModelActionAgent extends AbstractAction implements UndoableAction {

    private PropertyChangeEvent history_;
    private String desc_;
    protected static RPnPhaseSpaceAbstraction phaseSpace_;

    public RpModelActionAgent(String shortDesc, ImageIcon icon) {
        super(shortDesc, icon);
        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        desc_ = shortDesc;
        setEnabled(false);
        phaseSpace_ = RPnDataModule.PHASESPACE;
    }

    public abstract void execute();

    public abstract void unexecute();

    public void logAction(PropertyChangeEvent event) {
        history_ = event;

        if (UndoActionController.instance() == null) {
            UndoActionController.createInstance();
        } else {
            UndoActionController.instance().setLastAction(this);
        }
    }

    public void actionPerformed(ActionEvent event) {
        // garbage collection is ok ?

        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);

        UIController.instance().setState(action);
        if (UIController.instance().getNetStatusHandler().isOnline()) { //Sending application state
            RPnActionMediator.instance().setState(desc_);

            System.out.println(desc_);
        }

    }

    //
    // Accessors/Mutators
    //
    
    
    public void setPhaseSpace(RPnPhaseSpaceAbstraction phaseSpace) {
        phaseSpace_ = phaseSpace;
    }

    public PropertyChangeEvent log() {
        return history_;
    }

    @Override
    public String toString() {
        return desc_;
    }
}
