/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller;

import rpn.component.RpGeomFactory;
import rpn.command.*;
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
        DragPlotCommand.instance().addPropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().addPropertyChangeListener(this);
        BifurcationRefineCommand.instance().addPropertyChangeListener(this);      // ****
        ChangeXZeroCommand.instance().addPropertyChangeListener(this);



    }

    @Override
    protected void unregister() {
        DragPlotCommand.instance().removePropertyChangeListener(this);
        ChangeFluxParamsCommand.instance().removePropertyChangeListener(this);
        BifurcationRefineCommand.instance().removePropertyChangeListener(this);
        ChangeXZeroCommand.instance().removePropertyChangeListener(this);

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
        
        System.out.println(change);

        if (change.getSource() instanceof DragPlotCommand) {
            ((HugoniotParams) ((HugoniotCurveCalcND) geomFactory_.rpCalc()).getParams()).setXZero((RealVector) change.getNewValue());
            geomFactory_.updateGeom();
            return;
        }

        super.propertyChange(change);
    }
}
