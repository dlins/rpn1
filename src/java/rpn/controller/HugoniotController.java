/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.usecase.*;
import java.beans.PropertyChangeEvent;
import rpn.component.HugoniotCurveGeomFactory;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.HugoniotParams;
import wave.util.RealVector;

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
        DragPlotAgent.instance().addPropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().addPropertyChangeListener(this);
        BifurcationRefineAgent.instance().addPropertyChangeListener(this);      // ****
        ChangeXZeroAgent.instance().addPropertyChangeListener(this);

    }

    @Override
    protected void unregister() {
        DragPlotAgent.instance().removePropertyChangeListener(this);
        ChangeFluxParamsAgent.instance().removePropertyChangeListener(this);
        BifurcationRefineAgent.instance().removePropertyChangeListener(this);
        ChangeXZeroAgent.instance().removePropertyChangeListener(this);

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

        if (change.getSource() instanceof DragPlotAgent) {
            ((HugoniotParams) ((HugoniotCurveCalcND) geomFactory_.rpCalc()).getParams()).setXZero((RealVector) change.getNewValue());
            geomFactory_.updateGeom();
            return;
        }

        super.propertyChange(change);
    }
}
