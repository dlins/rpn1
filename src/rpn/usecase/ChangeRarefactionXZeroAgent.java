/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import rpnumerics.PhasePoint;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;
import java.beans.PropertyChangeEvent;
import rpn.controller.ui.*;

public class ChangeRarefactionXZeroAgent extends RpModelConfigChangeAgent {
    //
    // Constants
    //
    static public final String DESC_TEXT = "Change Rarefaction X-Zero";
    //
    // Members
    //
    static private ChangeRarefactionXZeroAgent instance_ = null;

    //
    // Constructors
    //
    protected ChangeRarefactionXZeroAgent() {
        super(DESC_TEXT);
    }

    public void execute() {

        RealVector[] userInputList = UIController.instance().userInputList();
        RealVector lastPointAdded = userInputList[userInputList.length - 1];
        PhasePoint newXZero = new PhasePoint(lastPointAdded);
        PhasePoint oldXZero = null;
        RPNUMERICS.getRarefactionProfile().setXZero(newXZero);
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, oldXZero, newXZero));
    }

    public void unexecute() {
        PhasePoint oldValue = (PhasePoint) log().getOldValue();
        PhasePoint newValue = (PhasePoint) log().getNewValue();
        // removes the new changed XZero
        applyChange(new PropertyChangeEvent(this, DESC_TEXT, newValue, oldValue));
    }

    static public ChangeRarefactionXZeroAgent instance() {
        if (instance_ == null) {
            instance_ = new ChangeRarefactionXZeroAgent();
        }
        return instance_;
    }
}
