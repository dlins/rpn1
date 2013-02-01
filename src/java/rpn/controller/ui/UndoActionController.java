/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import rpn.command.RpCommand;

public class UndoActionController extends AbstractAction {
    static public final String DESC_TEXT = "Undo Last Action";
    static private UndoActionController instance_ = null;
//    private rpn.controller.ui.UndoableAction last_;
    private RpCommand lastCommand_;
    private ArrayList<RpCommand> commandArray_;
    protected UndoActionController() {
        // no ICONS
        super(DESC_TEXT, null);
        lastCommand_ = null;
        setEnabled(false);
        commandArray_=new ArrayList<RpCommand>();
    }

    static public void createInstance() {
        if (instance_ == null)
            instance_ = new UndoActionController();
    }

    public void addAction(RpCommand action) {
        lastCommand_ = action;
        commandArray_.add(action);
        setEnabled(true);
    }

    public void actionPerformed(ActionEvent action) {
        if (lastCommand_ != null) {
            // undo and unselect...
            lastCommand_.unexecute();
            UIController.instance().setState(new GEOM_SELECTION());
        }
        setEnabled(false);
    }

    static public UndoActionController instance() { return instance_; }
    
    
    public Iterator<RpCommand> getCommandIterator(){return commandArray_.iterator();}

    
    public void removeLastCommand(){
        
        if(lastCommand_!=null)
        commandArray_.remove(lastCommand_);
    }
    
    public RpCommand getLastCommand() {
        return lastCommand_;
    }
}
