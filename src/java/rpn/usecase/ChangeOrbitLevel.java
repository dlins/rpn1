/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

public class ChangeOrbitLevel extends RpModelConfigChangeAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Orbit Level";
    //
    // Members
    //
    private static ChangeOrbitLevel instance_ = null;

    //
    // Constructors
    //
    protected ChangeOrbitLevel() {
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

    static public ChangeOrbitLevel instance() {
        if (instance_ == null) {
            instance_ = new ChangeOrbitLevel();
        }
        return instance_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        execute();
    }
}
