/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.beans.PropertyChangeEvent;
import rpn.controller.ui.UIController;
import rpnumerics.Configuration;

public class ChangeCurveConfigurationCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Param";
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

        Configuration newConfiguration = (Configuration) event.getNewValue();
        
        UIController.instance().logCommand(new RpCommand(this.getActionSelected(), newConfiguration));


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
