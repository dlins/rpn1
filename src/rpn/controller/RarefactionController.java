/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.usecase.*;
import java.beans.PropertyChangeEvent;
import rpn.component.RarefactionOrbitGeomFactory;

public class RarefactionController
        extends RpCalcController {
    //
    // Members
    //
    private RarefactionOrbitGeomFactory geomFactory_;

    //
    // Constructors
    //
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected void register() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeRarefactionXZeroAgent.instance().addPropertyChangeListener(this);
    }

    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeRarefactionXZeroAgent.instance().removePropertyChangeListener(this);
    }

    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (RarefactionOrbitGeomFactory) geom;
    }

    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        super.propertyChange(change);
    }

}
