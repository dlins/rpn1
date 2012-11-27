/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;
import java.util.Observable;
import java.util.Observer;
import rpn.RPnCurvesConfigPanel;
import rpnumerics.RPNUMERICS;
import wave.util.RealVector;

public class ChangeDirectionCommand extends RpModelConfigChangeCommand implements Observer{
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
        
        RealVector  oldDirection = new RealVector(1);
        oldDirection.setElement(0, new Double (RPnCurvesConfigPanel.getOrbitDirection()));
        applyChange(new PropertyChangeEvent(this, "direction", null, oldDirection));
    }

    public void unexecute() {

    }

    static public ChangeDirectionCommand instance() {
        if (instance_ == null) {
            instance_ = new ChangeDirectionCommand();
        }
        return instance_;
        
    }

    public void update(Observable o, Object arg) {
        System.out.println("Em update command");
        Integer newDirection = (Integer)arg;
        System.out.println(newDirection);
        RPNUMERICS.setDirection(newDirection);
        execute();

    }
}
