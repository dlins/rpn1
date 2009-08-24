/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpn.RPnCurvesConfigPanel;
import rpnumerics.RPNUMERICS;

public class ChangeDirectionAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Orbit Direction";
    //
    // Members
    //
    static private ChangeDirectionAgent instance_ = null;

    //
    // Constructors
    //
    protected ChangeDirectionAgent() {
        super(DESC_TEXT);
    }

    public void execute() {
        RPNUMERICS.setDirection(RPnCurvesConfigPanel.getOrbitDirection());
    }

    public void unexecute() {

    }

    static public ChangeDirectionAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeDirectionAgent();
        }
        return instance_;
    }
}
