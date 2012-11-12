/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;
import rpn.RPnCurvesConfigPanel;
import rpnumerics.RPNUMERICS;

public class ChangeDirectionCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Orbit Direction";
    //
    // Members
    //
    static private ChangeDirectionCommand instance_ = null;

    //
    // Constructors
    //
    protected ChangeDirectionCommand() {
        super(DESC_TEXT);
    }

    public void execute() {
        RPNUMERICS.setDirection(RPnCurvesConfigPanel.getOrbitDirection());
        applyChange(new PropertyChangeEvent(this, "direction", null, null));
    }

    public void unexecute() {

    }

    static public ChangeDirectionCommand instance() {
        if (instance_ == null) {
            instance_ = new ChangeDirectionCommand();
        }
        return instance_;
    }
}
