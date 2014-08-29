/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;

public class ReferencePointSelectionCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Reference Point Selection";
    //
    // Members
    //
    static private ReferencePointSelectionCommand instance_ = null;

    int curveToSelect_;

    //
    // Constructors/Initializers
    //
    protected ReferencePointSelectionCommand() {

        super(DESC_TEXT);

    }


    static public ReferencePointSelectionCommand instance() {
        if (instance_ == null) {
            instance_ = new ReferencePointSelectionCommand();
        }
        return instance_;
    }

    @Override
    public void execute() {

         applyChange(new PropertyChangeEvent(this, "referencepoint", "antes ", "depois"));
      
    }

    @Override
    public void unexecute() {
     
    }

}
