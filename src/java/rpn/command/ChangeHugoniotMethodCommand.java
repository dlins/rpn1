/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.command;

import java.beans.PropertyChangeEvent;


public class ChangeHugoniotMethodCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Hugoniot Method";
    //
    // Members
    //
    static private ChangeHugoniotMethodCommand instance_ = null;
    static private String methodName_=null;

    //
    // Constructors
    //
    protected ChangeHugoniotMethodCommand() {
        super(DESC_TEXT);
    }

    public void execute() {

        String oldMethodName = null;

      applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldMethodName, methodName_));
    }

    
    public void setMethodName(String methodName){
        
        methodName_=methodName;
        
    }
    
    
    public void unexecute() {
        String oldMethodName = (String)log().getOldValue();
        String newMethodName = (String)log().getNewValue();
        // removes the new changed XZero
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, newMethodName, oldMethodName));
    }

    static public ChangeHugoniotMethodCommand instance() {
        if (instance_ == null)
            instance_ = new ChangeHugoniotMethodCommand();
        return instance_;
    }
}
