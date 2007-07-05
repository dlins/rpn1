/*
  * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.usecase.*;
import rpnumerics.PhasePoint;
import java.beans.PropertyChangeEvent;
import rpnumerics.HugoniotCurveCalc;

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
    protected void register() {
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        ChangeXZeroAgent.instance().addPropertyChangeListener(this);
    }

    protected void unregister() {
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        ChangeXZeroAgent.instance().removePropertyChangeListener(this);
    }

    public void install(RpGeomFactory geom) {
        super.install(geom);
        geomFactory_ = (HugoniotCurveGeomFactory)geom;
    }

    public void uninstall(RpGeomFactory geom) {
        super.uninstall(geom);
        geomFactory_ = null;
    }

    public void propertyChange(PropertyChangeEvent change) {
        // this is to avoid void notifications of enabled/disbled
        if (change.getPropertyName().compareTo("enabled") != 0) {
            if (change.getSource() instanceof ChangeXZeroAgent) {
                // updates the HugoniotFunction xzero
                ((HugoniotCurveCalc)geomFactory_.rpCalc()).uMinusChangeNotify((PhasePoint)change.getNewValue());
            }
            super.propertyChange(change);
        }
    }
}
