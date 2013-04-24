/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;

public class ChangeCurveConfigurationCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

     private static String DESC_TEXT = "Change Curve Configuration Command";
    //
    // Members
    //
    static private ChangeCurveConfigurationCommand instance_ = null;


    //
    // Constructors
    //
    protected ChangeCurveConfigurationCommand() {
        super(DESC_TEXT);
    }

    @Override
    public void applyChange(PropertyChangeEvent event) {

      


    }

    public void execute() {
    }

    public void unexecute() {
    }
    
    
    static public ChangeCurveConfigurationCommand instance() {
        if (instance_ == null) {
            instance_ = new ChangeCurveConfigurationCommand();
        }
        return instance_;
    }
}
