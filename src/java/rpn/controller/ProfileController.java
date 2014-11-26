/*
  * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.command.*;
import java.beans.PropertyChangeEvent;

public class ProfileController extends RpCalcController {

    //
    // Members
    //

    //
    // Constructors
    //

    //
    // Accessors/Mutators
    //

    //
    // Methods
    //
    @Override
    protected void register() {
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        ChangeDirectionCommand.instance().addPropertyChangeListener(this);
        ChangeXZeroCommand.instance().addPropertyChangeListener(this);
    }

    @Override
    protected void unregister() {
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        ChangeDirectionCommand.instance().removePropertyChangeListener(this);
        ChangeXZeroCommand.instance().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {


           super.propertyChange(change);



    }

    }
}
