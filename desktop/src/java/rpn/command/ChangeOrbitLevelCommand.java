/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

public class ChangeOrbitLevelCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Orbit Level";
    //
    // Members
    //
    private static ChangeOrbitLevelCommand instance_ = null;

    //
    // Constructors
    //
    protected ChangeOrbitLevelCommand() {
        super(DESC_TEXT);
    }

    public void execute() {

        applyChange(new PropertyChangeEvent(this, "level", null, null));
    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        Double newValue = (Double) log().getOldValue();
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    static public ChangeOrbitLevelCommand instance() {
        if (instance_ == null) {
            instance_ = new ChangeOrbitLevelCommand();
        }
        return instance_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }
}
