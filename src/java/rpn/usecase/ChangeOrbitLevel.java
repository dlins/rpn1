/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import rpnumerics.RPNUMERICS;
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
        System.out.println("Chamando execute");
        Double oldValue = new Double(0.5);
        Double newValue = new Double(0.01);
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        System.out.println("OLD SIGMA = " + oldValue);
        Double newValue = (Double) log().getOldValue();
        RPNUMERICS.getShockProfile().setSigma(newValue);
        System.out.println("NEW SIGMA = " + newValue);
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
