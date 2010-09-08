/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.usecase.*;
import java.beans.PropertyChangeEvent;

public class HugoniotController extends RpCalcController {
    //
    // Members
    //

    private HugoniotCurveGeomFactory geomFactory_;

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

    }

    @Override
    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeDirectionAgent.instance().removePropertyChangeListener(this);

    }

    @Override
    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (HugoniotCurveGeomFactory) geom;
    }

    @Override
    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {
//            if (change.getSource() instanceof ChangeOrbitDirectionAgent) {
            // updates the HugoniotFunction xzero
//TESTE


//            ((HugoniotCurveCalc) geomFactory_.rpCalc()).uMinusChangeNotify((PhasePoint) change.getNewValue());
//            }

//            if (change.getSource() instanceof ChangeHugoniotMethodAgent) {
//                // updates the Hugoniot calc
//                RPNUMERICS.changeHugoniotMethod((String)change.getNewValue());
//            }
            super.propertyChange(change);
        }
    }
}
