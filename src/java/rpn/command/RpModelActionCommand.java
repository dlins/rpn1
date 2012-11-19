/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.ImageIcon;
import rpn.controller.ui.*;
import rpn.message.RPnActionMediator;

public abstract class RpModelActionCommand extends RpCommand{

    private PropertyChangeEvent history_;
    private String desc_;

    public RpModelActionCommand(String shortDesc, ImageIcon icon) {
      super(shortDesc, icon);
        putValue(Action.SHORT_DESCRIPTION, shortDesc);
        desc_ = shortDesc;
        setEnabled(false);

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
        setActionSelected(action);

        UIController.instance().setState(action);
        if (UIController.instance().getNetStatusHandler().isOnline()) { //Sending application state
            RPnActionMediator.instance().setState(desc_);

            System.out.println(desc_);
        }

    }

    //
    // Accessors/Mutators
    //
   

    public PropertyChangeEvent log() {
        return history_;
    }

    @Override
    public String toString() {
        return desc_;
    }
}
