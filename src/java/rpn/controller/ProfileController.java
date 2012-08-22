/*
  * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.usecase.*;
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
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeDirectionAgent.instance().addPropertyChangeListener(this);
        ChangeXZeroAgent.instance().addPropertyChangeListener(this);
    }

    @Override
    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeDirectionAgent.instance().removePropertyChangeListener(this);
        ChangeXZeroAgent.instance().removePropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {


           super.propertyChange(change);



    }

    }
}
