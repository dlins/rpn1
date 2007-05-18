/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import rpn.controller.ui.*;

public class UndoActionController extends AbstractAction {
    static public final String DESC_TEXT = "Undo Last Action";
    static private UndoActionController instance_ = null;
    private rpn.controller.ui.UndoableAction last_;

    protected UndoActionController() {
        // no ICONS
        super(DESC_TEXT, null);
        last_ = null;
        setEnabled(false);
    }

    static public void createInstance() {
        if (instance_ == null)
            instance_ = new UndoActionController();
    }

    public void setLastAction(rpn.controller.ui.UndoableAction action) {
        last_ = action;
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent action) {
        if (last_ != null) {
            // undo and unselect...
            last_.unexecute();
            UIController.instance().setState(new GEOM_SELECTION());
        }
        setEnabled(false);
    }

    static public UndoActionController instance() { return instance_; }
}
